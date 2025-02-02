package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.pi
import kotlin.math.min

class BiomeBubbleParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
) : SpriteBillboardParticle(world, posX, posY, posZ) {
    val spinSpeed: Float

    init {
        this.maxAge = 80 + random.nextInt(420)
        this.scale = random.nextFloat() * 0.3f + 0.15f
        this.angle = (random.nextFloat()) * Utils.rotate360
        this.prevAngle = angle
        this.spinSpeed = (random.nextFloat() - 0.5f) * 0.01f * pi
        this.velocityX = (random.nextDouble() - 0.5f) * 0.005
        this.velocityY = (random.nextDouble() - 0.5f) * 0.02
        this.velocityZ = (random.nextDouble() - 0.5f) * 0.005
        this.colorAlpha = 0f
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tint: Float): Int = super.getBrightness(tint) + 255

    override fun tick() {
        if (age + 20 >= maxAge) {
            this.colorAlpha -= 0.05f
        } else if (this.colorAlpha < 1) {
            this.colorAlpha += 0.05f
        }
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevAngle = angle
            this.angle += spinSpeed
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
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
            val particle = BiomeBubbleParticle(world, posX, posY, posZ)
            particle.setSprite(spriteProvider)
            particle.scale(0.5f)
            return particle
        }
    }
}