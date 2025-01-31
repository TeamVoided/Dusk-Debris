package org.teamvoided.dusk_debris.particle

import com.ibm.icu.impl.ICUService.Factory
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.Box
import org.teamvoided.dusk_debris.util.Utils
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
    val rotOffset: Float
    val radius: Float
    val speed: Float

    init {
        this.x = x
        this.y = y
        this.z = z
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.maxAge = 500
        this.scale = 0.4f + 0.125f * random.nextFloat()
        this.velocityMultiplier = 0.005f
        this.gravityStrength = -(0.005f * random.nextFloat() + 0.001f)
        this.colorAlpha = 0f

        this.rotOffset = random.nextFloat() * Utils.rotate360
        this.radius = random.nextFloat() * 0.1f + 0.01f
        this.speed = random.nextFloat() * 0.1f + 0.01f
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age + 20 >= maxAge) {
            this.colorAlpha -= 0.05f
        } else if (this.colorAlpha < 1) {
            this.colorAlpha += 0.05f
        }
        if (age++ < this.maxAge) {
            val the = age * 0.05f * Utils.rotate360 * speed + rotOffset // * 3.18318
            this.velocityX += cos(the) * 0.05f * radius
            this.velocityZ += sin(the) * 0.05f * radius
            this.velocityY -= gravityStrength
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            this.velocityX = 0.0
            this.velocityY = 0.0
            this.velocityZ = 0.0

            if (this.x == this.prevPosX || this.y == this.prevPosY || this.z == this.prevPosZ) {
                this.markDead()
            }
        } else {
            this.markDead()
        }
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT


    @Environment(EnvType.CLIENT)
    open class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType,
            world: ClientWorld,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = BubbleCubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }

    @Environment(EnvType.CLIENT)
    class SmallFactory(spriteProvider: SpriteProvider) : Factory(spriteProvider) {
        override fun createParticle(
            type: DefaultParticleType,
            world: ClientWorld,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = super.createParticle(type, world, posX, posY, posZ, velX, velY, velZ)
            particle.scale(0.5f)
            return particle
        }
    }
}