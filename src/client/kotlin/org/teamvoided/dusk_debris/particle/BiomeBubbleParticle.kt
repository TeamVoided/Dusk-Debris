package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
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
    val maxAlpha: Float

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
        this.maxAlpha = random.nextFloat() * 0.2f + 0.1f
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tint: Float): Int = super.getBrightness(tint) + 255

    override fun tick() {
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

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        var alpha = (age + tickDelta) / maxAge
        alpha = 1f - 2f * alpha
        alpha *= alpha
        alpha = 1f - alpha
        this.colorAlpha = alpha * maxAlpha
        super.buildGeometry(vertexConsumer, camera, tickDelta)
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