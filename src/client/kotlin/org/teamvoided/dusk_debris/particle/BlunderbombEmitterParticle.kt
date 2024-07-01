package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import kotlin.math.max

@Environment(EnvType.CLIENT)
class BlunderbombEmitterParticle internal constructor(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) :
    SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    private val initialVelocity = 0.8
    private val velocityChange = 0.9f
    private val extraParticle = ParticleTypes.SMOKE

    init {
        this.velocityX = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityY = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityZ = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityY += (random.nextFloat() * 0.4f).toDouble()
        this.gravityStrength = 0.75f
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
        this.gravityStrength *= velocityChange
        this.velocityMultiplier *= velocityChange
        super.tick()
        if (!this.dead) {
            val f = age.toFloat() / maxAge.toFloat()
            if (random.nextFloat() > f) {
                world.addParticle(
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