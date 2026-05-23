package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.max
import kotlin.math.min

@Environment(EnvType.CLIENT)
open class GeyserParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    val maxAlpha: Float

    init {
        val color = random.nextFloat() * 0.2f + 0.5f
        this.rCol = color + 0.01f
        this.gCol = color
        this.bCol = color + 0.04f
        this.maxAlpha = random.nextFloat() * 0.6f + 0.4f
        this.alpha = maxAlpha
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.gravity = 1f
        this.quadSize = 1.0f
        this.lifetime = ((random.nextDouble() * 45).toInt() + 15)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val alphaAge = max(0.01f, maxAlpha - (age + tickDelta) * 0.015f)
        val alphaCamera = camera.entity.distanceToSqr(x, y, z).toFloat() / RANGE
        this.alpha = min(alphaAge, alphaCamera)
        super.render(vertexConsumer, camera, tickDelta)
    }

//    override fun tick() {
//        if (this.age < this.maxAge) {
//            this.velocityX += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
//            this.velocityZ += (random.nextFloat() / 5000.0f * (if (random.nextBoolean()) 1 else -1).toFloat()).toDouble()
//        }
//        super.tick()
//    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GeyserParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }

    companion object {
        const val RANGE = 1.0f
    }
}