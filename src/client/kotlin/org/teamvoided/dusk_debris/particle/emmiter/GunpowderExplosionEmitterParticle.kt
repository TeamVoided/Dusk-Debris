package org.teamvoided.dusk_debris.particle.emmiter

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.color.GunpowderExplosionEmitterParticleEffect
import org.teamvoided.dusk_debris.particle.color.GunpowderExplosionSmokeParticleEffect
import java.awt.Color

@Environment(EnvType.CLIENT)
class GunpowderExplosionEmitterParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    val radius: Float,
    val color: Color
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    init {
        this.lifetime = 10
    }

    override fun tick() {
        for (i in 0..(radius.toInt() * radius.toInt()) / 2) {
            val randInRadius = Mth.sqrt(random.nextFloat()) * radius
            val xyz = Vec3(
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble()
            ).normalize().scale(randInRadius.toDouble()).add(x, y, z)
            level.addParticle(
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
        if (this.age == this.lifetime) {
            this.remove()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory : ParticleProvider<GunpowderExplosionEmitterParticleEffect> {
        override fun createParticle(
            type: GunpowderExplosionEmitterParticleEffect,
            world: ClientLevel,
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