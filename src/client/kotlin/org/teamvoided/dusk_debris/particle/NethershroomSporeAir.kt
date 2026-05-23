package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.SuspendedParticle
import net.minecraft.core.particles.ParticleGroup
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import java.util.*

@Environment(EnvType.CLIENT)
class NethershroomSporeAir(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
    override fun createParticle(
        defaultParticleType: SimpleParticleType,
        world: ClientLevel,
        d: Double,
        e: Double,
        f: Double,
        g: Double,
        h: Double,
        i: Double
    ): Particle {
        val waterSuspendParticle: SuspendedParticle = object : SuspendedParticle(
            world,
            this.spriteProvider,
            d,
            e,
            f,
            0.0,
            -0.8,
            0.0
        ) {
            override fun getParticleGroup(): Optional<ParticleGroup> {
                return Optional.of(ParticleGroup.SPORE_BLOSSOM)
            }
        }
        waterSuspendParticle.setLifetime(Mth.randomBetweenInclusive(world.random, 500, 1000))
        waterSuspendParticle.gravity = 0.01f
        waterSuspendParticle.setColor(0.32f, 0.5f, 0.22f)
        return waterSuspendParticle
    }
}