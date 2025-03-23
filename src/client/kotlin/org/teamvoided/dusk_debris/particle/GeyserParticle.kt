package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import kotlin.math.max
import kotlin.math.min

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
    val maxAlpha: Float

    init {
        val color = random.nextFloat() * 0.2f + 0.5f
        this.colorRed = color + 0.01f
        this.colorGreen = color
        this.colorBlue = color + 0.04f
        this.maxAlpha = random.nextFloat() * 0.6f + 0.4f
        this.colorAlpha = maxAlpha
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.gravityStrength = 1f
        this.scale = 1.0f
        this.maxAge = ((random.nextDouble() * 45).toInt() + 15)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val alphaAge = max(0.01f, maxAlpha - (age + tickDelta) * 0.015f)
        val alphaCamera = camera.focusedEntity.squaredDistanceTo(x, y, z).toFloat() / RANGE
        this.colorAlpha = min(alphaAge, alphaCamera)
        super.buildGeometry(vertexConsumer, camera, tickDelta)
    }

//    override fun tick() {
//        if (this.age < this.maxAge) {
//            this.velocityX += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
//            this.velocityZ += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
//        }
//        super.tick()
//    }

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

    companion object {
        const val RANGE = 1.0f
    }
}