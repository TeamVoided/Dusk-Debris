package org.teamvoided.dusk_debris.particle.stupid_particles.abstracts

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.BillboardParticle
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeSimple
import org.teamvoided.dusk_debris.particle.stupid_particles.models.CubeUnwrapped

abstract class AbstractCubeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xVel: Double,
    yVel: Double,
    zVel: Double
) : SpriteBillboardKotlinParticle(world, x, y, z, xVel, yVel, zVel) {
    private var cubeShape: CubeSimple? = null
    var prevRot: Vec3d = Vec3d.ZERO
    var rotation: Vec3d = Vec3d.ZERO
    var rotSpeed: Vec3d = Vec3d.ZERO


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

    open fun getRotation(tickDelta: Float): Vector3f {
        val x = MathHelper.lerp(tickDelta.toDouble(), prevRot.x, rotation.x).toFloat()
        val y = MathHelper.lerp(tickDelta.toDouble(), prevRot.y, rotation.y).toFloat()
        val z = MathHelper.lerp(tickDelta.toDouble(), prevRot.z, rotation.z).toFloat()
        return Vector3f(x, y, z)
    }

    override fun setSprite(spriteProvider: SpriteProvider) {
        super.setSprite(spriteProvider)
        createShape()
        //if the particle texture changes size, simply copy this for setSpriteForAge
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
}