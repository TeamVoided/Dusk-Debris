package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.init.DuskParticles
import kotlin.math.sqrt

@Environment(EnvType.CLIENT)
class GunpowderExplosionEmitterParticle(world: ClientWorld, x: Double, y: Double, z: Double, val radius: Float) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    init {
        this.maxAge = 20
    }

    override fun tick() {
        for (i in 0..(radius.toInt() * radius.toInt()) / 2) {
            val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius
            var x = (random.nextDouble() - random.nextDouble())
            var y = (random.nextDouble() - random.nextDouble())
            var z = (random.nextDouble() - random.nextDouble())
            val a = sqrt(x * x + y * y + z * z)
            x = this.x + (randInRadius * x) / a
            y = this.y + (randInRadius * y) / a
            z = this.z + (randInRadius * z) / a
            world.addParticle(
                DuskParticles.GUNPOWDER_EXPLOSION_SMOKE,
                x,
                y,
                z,
                0.0,
                0.0,
                0.0
            )
        }
//        age.toFloat() / maxAge.toFloat()

        ++this.age
        if (this.age == this.maxAge) {
            this.markDead()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleFactory<FloatInputParticleEffect> {
        override fun createParticle(
            type: FloatInputParticleEffect,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return GunpowderExplosionEmitterParticle(world, x, y, z, type.radius)
        }
    }

    companion object {
        const val defaultRadius: Float = 4f
    }
}