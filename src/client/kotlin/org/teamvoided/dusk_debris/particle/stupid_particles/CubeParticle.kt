package org.teamvoided.dusk_debris.particle.stupid_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.Direction
import org.joml.Quaternionf
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.stupid_particles.abstracts.SpriteBillboardKotlinParticle
import org.teamvoided.dusk_debris.particle.stupid_particles.model_test.CubeUnwrapped
import kotlin.math.abs

class CubeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xVel: Double,
    yVel: Double,
    zVel: Double
) : SpriteBillboardKotlinParticle(world, x, y, z, xVel, yVel, zVel) {
    private val cubeShape: CubeUnwrapped

    init {
        this.maxAge = 40 + random.nextInt(100)
        val sizeU = abs(this.maxU - this.minU).toInt()
        val sizeV = abs(this.maxV - this.minV).toInt()
        this.cubeShape = CubeUnwrapped(
            sizeU,
            sizeV,
            8f,
            8f,
            8f,
            false,
            16f, 16f,
            Direction.entries.toSet()
        )
        println(cubeShape)
    }

    override fun drawPlane(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        val color = Vector4f(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
        val brightness = this.getBrightness(tickDelta)
        val size = this.getSize(tickDelta)
        cubeShape.renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, size)
    }

    override fun tick() {
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE

    @Environment(EnvType.CLIENT)
    open class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType,
            world: ClientWorld,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            println(1)
            val particle = CubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            println(2)
            particle.setSprite(spriteProvider)
            println(3)
            return particle
        }
    }
}