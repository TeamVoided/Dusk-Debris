package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes

@Environment(EnvType.CLIENT)
class BlunderbombEmitterParticle internal constructor(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) :
    SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    private val initialVelocity = 0.8
    private val velocityChange = -(initialVelocity / maxAge) * age + initialVelocity

    init {
        this.gravityStrength = 0.75f
        this.velocityMultiplier = 0.999f
        this.velocityX *= initialVelocity
        this.velocityY *= initialVelocity
        this.velocityZ *= initialVelocity
        this.velocityY = (random.nextFloat() * 0.4f + 0.05f).toDouble()
        this.scale *= random.nextFloat() * 2.0f + 0.2f
        this.maxAge = 20
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    public override fun getBrightness(tint: Float): Int {
        val i = super.getBrightness(tint)
        val j: Int = 2
        val k = i shr 16 and 255
        return 240 or (k shl 16)
    }

    override fun getSize(tickDelta: Float): Float {
        val f = (age.toFloat() + tickDelta) / maxAge.toFloat()
        return this.scale * (1.0f - f * f)
    }

    override fun tick() {
        super.tick()
        if (!this.dead) {
            val f = age.toFloat() / maxAge.toFloat()
            if (random.nextFloat() > f) {
                world.addParticle(
                    ParticleTypes.SMOKE,
                    this.x,
                    this.y,
                    this.z,
                    1.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
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
            val blunderbombParticle = BlunderbombEmitterParticle(world, d, e, f)
            blunderbombParticle.setSprite(this.spriteProvider)
            return blunderbombParticle
        }
    }
}