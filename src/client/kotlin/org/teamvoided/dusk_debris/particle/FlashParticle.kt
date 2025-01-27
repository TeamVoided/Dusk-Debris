package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils
import java.awt.Color

class FlashParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    color: Color,
    maxAge: Int
) : SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {
    init {
        this.gravityStrength = 0f
        this.maxAge = maxAge
        this.colorRed = color.red / 255f
        this.colorGreen = color.green / 255f
        this.colorBlue = color.blue / 255f
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer?, camera: Camera?, tickDelta: Float) {
        this.setColorAlpha(0.6f - (age + tickDelta - 1f) * 0.25f * 0.5f)
        super.buildGeometry(vertexConsumer, camera, tickDelta)
    }

    override fun getSize(tickDelta: Float): Float {
        return 7.1f * MathHelper.sin((age + tickDelta - 1f) * 0.25f * Utils.rotate180)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<FlashParticleEffect> {
        override fun createParticle(
            type: FlashParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = FlashParticle(world, posX, posY, posZ, velX, velY, velZ, type.color, type.maxAge)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}