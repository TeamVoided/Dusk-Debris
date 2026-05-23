package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.particle.color.FlashParticleEffect
import org.teamvoided.dusk_debris.util.Utils
import java.awt.Color

class FlashParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    color: Color,
    maxAge: Int
) : TextureSheetParticle(world, x, y, z, velX, velY, velZ) {
    init {
        this.gravity = 0f
        this.lifetime = maxAge
        this.rCol = color.red / 255f
        this.gCol = color.green / 255f
        this.bCol = color.blue / 255f
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun render(vertexConsumer: VertexConsumer?, camera: Camera?, tickDelta: Float) {
        this.setAlpha(0.6f - (age + tickDelta - 1f) * 0.25f * 0.5f)
        super.render(vertexConsumer, camera, tickDelta)
    }

    override fun getQuadSize(tickDelta: Float): Float {
        return 7.1f * Mth.sin((age + tickDelta - 1f) * 0.25f * Utils.rotate180)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<FlashParticleEffect> {
        override fun createParticle(
            type: FlashParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = FlashParticle(world, posX, posY, posZ, velX, velY, velZ, type.color, type.maxAge)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}