package org.teamvoided.dusk_debris.particle.emmiter

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType

@Environment(EnvType.CLIENT)
class FirebombParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    init {
        this.xd = 2 * (random.nextDouble() - random.nextDouble())
        this.yd = 2 * (random.nextDouble() - random.nextDouble())
        this.zd = 2 * (random.nextDouble() - random.nextDouble())
        this.lifetime = 10
    }

    override fun tick() {
        this.friction *= 0.9f
        level.addParticle(
            ParticleTypes.FLAME,
            x,
            y,
            z,
            0.0,
            0.0,
            0.0
        )
        super.tick()
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleProvider<SimpleParticleType> {
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
            val flameParticle = FirebombParticle(world, d, e, f)
            return flameParticle
        }
    }
}