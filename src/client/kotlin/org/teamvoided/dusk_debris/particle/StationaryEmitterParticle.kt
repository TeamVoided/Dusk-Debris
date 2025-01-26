package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.ParticleEffect

class StationaryEmitterParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val particle: ParticleEffect,
    maxAge: Int,
    private val delayBetween: Int
) : NoRenderParticle(world, posX, posY, posZ) {
    init {
        this.maxAge = maxAge
        this.prevAngle = angle
        this.velocityMultiplier = 0.9f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ

    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else if (age % delayBetween == 0) {
            world.addParticle(
                particle,
                x, y, z,
                velocityX, velocityY, velocityZ
            )
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleFactory<StationaryEmitterParticleEffect> {
        override fun createParticle(
            particleEffect: StationaryEmitterParticleEffect,
            world: ClientWorld,
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