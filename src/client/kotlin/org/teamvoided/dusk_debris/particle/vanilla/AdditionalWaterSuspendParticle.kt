package org.teamvoided.dusk_debris.particle.vanilla

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.SuspendedParticle
import net.minecraft.core.particles.SimpleParticleType

class AdditionalWaterSuspendParticle(
    world: ClientLevel,
    spriteProvider: SpriteSet,
    x: Double,
    y: Double,
    z: Double,
    velx: Double,
    vely: Double,
    velz: Double
) : SuspendedParticle(world, spriteProvider, x, y, z, velx, vely, velz) {
    class UnderacidFactory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
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
            val waterSuspendParticle = SuspendedParticle(world, spriteProvider, d, e, f, g, h, i)
            waterSuspendParticle.setColor(0.3f, 0.6f, 0.35f)
            return waterSuspendParticle
        }
    }
}