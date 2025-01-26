package org.teamvoided.dusk_debris.particle.vanilla

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.particle.WaterSuspendParticle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

class AdditionalWaterSuspendParticle(
    world: ClientWorld,
    spriteProvider: SpriteProvider,
    x: Double,
    y: Double,
    z: Double,
    velx: Double,
    vely: Double,
    velz: Double
) : WaterSuspendParticle(world, spriteProvider, x, y, z, velx, vely, velz) {
    class UnderacidFactory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            val waterSuspendParticle = WaterSuspendParticle(world, spriteProvider, d, e, f, g, h, i)
            waterSuspendParticle.setColor(0.3f, 0.6f, 0.35f)
            return waterSuspendParticle
        }
    }
}