package org.teamvoided.dusk_debris.particle.cube_particles.abstracts

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.BillboardParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class BillboardKotlinParticle : Particle {
    protected var scale: Float

    protected constructor(world: ClientWorld, x: Double, y: Double, z: Double) : super(world, x, y, z) {
        this.scale = 1f
    }

    protected constructor(
        world: ClientWorld,
        x: Double, y: Double, z: Double,
        xVel: Double, yVel: Double, zVel: Double
    ) : super(world, x, y, z, xVel, yVel, zVel) {
        this.scale = 1f
    }

    open val particleRotation: BillboardParticle.FacingCameraMode = BillboardParticle.FacingCameraMode.ALL_AXIS


    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        particleRotation.setRotation(quaternionf, camera, tickDelta)
        if (this.angle != 0f) {
            quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle))
        }

        this.lerpPosition(vertexConsumer, camera, quaternionf, tickDelta)
    }

    open fun lerpPosition(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) {
        val camera = camera.pos
        val x = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - camera.x).toFloat()
        val y = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - camera.y).toFloat()
        val z = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - camera.z).toFloat()
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
        val brightness = this.getBrightness(tickDelta)
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
            .xyz(vector3f.x, vector3f.y, vector3f.z)
            .uv0(u, v)
            .color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
            .uv2(brightness)
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
