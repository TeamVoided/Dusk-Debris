package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.BlockLeakParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils

class AstrasStrangeGoopParticle(world: ClientWorld, x: Double, y: Double, z: Double, fluid: Fluid) :
    BlockLeakParticle(world, x, y, z, fluid) {

    class FlyingGoop(
        world: ClientWorld,
        x: Double,
        y: Double,
        z: Double,
        velocityX: Double,
        velocityY: Double,
        velocityZ: Double,
        fluid: Fluid,
        particle: GoopFlyingParticleEffect
    ) : ContinuousFalling(world, x, y, z, fluid, null) {
        constructor(
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            fluid: Fluid,
            particle: GoopFlyingParticleEffect
        ) : this(world, x, y, z, 0.0, 0.0, 0.0, fluid, particle)

        var stoppedInDirection: Direction? = null

        init {
            maxAge = particle.maxAge() //(128.0 / (random.nextDouble() * 0.8 + 0.2)).toInt()
            this.velocityX = velocityX
            this.velocityY = velocityY
            this.velocityZ = velocityZ
            gravityStrength = 0.02f
        }

        override fun updateVelocity() {
            if (this.stoppedInDirection != null) {
                this.markDead()
                world.addParticle(
                    GoopLandedParticleEffect(maxAge, stoppedInDirection!!),
                    this.x,
                    this.y,
                    this.z,
                    0.0,
                    0.0,
                    0.0
                )
                val volume = MathHelper.nextBetween(this.random, 0.3f, 1.0f)
                world.playSound(
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.BLOCK_BEEHIVE_DRIP,
                    SoundCategory.BLOCKS,
                    volume,
                    1.0f,
                    false
                )
            }
        }

        override fun move(x: Double, y: Double, z: Double) {
            super.move(x, y, z)
            var dx = x
            var dy = y
            var dz = z
            if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && (dx * dx + dy * dy + dz * dz < 10000)) {
                val vec3d = Entity.adjustSingleAxisMovementForCollisions(
                    null as Entity?, Vec3d(dx, dy, dz),
                    this.boundingBox,
                    this.world, listOf()
                )
                dx = vec3d.x
                dy = vec3d.y
                dz = vec3d.z
            }

            if (dx != 0.0 || dy != 0.0 || dz != 0.0) {
                this.boundingBox = boundingBox.offset(dx, dy, dz)
                this.repositionFromBoundingBox()
            }

            if (x != dx) {
                if (x >= 0)
                    stoppedInDirection = Direction.EAST
                else
                    stoppedInDirection = Direction.WEST
                this.velocityX = 0.0
            }

            if (y != dy) {
                if (dy < 0.0)
                    this.onGround
                if (y >= 0)
                    stoppedInDirection = Direction.UP
                else
                    stoppedInDirection = Direction.DOWN

                this.velocityY = 0.0
            }

            if (z != dz) {
                if (y >= 0)
                    stoppedInDirection = Direction.SOUTH
                else
                    stoppedInDirection = Direction.NORTH
                this.velocityZ = 0.0
            }
        }
    }


    @Environment(EnvType.CLIENT)
    class LandedAndTransform(
        world: ClientWorld,
        d: Double,
        e: Double,
        f: Double,
        fluid: Fluid,
        private val particle: GoopLandedParticleEffect
    ) : BlockLeakParticle(world, d, e, f, fluid) {
        private var stoppedInDirection: Direction

        init {
            this.maxAge = this.particle.maxAge()
            this.stoppedInDirection = particle.direction()
            this.gravityStrength = 0f
            this.onGround = true
            this.angle = random.nextFloat() * Utils.rotate360
            this.prevAngle = angle
        }

        override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
            val quaternionf = Quaternionf()
            //x is pitch, top and bottom
            //y is yaw, left to right
            //z is roll, side to side
            when (this.stoppedInDirection.axis) {
                Direction.Axis.Y ->
                    if (camera.pos.y < this.y) quaternionf.rotationX(Utils.rotate90)
                    else quaternionf.rotationX(Utils.rotate270)

                Direction.Axis.X ->
                    if (camera.pos.x < this.x) quaternionf.rotationY(Utils.rotate270)
                    else quaternionf.rotationY(Utils.rotate90)

                Direction.Axis.Z ->
                    if (camera.pos.z < this.z) quaternionf.rotationY(Utils.rotate180)
            }
            if (this.angle != 0f) {
                quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle))
            }
            this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        }

        override fun method_60373(
            vertexConsumer: VertexConsumer,
            camera: Camera,
            quaternionf: Quaternionf,
            tickDelta: Float
        ) {
            if (stoppedInDirection == Direction.DOWN) {
                val vec3d = camera.pos
                val posX = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - vec3d.getX()).toFloat()
                val posY = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - vec3d.getY()).toFloat()
                val posZ = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - vec3d.getZ()).toFloat()
                this.method_60374(vertexConsumer, quaternionf, posX, posY + 0.0005f, posZ, tickDelta)
            } else super.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        }

        private fun fly() {
            this.markDead()
            world.addParticle(GoopFlyingParticleEffect(maxAge), this.x, this.y, this.z, 0.0, 0.0, 0.0)
        }

//        override fun updateAge() {
//            this.maxAge -= 7 // + 1, super already subtracts one
//            super.updateAge()
//        }

        override fun move(x: Double, y: Double, z: Double) {
            var dx = x
            var dy = y
            var dz = z
            if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && (dx * dx + dy * dy + dz * dz < 10000)) {
                val vec3d = Entity.adjustSingleAxisMovementForCollisions(
                    null as Entity?, Vec3d(dx, dy, dz),
                    this.boundingBox,
                    this.world, listOf()
                )
                dx = vec3d.x
                dy = vec3d.y
                dz = vec3d.z
            }

            if (dx != 0.0 || dy != 0.0 || dz != 0.0) {
                this.boundingBox = boundingBox.offset(dx, dy, dz)
                this.repositionFromBoundingBox()
            }

//            this.onGround = (y != dy && dy < 0.0)
            when (stoppedInDirection) {
                Direction.EAST -> this.velocityX = 0.05
                Direction.WEST -> this.velocityX = -0.05
                Direction.SOUTH -> this.velocityZ = 0.05
                Direction.NORTH -> this.velocityZ = -0.05
                Direction.UP -> this.velocityY = 0.05
                Direction.DOWN -> this.velocityY = -0.05
            }

            when (stoppedInDirection.axis) {
                Direction.Axis.Y -> if (dy != 0.0) fly()
                Direction.Axis.X -> if (dx != 0.0) fly()
                Direction.Axis.Z -> if (dz != 0.0) fly()
            }
//            sendMessageIngame("dy: $dy, y: $y, velocityY: $velocityY, add: " + (y + velocityY))
        }
    }

    class FallingGoopFactory(private val spriteProvider: SpriteProvider) :
        ParticleFactory<GoopFlyingParticleEffect> {
        override fun createParticle(
            particleEffect: GoopFlyingParticleEffect,
            clientWorld: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val particle =
                FlyingGoop(clientWorld, x, y, z, velocityX, velocityY, velocityZ, Fluids.EMPTY, particleEffect)
            particle.setSprite(spriteProvider)
            return particle
        }
    }

    class LandedGoopFactory(private val spriteProvider: SpriteProvider) :
        ParticleFactory<GoopLandedParticleEffect> {
        override fun createParticle(
            particleEffect: GoopLandedParticleEffect,
            clientWorld: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val particle: BlockLeakParticle = LandedAndTransform(clientWorld, x, y, z, Fluids.EMPTY, particleEffect)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}