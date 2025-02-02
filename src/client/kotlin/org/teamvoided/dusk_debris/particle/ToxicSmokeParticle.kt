package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import java.awt.Color

@Environment(EnvType.CLIENT)
open class ToxicSmokeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val color: Color
) : SpriteBillboardParticle(world, x, y, z) {

    init {
        this.velocityX = (random.nextFloat() - random.nextFloat()) * 0.05
        this.velocityY = (random.nextFloat() - random.nextFloat()) * 0.05
        this.velocityZ = (random.nextFloat() - random.nextFloat()) * 0.05
        this.gravityStrength = 0f
        this.colorRed = (color.red / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
        this.colorGreen = (color.green / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
        this.colorBlue = (color.blue / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
//        this.colorAlpha = random.nextFloat() * 0.5f + 0.5f
        this.scale = 1.0f
//        this.maxAge = 80
        this.maxAge = ((random.nextFloat() * 80).toInt() + 60)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.velocityX *= 0.975
            this.velocityY *= 0.975
            this.velocityZ *= 0.975
            this.move(this.velocityX, this.velocityY, this.velocityZ)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<NethershroomSporeParticleEffect> {
        override fun createParticle(
            type: NethershroomSporeParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = ToxicSmokeParticle(world, posX, posY, posZ, velX, velY, velZ, type.color)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}