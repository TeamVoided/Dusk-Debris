package org.teamvoided.dusk_debris.particle.cube_particles.abstracts

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.cube_particles.models.CubeSimple
import org.teamvoided.dusk_debris.particle.cube_particles.models.CubeUnwrapped

abstract class AbstractCubeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xVel: Double,
    yVel: Double,
    zVel: Double
) : SpriteBillboardKotlinParticle(world, x, y, z, xVel, yVel, zVel) {
    private var cubeShape: CubeSimple? = null
    var prevRot: Vec3 = Vec3.ZERO
    var rotation: Vec3 = Vec3.ZERO
    var rotSpeed: Vec3 = Vec3.ZERO


    override fun drawParticle(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        if (cubeShape != null) {
            val color = Vector4f(this.rCol, this.gCol, this.bCol, this.alpha)
            val brightness = this.getLightColor(tickDelta)
            val size = this.getSize(tickDelta)
            cubeShape!!.renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, size)
        } else {
            println("set sprite, fool")
            this.remove()
        }
    }

    open fun getRotation(tickDelta: Float): Vector3f {
        val x = Mth.lerp(tickDelta.toDouble(), prevRot.x, rotation.x).toFloat()
        val y = Mth.lerp(tickDelta.toDouble(), prevRot.y, rotation.y).toFloat()
        val z = Mth.lerp(tickDelta.toDouble(), prevRot.z, rotation.z).toFloat()
        return Vector3f(x, y, z)
    }

    override fun setSprite(spriteProvider: SpriteSet) {
        super.setSprite(spriteProvider)
        createShape()
        //if the particle texture changes size, simply copy this for setSpriteForAge
    }

    open fun createShape() {
        this.cubeShape = CubeUnwrapped(this.minU(), this.maxU(), this.minV(), this.maxV())
    }

    override val particleRotation = SingleQuadParticle.FacingCameraMode { rotation: Quaternionf, _, tickDelta: Float ->
        val rotate = this.getRotation(tickDelta)
        rotation.rotateZ(rotate.x)
        rotation.rotateY(rotate.y)
        rotation.rotateX(rotate.z)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE
}