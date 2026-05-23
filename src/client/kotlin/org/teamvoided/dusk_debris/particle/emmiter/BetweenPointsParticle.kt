package org.teamvoided.dusk_debris.particle.emmiter

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.BetweenPointsParticleEffect

class BetweenPointsParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    private val targetPos: Vec3,
    isOminous: Boolean,
    particleDistance: Int,
    private val rate: Int,
) : NoRenderParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    private val startPos: Vec3 = Vec3(x, y, z)
    private val particle: ParticleOptions

    init {
        val distance = (startPos.distanceTo(targetPos) * particleDistance)
        this.lifetime = (rate * distance).toInt()
        particle = if (isOminous) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
    }

    override fun tick() {
        if (age++ >= lifetime) {
            level.addParticle(
                particle,
                targetPos.x, targetPos.y, targetPos.z,
                0.0, 0.0, 0.0
            )
            this.remove()
        } else if (age % rate == 0) {

            val lerp = this.age.toDouble() / this.lifetime
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            this.x = Mth.lerp(lerp, startPos.x, targetPos.x())
            this.y = Mth.lerp(lerp, startPos.y, targetPos.y())
            this.z = Mth.lerp(lerp, startPos.z, targetPos.z())

            level.addParticle(
                particle,
                this.x + (random.nextDouble() - 0.5),
                this.y + (random.nextDouble() - 0.5),
                this.z + (random.nextDouble() - 0.5),
                0.0, 0.0, 0.0
            )
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleProvider<BetweenPointsParticleEffect> {
        override fun createParticle(
            particleEffect: BetweenPointsParticleEffect,
            world: ClientLevel,
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
