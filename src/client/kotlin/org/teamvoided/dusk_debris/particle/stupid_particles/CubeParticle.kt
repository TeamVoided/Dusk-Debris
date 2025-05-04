package org.teamvoided.dusk_debris.particle.stupid_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.stupid_particles.abstracts.SpriteBillboardKotlinParticle
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeSimple
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeUnwrapped
import org.teamvoided.dusk_debris.util.Utils

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
    private var prevRot: Vec3d = Vec3d.ZERO
    private var rotation: Vec3d = Vec3d.ZERO
    private var rotSpeed: Vec3d = Vec3d.ZERO

    init {
        this.maxAge = 100
        rotation = Vec3d(
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360
        )
        prevRot = rotation
        rotSpeed = Vec3d(
            (random.nextDouble() - 0.5) * 0.1,
            (random.nextDouble() - 0.5) * 0.1,
            (random.nextDouble() - 0.5) * 0.1
        )
    }

    override fun drawParticle(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        if (cubeShape != null) {
            val color = Vector4f(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
            val brightness = this.getBrightness(tickDelta)
            val size = this.getSize(tickDelta)
            cubeShape!!.renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, size)
        } else {
            println("set sprite, fool")
            this.markDead()
        }
    }

    private fun getRotation(tickDelta: Float): Vector3f {
        val x = MathHelper.lerp(tickDelta.toDouble(), prevRot.x, rotation.x).toFloat()
        val y = MathHelper.lerp(tickDelta.toDouble(), prevRot.y, rotation.y).toFloat()
        val z = MathHelper.lerp(tickDelta.toDouble(), prevRot.z, rotation.z).toFloat()
        return Vector3f(x, y, z)
    }

    override fun tick() {
        prevPosX = x
        prevPosY = y
        prevPosZ = z
        prevRot = rotation
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            rotation = rotation.add(rotSpeed)
            x += velocityX
            y += velocityY
            z += velocityZ
            velocityX * velocityMultiplier
            velocityY * velocityMultiplier
            velocityZ * velocityMultiplier
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
        this.cubeShape = CubeUnwrapped(this.minU(), this.maxU(), this.minV(), this.maxV())
    }

    override val particleRotation = BillboardParticle.FacingCameraMode { rotation: Quaternionf, _, tickDelta: Float ->
        val rotate = this.getRotation(tickDelta)
        rotation.rotateZ(rotate.x)
        rotation.rotateY(rotate.y)
        rotation.rotateX(rotate.z)
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
            val particle = CubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}