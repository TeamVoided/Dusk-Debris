package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.util.normalize
import java.awt.Color
import kotlin.math.sqrt

@Environment(EnvType.CLIENT)
class GunpowderExplosionEmitterParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    val radius: Float,
    val color: Color
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    init {
        this.maxAge = 10
    }

    override fun tick() {
        for (i in 0..(radius.toInt() * radius.toInt()) / 2) {
            val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius
            val xyz = Vec3d(
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble()
            ).normalize(randInRadius).add(x, y, z)
            world.addParticle(
                GunpowderExplosionSmokeParticleEffect(color),
                xyz.x,
                xyz.y,
                xyz.z,
                0.0,
                0.0,
                0.0
            )
        }

        ++this.age
        if (this.age == this.maxAge) {
            this.markDead()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleFactory<GunpowderExplosionEmitterParticleEffect> {
        override fun createParticle(
            type: GunpowderExplosionEmitterParticleEffect,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return GunpowderExplosionEmitterParticle(world, x, y, z, type.radius, type.color)
        }
    }

    companion object {
        const val defaultRadius: Float = 4f
    }
}