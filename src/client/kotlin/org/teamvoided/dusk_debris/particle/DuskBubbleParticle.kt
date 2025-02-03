package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.cos
import kotlin.math.sin

@Deprecated("Just a copy of BubbleCubeParticle without the cube, will wait until Cube is finished before messing with it")
class DuskBubbleParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
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
        this.maxAge = 800 + random.nextInt(800)
        this.scale = 0.1f + 0.125f * random.nextFloat()
        this.velocityMultiplier = 0.005f
        this.gravityStrength = -(0.007f * random.nextFloat() + 0.002f)
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
            val particle = DuskBubbleParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}