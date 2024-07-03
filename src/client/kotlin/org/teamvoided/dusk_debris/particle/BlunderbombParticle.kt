package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes

@Environment(EnvType.CLIENT)
class BlunderbombParticle internal constructor(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) :
    SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {

    private val velocityChange = 0.99f
    private val extraParticle = ParticleTypes.SMOKE

    init {
        this.velocityX = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityY = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityZ = (random.nextFloat() - random.nextFloat()).toDouble()
        this.velocityY += (random.nextFloat() * 0.4f).toDouble()
        this.gravityStrength = 1f
//        this.scale *= random.nextFloat() * 0.6f + 0.5f
        this.maxAge = 20
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.NO_RENDER
    }

    public override fun getBrightness(tint: Float): Int {
        val i = super.getBrightness(tint)
        val j: Int = 2
        val k = i shr 16 and 255
        return 240 or (k shl 16)
    }

//    override fun getSize(tickDelta: Float): Float {
//        val f = (age.toFloat() + tickDelta) / maxAge.toFloat()
//        return this.scale * (1.0f - f * f)
//    }

    override fun tick() {
        this.gravityStrength *= 0.9f
        this.velocityMultiplier *= 0.975f
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
            val blunderbombParticle = BlunderbombParticle(world, d, e, f)
            blunderbombParticle.setSprite(this.spriteProvider)
            return blunderbombParticle
        }
    }
}