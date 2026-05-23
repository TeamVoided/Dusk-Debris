package org.teamvoided.dusk_debris.particle.emmiter

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import org.teamvoided.dusk_debris.particle.StationaryEmitterParticleEffect

class StationaryEmitterParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val particle: ParticleOptions,
    maxAge: Int,
    private val delayBetween: Int
) : NoRenderParticle(world, posX, posY, posZ) {
    init {
        this.lifetime = maxAge
        this.oRoll = roll
        this.friction = 0.9f
        this.xd = velX
        this.yd = velY
        this.zd = velZ

    }

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else if (age % delayBetween == 0) {
            level.addParticle(
                particle,
                x, y, z,
                xd, yd, zd
            )
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleProvider<StationaryEmitterParticleEffect> {
        override fun createParticle(
            particleEffect: StationaryEmitterParticleEffect,
            world: ClientLevel,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            return StationaryEmitterParticle(
                world,
                posX, posY, posZ,
                velX, velY, velZ,
                particleEffect.particle(),
                particleEffect.maxAge(),
                particleEffect.delayBetween()
            )
        }
    }
}