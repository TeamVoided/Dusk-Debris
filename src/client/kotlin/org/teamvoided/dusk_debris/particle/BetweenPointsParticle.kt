package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.VibrationParticleEffect
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.event.PositionSource
import org.teamvoided.dusk_debris.block.mixin.TrialSpawnerParticlesMixin
import org.teamvoided.dusk_debris.util.addParticle
import org.teamvoided.dusk_debris.util.spawnParticles
import kotlin.math.sqrt

class BetweenPointsParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    private val targetPos: Vec3d,
    isOminous: Boolean,
    particleDistance: Int,
    private val rate: Int,
) : NoRenderParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    private val startPos: Vec3d = Vec3d(x, y, z)
    private val particle: ParticleEffect

    init {
        val distance = (startPos.distanceTo(targetPos) * particleDistance)
        this.maxAge = (rate * distance).toInt()
        particle = if (isOminous) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
    }

    override fun tick() {
        if (age++ >= maxAge) {
            world.addParticle(
                particle,
                targetPos.x, targetPos.y, targetPos.z,
                velocityX, velocityY, velocityZ
            )
            this.markDead()
        } else if (age % rate == 0) {

            val lerp = this.age.toDouble() / this.maxAge
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            this.x = MathHelper.lerp(lerp, startPos.x, targetPos.getX())
            this.y = MathHelper.lerp(lerp, startPos.y, targetPos.getY())
            this.z = MathHelper.lerp(lerp, startPos.z, targetPos.getZ())

            world.addParticle(
                particle,
                this.x + (random.nextDouble() - 0.5),
                this.y + (random.nextDouble() - 0.5),
                this.z + (random.nextDouble() - 0.5),
                velocityX, velocityY, velocityZ
            )
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleFactory<BetweenPointsParticleEffect> {
        override fun createParticle(
            particleEffect: BetweenPointsParticleEffect,
            world: ClientWorld,
            x: Double, y: Double, z: Double,
            velocityX: Double, velocityY: Double, velocityZ: Double
        ): Particle {
            val particle = BetweenPointsParticle(
                world,
                x, y, z,
                velocityX, velocityY, velocityZ,
                particleEffect.getTargetPos(),
                particleEffect.ominous,
                particleEffect.particleDistance,
                particleEffect.rate
            )
            return particle
        }
    }
}
