package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import java.awt.Color

@Environment(EnvType.CLIENT)
open class GunpowderExplosionSmokeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    val color: Color
) :
    SpriteBillboardParticle(world, x, y, z) {

    init {
        this.velocityY += Math.random() * 0.05
        this.velocityX += (Math.random() - Math.random()) / 3
        this.velocityZ += (Math.random() - Math.random()) / 3
        this.gravityStrength = 0f
        this.colorRed = color.red / 255f
        this.colorGreen = color.green / 255f
        this.colorBlue = color.blue / 255f
        this.scale = 0.33f * (random.nextFloat() * random.nextFloat() * 6.0f + 1.0f)
        this.maxAge = ((random.nextFloat() * 80).toInt() + 60)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT
    }

    override fun tick() {
        val multiplier = -((this.age / this.maxAge.toFloat()) * (this.age / this.maxAge.toFloat())) * 0.8f + 1f
        if (this.colorRed > 0.2) this.colorRed *= multiplier
        if (this.colorGreen > 0.2) this.colorGreen *= multiplier
        if (this.colorBlue > 0.2) this.colorBlue *= multiplier
        this.colorAlpha *=
            -((this.age / this.maxAge.toFloat()) * (this.age / this.maxAge.toFloat() * (this.age / this.maxAge.toFloat()))) + 1f
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.velocityX *= 0.75
            this.velocityY *= 0.95
            this.velocityZ *= 0.75
            this.move(this.velocityX, this.velocityY, this.velocityZ)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<GunpowderExplosionSmokeParticleEffect> {
        override fun createParticle(
            type: GunpowderExplosionSmokeParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GunpowderExplosionSmokeParticle(world, posX, posY, posZ, type.color)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}