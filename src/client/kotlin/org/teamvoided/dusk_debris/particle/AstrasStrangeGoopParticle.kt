package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.particle.DefaultParticleType
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.util.sendMessageIngame
import kotlin.math.abs

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
        }

        override fun updateVelocity() {
//            if (!this.onGround) {
//                fly()
//            }
        }

        private fun fly() {
            this.markDead()
            world.addParticle(GoopFlyingParticleEffect(maxAge), this.x, this.y, this.z, 0.0, 0.0, 0.0)
        }

        override fun updateAge() {
//            this.maxAge -= 7 // + 1, super already subtracts one
            super.updateAge()
        }

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
            particle.gravityStrength = 0.01f
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