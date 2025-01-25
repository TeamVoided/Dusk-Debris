package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.abs
import kotlin.math.sqrt

class ElectricityParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private var yaw = 0f
    private var pitch = 0f

    init {
        maxAge = (random.nextInt(10) + 10)
        setEverything()
    }

    private fun setEverything() {
        yaw = random.nextFloat() * Utils.rotate360
        pitch = random.nextFloat() * Utils.rotate360
        angle = random.nextFloat() * Utils.rotate360 //roll
        scale = random.nextFloat() * 0.3f + 0.1f + (abs((age / maxAge) - 0.5f) + 0.5f) * 0.2f
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        quaternionf.rotationY(yaw).rotateX(-pitch).rotateZ(angle)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        quaternionf.rotationY(-Utils.rotate180 + yaw).rotateX(pitch).rotateZ(angle)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
    }

    override fun tick() {
        this.setSpriteForAge(this.spriteProvider)
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            setEverything()
        }
    }

    override fun getBrightness(tint: Float): Int {
        return 240
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT


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
            val particle = ElectricityParticle(world, posX, posY, posZ, spriteProvider)
            particle.setSpriteForAge(this.spriteProvider)
            return particle
        }
    }
}