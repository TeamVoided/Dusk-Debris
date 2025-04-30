package org.teamvoided.dusk_debris.particle.stupid_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelCuboidData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.particle.BillboardParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class DiveParticle : Particle {
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


    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        if (this.angle != 0.0f) {
            quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle))
        }

        this.drawPlaneLerping(vertexConsumer, camera, quaternionf, tickDelta)
    }

    fun drawPlaneLerping(vertexConsumer: VertexConsumer, camera: Camera, quaternionf: Quaternionf, tickDelta: Float) {
        val vec3d = camera.pos
        val x = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - vec3d.getX()).toFloat()
        val y = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - vec3d.getY()).toFloat()
        val z = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - vec3d.getZ()).toFloat()
        this.drawPlane(vertexConsumer, quaternionf, x, y, z, tickDelta)
    }

    fun drawPlane(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        val size = this.getSize(tickDelta)
        val minU = this.minU
        val maxU = this.maxU
        val minV = this.minV
        val maxV = this.maxV
        val brightness = this.getBrightness(tickDelta)
        this.corner(vertexConsumer, quaternionf, x, y, z,  1f, -1f, size, maxU, maxV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z,  1f,  1f, size, maxU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f,  1f, size, minU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f, -1f, size, minU, maxV, brightness)
    }

    fun corner(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        textureVer: Float,
        textureHor: Float,
        size: Float,
        u: Float,
        v: Float,
        brightness: Int
    ) {
        val vector3f = Vector3f(textureVer, textureHor, 0.0f).rotate(quaternionf).mul(size).add(x, y, z)
        vertexConsumer
            .xyz(vector3f.x(), vector3f.y(), vector3f.z())
            .uv0(u, v)
            .color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
            .uv2(brightness)
    }

    fun getSize(tickDelta: Float): Float =this.scale

    override fun scale(scale: Float): Particle {
        this.scale *= scale
        return super.scale(scale)
    }

    protected abstract val minU: Float
    protected abstract val maxU: Float
    protected abstract val minV: Float
    protected abstract val maxV: Float
}
