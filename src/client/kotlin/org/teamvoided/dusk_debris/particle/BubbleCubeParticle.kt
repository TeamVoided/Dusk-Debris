package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import kotlin.math.cos
import kotlin.math.sin

class BubbleCubeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : CubeParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        this.x = x
        this.y = y
        this.z = z
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.maxAge = 1000
        this.scale = 0.5f
        this.velocityMultiplier = 0.001f
        this.gravityStrength = -0.05f
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age++ < this.maxAge) {
            val the = age * 0.1f
            val rot = 0.5f * 0.00001f * (age * age)
            this.velocityX += cos(the) * rot
            this.velocityZ += sin(the) * rot
            this.velocityY -= gravityStrength
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            this.velocityX *= velocityMultiplier
            this.velocityY *= velocityMultiplier
            this.velocityZ *= velocityMultiplier
        } else {
            this.markDead()
        }


//        if (this.age++ >= this.maxAge || (this.y == this.prevPosY)) {
//            this.markDead()
//        } else {
//            this.prevPosX = this.x
//            this.prevPosY = this.y
//            this.prevPosZ = this.z
//            this.move(this.velocityX, this.velocityY, this.velocityZ)
//        }
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT


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
            val particle = BubbleCubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }

    @Environment(EnvType.CLIENT)
    class SmallFactory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
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
            val particle = BubbleCubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            particle.scale(0.5f)
            return particle
        }
    }
}