package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.pi
import org.teamvoided.dusk_debris.util.Utils.rotate90

class DrainedSoulParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {
    val spinSpeed: Float

    init {
        this.setSpriteForAge(spriteProvider)
        this.maxAge = 12 + random.nextInt(8)
        this.scale = random.nextFloat() * 0.667f + 0.333f
        this.angle = (random.nextFloat()) * Utils.rotate360
        this.prevAngle = angle
        this.spinSpeed = (random.nextFloat() - 0.5f) * 0.2f * pi
        this.velocityMultiplier = 0.9f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT
    }

    public override fun getBrightness(tint: Float): Int {
        return 15728880
    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevAngle = angle
            this.angle += spinSpeed()
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            velocityX *= velocityMultiplier
            velocityY *= velocityMultiplier
            velocityZ *= velocityMultiplier
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
            this.setSpriteForAge(this.spriteProvider)
        }
    }

    private fun spinSpeed(): Float {
        var value = age / maxAge.toFloat()
        value = 1f - 2 * value
        value *= value
        value = 1f - value
        return this.spinSpeed * value
    }

    @Environment(EnvType.CLIENT)
    class SmallFactory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
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
            val particle: Particle = DrainedSoulParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.scale(0.15f)
            return particle
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
            val particle: Particle = DrainedSoulParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.scale(0.5f)
            return particle
        }
    }
}