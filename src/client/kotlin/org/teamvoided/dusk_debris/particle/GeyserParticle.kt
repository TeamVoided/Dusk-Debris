package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
open class GeyserParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        val color = random.nextFloat() * 0.2f + 0.5f
        this.velocityX = (random.nextDouble() - random.nextDouble()) * 0.15
        this.velocityY = random.nextDouble() + 0.6
        this.velocityZ = (random.nextDouble() - random.nextDouble()) * 0.15
        this.gravityStrength = 1f
        this.colorRed = color + 0.01f
        this.colorGreen = color
        this.colorBlue = color + 0.04f
        this.colorAlpha = random.nextFloat() * 0.6f + 0.4f
        this.scale = 1.0f
        this.maxAge = ((random.nextDouble() * 45).toInt() + 15)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        if (this.age < this.maxAge && !(this.colorAlpha <= 0.0f)) {
//            this.velocityX += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
//            this.velocityZ += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
            if (this.colorAlpha > 0.01f) {
                this.colorAlpha -= 0.015f
            }
        }
        super.tick()
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GeyserParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}