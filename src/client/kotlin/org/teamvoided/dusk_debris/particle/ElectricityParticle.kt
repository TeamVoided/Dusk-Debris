package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.abs

class ElectricityParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    private val spriteProvider: SpriteSet
) : TextureSheetParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private var yaw = 0f
    private var pitch = 0f
    private var visible = true

    init {
        lifetime = (random.nextInt(10) + 10)
        setEverything()
    }

    private fun setEverything() {
        yaw = random.nextFloat() * Utils.rotate360
        pitch = random.nextFloat() * Utils.rotate360
        roll = random.nextFloat() * Utils.rotate360 //roll
        quadSize = random.nextFloat() * 0.3f + 0.1f + (abs((age / lifetime) - 0.5f) + 0.5f) * 0.3f
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (visible) {
            val quaternionf = Quaternionf()
            quaternionf.rotationY(yaw).rotateX(-pitch).rotateZ(roll)
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
            quaternionf.rotationY(yaw - Utils.rotate180).rotateX(pitch).rotateZ(roll)
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
        }
    }

    override fun tick() {
        this.setSpriteFromAge(this.spriteProvider)
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            if (random.nextInt(2) != 0) visible = !visible
            if (visible) setEverything()
        }
    }

    override fun getLightColor(tint: Float): Int = 240

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT


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
            val particle = ElectricityParticle(world, posX, posY, posZ, spriteProvider)
            particle.setSpriteFromAge(this.spriteProvider)
            return particle
        }
    }
}