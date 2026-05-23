package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.DripParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils

class AstrasStrangeGoopParticle(world: ClientLevel, x: Double, y: Double, z: Double, fluid: Fluid) :
    DripParticle(world, x, y, z, fluid) {

    class FlyingGoop(
        world: ClientLevel,
        x: Double,
        y: Double,
        z: Double,
        velocityX: Double,
        velocityY: Double,
        velocityZ: Double,
        fluid: Fluid,
        particle: GoopFlyingParticleEffect
    ) : FallAndLandParticle(world, x, y, z, fluid, null) {
        constructor(
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            fluid: Fluid,
            particle: GoopFlyingParticleEffect
        ) : this(world, x, y, z, 0.0, 0.0, 0.0, fluid, particle)

        var stoppedInDirection: Direction? = null

        init {
            lifetime = particle.maxAge() //(128.0 / (random.nextDouble() * 0.8 + 0.2)).toInt()
            this.xd = velocityX
            this.yd = velocityY
            this.zd = velocityZ
            gravity = 0.02f
        }

        override fun postMoveUpdate() {
            if (this.stoppedInDirection != null) {
                this.remove()
                level.addParticle(
                    GoopLandedParticleEffect(lifetime, stoppedInDirection!!),
                    this.x,
                    this.y,
                    this.z,
                    0.0,
                    0.0,
                    0.0
                )
                val volume = Mth.randomBetween(this.random, 0.3f, 1.0f)
                level.playLocalSound(
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.BEEHIVE_DRIP,
                    SoundSource.BLOCKS,
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
                val vec3d = Entity.collideBoundingBox(
                    null as Entity?, Vec3(dx, dy, dz),
                    this.boundingBox,
                    this.level, listOf()
                )
                dx = vec3d.x
                dy = vec3d.y
                dz = vec3d.z
            }

            if (dx != 0.0 || dy != 0.0 || dz != 0.0) {
                this.boundingBox = boundingBox.move(dx, dy, dz)
                this.setLocationFromBoundingbox()
            }

            if (x != dx) {
                stoppedInDirection = if (x >= 0) Direction.EAST else Direction.WEST
            }

            if (y != dy) {
                stoppedInDirection = if (y >= 0) Direction.UP else Direction.DOWN
            }

            if (z != dz) {
                stoppedInDirection = if (y >= 0) Direction.SOUTH else Direction.NORTH
            }
        }
    }


    @Environment(EnvType.CLIENT)
    class LandedAndTransform(
        world: ClientLevel,
        d: Double,
        e: Double,
        f: Double,
        fluid: Fluid,
        private val particle: GoopLandedParticleEffect
    ) : DripParticle(world, d, e, f, fluid) {
        private var stoppedInDirection: Direction

        init {
            this.lifetime = this.particle.maxAge()
            this.stoppedInDirection = particle.direction()
            this.gravity = 0f
            this.onGround = true
            this.roll = random.nextFloat() * Utils.rotate360
            this.oRoll = roll
        }

        override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
            val quaternionf = Quaternionf()
            //x is pitch, top and bottom
            //y is yaw, left to right
            //z is roll, side to side
            when (this.stoppedInDirection.axis) {
                Direction.Axis.Y ->
                    if (camera.position.y < this.y) quaternionf.rotationX(Utils.rotate90)
                    else quaternionf.rotationX(Utils.rotate270)

                Direction.Axis.X ->
                    if (camera.position.x < this.x) quaternionf.rotationY(Utils.rotate270)
                    else quaternionf.rotationY(Utils.rotate90)

                Direction.Axis.Z ->
                    if (camera.position.z < this.z) quaternionf.rotationY(Utils.rotate180)
            }
            if (this.roll != 0f) {
                quaternionf.rotateZ(Mth.lerp(tickDelta, this.oRoll, this.roll))
            }
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
        }

        override fun renderRotatedQuad(
            vertexConsumer: VertexConsumer,
            camera: Camera,
            quaternionf: Quaternionf,
            tickDelta: Float
        ) {
            if (stoppedInDirection == Direction.DOWN) {
                val vec3d = camera.position
                val posX = (Mth.lerp(tickDelta.toDouble(), this.xo, this.x) - vec3d.x()).toFloat()
                val posY = (Mth.lerp(tickDelta.toDouble(), this.yo, this.y) - vec3d.y()).toFloat()
                val posZ = (Mth.lerp(tickDelta.toDouble(), this.zo, this.z) - vec3d.z()).toFloat()
                this.renderRotatedQuad(vertexConsumer, quaternionf, posX, posY + 0.0005f, posZ, tickDelta)
            } else super.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
        }

        private fun fly() {
            this.remove()
            level.addParticle(GoopFlyingParticleEffect(lifetime), this.x, this.y, this.z, 0.0, 0.0, 0.0)
        }

//        override fun updateAge() {
//            this.maxAge -= 7 // + 1, super already subtracts one
//            super.updateAge()
//        }

        override fun tick() {
            when (stoppedInDirection) {
                Direction.EAST -> this.xd = 0.05
                Direction.WEST -> this.xd = -0.05
                Direction.SOUTH -> this.zd = 0.05
                Direction.NORTH -> this.zd = -0.05
                Direction.UP -> this.yd = 0.05
                Direction.DOWN -> this.yd = -0.05
            }
            super.tick()
        }

        override fun move(x: Double, y: Double, z: Double) {
            var dx = x
            var dy = y
            var dz = z
            if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && (dx * dx + dy * dy + dz * dz < 10000)) {
                val vec3d = Entity.collideBoundingBox(
                    null as Entity?, Vec3(dx, dy, dz),
                    this.boundingBox,
                    this.level, listOf()
                )
                dx = vec3d.x
                dy = vec3d.y
                dz = vec3d.z
            }

            if (dx != 0.0 || dy != 0.0 || dz != 0.0) {
                this.boundingBox = boundingBox.move(dx, dy, dz)
                this.setLocationFromBoundingbox()
            }

//            this.onGround = (y != dy && dy < 0.0)

            when (stoppedInDirection.axis) {
                Direction.Axis.Y -> if (dy != 0.0) fly()
                Direction.Axis.X -> if (dx != 0.0) fly()
                Direction.Axis.Z -> if (dz != 0.0) fly()
            }
//            sendMessageIngame("dy: $dy, y: $y, velocityY: $velocityY, add: " + (y + velocityY))
        }
    }

    class FallingGoopFactory(private val spriteProvider: SpriteSet) :
        ParticleProvider<GoopFlyingParticleEffect> {
        override fun createParticle(
            particleEffect: GoopFlyingParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val particle =
                FlyingGoop(clientWorld, x, y, z, velocityX, velocityY, velocityZ, Fluids.EMPTY, particleEffect)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }

    class LandedGoopFactory(private val spriteProvider: SpriteSet) :
        ParticleProvider<GoopLandedParticleEffect> {
        override fun createParticle(
            particleEffect: GoopLandedParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val particle: DripParticle = LandedAndTransform(clientWorld, x, y, z, Fluids.EMPTY, particleEffect)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}