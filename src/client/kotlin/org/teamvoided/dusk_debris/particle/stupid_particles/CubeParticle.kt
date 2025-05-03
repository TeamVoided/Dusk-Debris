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
import org.joml.Quaternionf
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.stupid_particles.abstracts.SpriteBillboardKotlinParticle
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeUnwrapped
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeSimple
import org.teamvoided.dusk_debris.util.NONE

open class CubeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xVel: Double,
    yVel: Double,
    zVel: Double
) : SpriteBillboardKotlinParticle(world, x, y, z, xVel, yVel, zVel) {
    private var cubeShape: CubeSimple? = null

    override fun drawParticle(
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
        if (cubeShape != null) {
            cubeShape!!.renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, size)
        } else {
            println("set sprite, fool")
            this.markDead()
        }
    }

    override fun tick() {
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            //this.x += this.velocityX
            //this.y += this.velocityY
            //this.z += this.velocityZ
            //this.velocityX * this.velocityMultiplier
            //this.velocityY * this.velocityMultiplier
            //this.velocityZ * this.velocityMultiplier
        }
    }

    override fun setSprite(spriteProvider: SpriteProvider) {
        super.setSprite(spriteProvider)
        createShape()
    }

    override fun setSpriteForAge(spriteProvider: SpriteProvider) {
        super.setSpriteForAge(spriteProvider)
        createShape()
    }

    open fun createShape() {
        this.cubeShape = CubeUnwrapped(
            this.minU(),
            this.maxU(),
            this.minV(),
            this.maxV(),
            2f,
            2f,
            4f,
        )
    }

    override val facingCameraMode = NONE

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE

    @Environment(EnvType.CLIENT)
    open class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType,
            world: ClientWorld,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = CubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}