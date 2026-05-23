package org.teamvoided.dusk_debris.particle.emmiter

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType

@Environment(EnvType.CLIENT)
class BlunderbombParticle internal constructor(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    private val extraParticle = ParticleTypes.SMOKE

    init {
        this.xd = (random.nextFloat() - random.nextFloat()).toDouble()
        this.yd = (random.nextFloat() - random.nextFloat()).toDouble()
        this.zd = (random.nextFloat() - random.nextFloat()).toDouble()
        this.yd += (random.nextFloat() * 0.4f).toDouble()
        this.gravity = 1f
        this.lifetime = 20
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.NO_RENDER
    }

    override fun tick() {
        this.gravity *= 0.9f
        this.friction *= 0.975f
        super.tick()
        if (!this.removed) {
            val f = age.toFloat() / lifetime.toFloat()
            if (random.nextFloat() > f) {
                level.addParticle(
                    extraParticle,
                    this.x,
                    this.y,
                    this.z,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
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
            val blunderbombParticle = BlunderbombParticle(world, d, e, f)
            return blunderbombParticle
        }
    }
}