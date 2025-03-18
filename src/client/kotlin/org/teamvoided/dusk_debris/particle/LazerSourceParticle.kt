package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils.pi
import java.awt.Color
import java.util.*
import kotlin.math.max

class LazerSourceParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    init {
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.scale = random.nextFloat() * 0.25f + 0.25f
        this.maxAge = 10 + random.nextInt(10)
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
    }

    override fun getSize(tickDelta: Float): Float = super.getSize(tickDelta) * mult(tickDelta)

    override fun method_60373(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) {
        val cameraPos = camera.pos
        val particlePos = Vec3d(
            lerp(tickDelta.toDouble(), this.prevPosX, this.x),
            lerp(tickDelta.toDouble(), this.prevPosY, this.y),
            lerp(tickDelta.toDouble(), this.prevPosZ, this.z)
        )

        val offsetPos = Vec3d(
            particlePos.x - cameraPos.x,
            particlePos.y - cameraPos.y,
            particlePos.z - cameraPos.z
        ).normalize().multiply(mult(tickDelta).toDouble())
        val returnPos = Vec3d(
            (particlePos.x - (offsetPos.x) - cameraPos.x),
            (particlePos.y - (offsetPos.y) - cameraPos.y),
            (particlePos.z - (offsetPos.z) - cameraPos.z)
        )

        this.method_60374(
            vertexConsumer,
            quaternionf,
            returnPos.x.toFloat(),
            returnPos.y.toFloat(),
            returnPos.z.toFloat(),
            tickDelta
        )
    }

    private fun mult(tickDelta: Float): Float {
        val mult = (2f / (maxAge - 1f)) * (age + tickDelta) - 1f
        return 1f - (mult * mult)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_LIT

    override fun getBrightness(tint: Float): Int = 240


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
            val particle = LazerSourceParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}