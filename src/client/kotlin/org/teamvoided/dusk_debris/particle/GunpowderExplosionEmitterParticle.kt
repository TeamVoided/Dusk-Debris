package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.init.DuskParticles
import kotlin.math.sqrt

@Environment(EnvType.CLIENT)
class GunpowderExplosionEmitterParticle(world: ClientWorld, x: Double, y: Double, z: Double, val radius: Float) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    //        constructor(world: ClientWorld, x: Double, y: Double, z: Double): super(world, x, y, z, 8.0f)
    init {
        this.maxAge = 10
    }

    override fun tick() {
        for (i in 0..radius.toInt()) {
            val x = (random.nextDouble() - random.nextDouble())
            val y = (random.nextDouble() - random.nextDouble())
            val z = (random.nextDouble() - random.nextDouble())
            val a = sqrt(x * x + y * y + z * z - y)
            world.addParticle(
                DuskParticles.GUNPOWDER_EXPLOSION_SMOKE,
                this.x + ((radius * x) / a),
                this.y + ((radius * y) / a),
                this.z + ((radius * z) / a),
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
            return GunpowderExplosionEmitterParticle(world, x, y, z, defaultRadius)
        }
    }

    companion object {
        const val defaultRadius: Float = 4f
//        private val RADIUS: Codec<Float> get() = Codec
//        val CODEC: MapCodec<GunpowderExplosionEmitterParticle>

        init {
//            CODEC =
//                RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<GunpowderExplosionEmitterParticle> ->
//                    instance.group(
//                        Codecs.POSITIVE_FLOAT.fieldOf("radius").forGetter{radius:GunpowderExplosionEmitterParticle -> radius.radius}
//                    ).apply(instance, Function)
//                }
        }
    }
}