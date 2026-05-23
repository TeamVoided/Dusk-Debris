package org.teamvoided.dusk_debris.particle.cube_particles.abstracts

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class BillboardKotlinParticle : Particle {
    protected var scale: Float

    protected constructor(world: ClientLevel, x: Double, y: Double, z: Double) : super(world, x, y, z) {
        this.scale = 1f
    }

    protected constructor(
        world: ClientLevel,
        x: Double, y: Double, z: Double,
        xVel: Double, yVel: Double, zVel: Double
    ) : super(world, x, y, z, xVel, yVel, zVel) {
        this.scale = 1f
    }

    open val particleRotation: SingleQuadParticle.FacingCameraMode = SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ


    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        particleRotation.setRotation(quaternionf, camera, tickDelta)
        if (this.roll != 0f) {
            quaternionf.rotateZ(Mth.lerp(tickDelta, this.oRoll, this.roll))
        }

        this.lerpPosition(vertexConsumer, camera, quaternionf, tickDelta)
    }

    open fun lerpPosition(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) {
        val camera = camera.position
        val x = (Mth.lerp(tickDelta.toDouble(), this.xo, this.x) - camera.x).toFloat()
        val y = (Mth.lerp(tickDelta.toDouble(), this.yo, this.y) - camera.y).toFloat()
        val z = (Mth.lerp(tickDelta.toDouble(), this.zo, this.z) - camera.z).toFloat()
        this.drawParticle(vertexConsumer, quaternionf, x, y, z, tickDelta)
    }

    open fun drawParticle(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        val size = this.getSize(tickDelta)
        val minU = this.minU()
        val maxU = this.maxU()
        val minV = this.minV()
        val maxV = this.maxV()
        val brightness = this.getLightColor(tickDelta)
        this.corner(vertexConsumer, quaternionf, x, y, z,  1f, -1f, size, maxU, maxV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z,  1f,  1f, size, maxU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f,  1f, size, minU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f, -1f, size, minU, maxV, brightness)
    }

    open fun corner(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        textureCordHor: Float,
        textureCordVer: Float,
        size: Float,
        u: Float,
        v: Float,
        brightness: Int
    ) {
        val vector3f = Vector3f(textureCordHor, textureCordVer, 0f).rotate(quaternionf).mul(size).add(x, y, z)
        vertexConsumer
            .addVertex(vector3f.x, vector3f.y, vector3f.z)
            .setUv(u, v)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
            .setLight(brightness)
    }

    open fun getSize(tickDelta: Float): Float = this.scale

    override fun scale(scale: Float): Particle {
        this.scale *= scale
        return super.scale(scale)
    }

    protected abstract fun minU(): Float
    protected abstract fun maxU(): Float
    protected abstract fun minV(): Float
    protected abstract fun maxV(): Float
}
