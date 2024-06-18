package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.init.DuskParticles

@Environment(EnvType.CLIENT)
class GunpowderExplosionEmitterParticle(world: ClientWorld, x: Double, y: Double, z: Double) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    init {
        this.maxAge = 40
    }

    override fun tick() {
        for (i in 0..5) {
            val x = this.x + (random.nextDouble() - random.nextDouble()) * 4
            val y = this.y + (random.nextDouble() - random.nextDouble()) * 4
            val z = this.z + (random.nextDouble() - random.nextDouble()) * 4
            world.addParticle(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, x, y, z, (age.toFloat() / maxAge.toFloat()).toDouble(), 0.0, 0.0)
        }

        ++this.age
        if (this.age == this.maxAge) {
            this.markDead()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return GunpowderExplosionEmitterParticle(world, x, y, z)
        }
    }
}