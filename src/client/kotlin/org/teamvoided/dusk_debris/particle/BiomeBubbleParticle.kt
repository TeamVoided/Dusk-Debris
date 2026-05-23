package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.PI

class BiomeBubbleParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
) : TextureSheetParticle(world, posX, posY, posZ) {
    val spinSpeed: Float
    val maxAlpha: Float

    init {
        this.lifetime = 80 + random.nextInt(420)
        this.quadSize = random.nextFloat() * 0.85f + 0.15f
        this.roll = (random.nextFloat()) * Utils.rotate90 - Utils.rotate45
        this.oRoll = roll
        this.spinSpeed = (random.nextFloat() - 0.5f) * 0.01f * PI
        this.xd = (random.nextDouble() - 0.5f) * 0.005
        this.yd = (random.nextDouble() - 0.5f) * 0.02
        this.zd = (random.nextDouble() - 0.5f) * 0.005
        this.alpha = 0f
        this.maxAlpha = random.nextFloat() * 0.35f + 0.15f
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    public override fun getLightColor(tint: Float): Int = super.getLightColor(tint) + 255

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.oRoll = roll
            this.roll += spinSpeed
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
        }
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (age + tickDelta < lifetime) {
            var alpha = (age + tickDelta) / lifetime
            alpha = 1f - 2f * alpha
            alpha *= alpha
            alpha = 1f - alpha
            this.alpha = alpha * maxAlpha
        }
        super.render(vertexConsumer, camera, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = BiomeBubbleParticle(world, posX, posY, posZ)
            particle.pickSprite(spriteProvider)
            particle.scale(0.5f)
            return particle
        }
    }
}