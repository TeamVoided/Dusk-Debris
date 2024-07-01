package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
open class GunpowderExplosionSmokeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    spriteProvider: SpriteProvider,
//    colorRed: Float,
//    colorBlue: Float,
//    colorGreen: Float
) :
    SpriteBillboardParticle(world, x, y, z) {
    private val spriteProvider: SpriteProvider

    init {
        this.velocityY += Math.random() * 0.05
        this.gravityStrength = 0f
        this.spriteProvider = spriteProvider
        val grayscaleColor = random.nextFloat() * 0.5f + 0.5f
        this.colorRed = grayscaleColor
        this.colorBlue = grayscaleColor
        this.colorGreen = grayscaleColor
        this.colorAlpha = 1f
        this.scale = 0.33f * (random.nextFloat() * random.nextFloat() * 6.0f + 1.0f)
        this.maxAge = 60
//        this.maxAge = ((random.nextFloat() * 80).toInt() + 60)
        this.setSpriteForAge(spriteProvider)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        val multiplier = (maxAge - age) / maxAge.toFloat()
        this.colorRed *= multiplier
        this.colorBlue *= multiplier
        this.colorGreen *= multiplier
        this.colorAlpha *= multiplier
        super.tick()
        this.setSpriteForAge(this.spriteProvider)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            return GunpowderExplosionSmokeParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
        }
    }
}