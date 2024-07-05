package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes

@Environment(EnvType.CLIENT)
class FirebombParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    init {
        this.velocityX = 2 * (random.nextDouble() - random.nextDouble())
        this.velocityY = 2 * (random.nextDouble() - random.nextDouble())
        this.velocityZ = 2 * (random.nextDouble() - random.nextDouble())
        this.maxAge = 10
    }

    override fun tick() {
        this.velocityMultiplier *= 0.9f
        world.addParticle(
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
    class Factory : ParticleFactory<DefaultParticleType> {
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
            val flameParticle = FirebombParticle(world, d, e, f)
            return flameParticle
        }
    }
}