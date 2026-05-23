package org.teamvoided.dusk_debris.particle.cube_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f
import org.teamvoided.dusk_debris.particle.cube_particles.abstracts.SpriteBillboardKotlinParticle
import org.teamvoided.dusk_debris.particle.cube_particles.models.CubeSimple
import org.teamvoided.dusk_debris.particle.cube_particles.models.CubeUnwrapped
import org.teamvoided.dusk_debris.util.Utils

open class CubeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xVel: Double,
    yVel: Double,
    zVel: Double
) : SpriteBillboardKotlinParticle(world, x, y, z, xVel, yVel, zVel) {
    private var cubeShape: CubeSimple? = null
    private var prevRot: Vec3 = Vec3.ZERO
    private var rotation: Vec3 = Vec3.ZERO
    private var rotSpeed: Vec3 = Vec3.ZERO

    init {
        this.lifetime = 100
        rotation = Vec3(
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360
        )
        prevRot = rotation
        rotSpeed = Vec3(
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
            val color = Vector4f(this.rCol, this.gCol, this.bCol, this.alpha)
            val brightness = this.getLightColor(tickDelta)
            val size = this.getSize(tickDelta)
            cubeShape!!.renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, size)
        } else {
            println("set sprite, fool")
            this.remove()
        }
    }

    private fun getRotation(tickDelta: Float): Vector3f {
        val x = Mth.lerp(tickDelta.toDouble(), prevRot.x, rotation.x).toFloat()
        val y = Mth.lerp(tickDelta.toDouble(), prevRot.y, rotation.y).toFloat()
        val z = Mth.lerp(tickDelta.toDouble(), prevRot.z, rotation.z).toFloat()
        return Vector3f(x, y, z)
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        prevRot = rotation
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            rotation = rotation.add(rotSpeed)
            x += xd
            y += yd
            z += zd
            xd * friction
            yd * friction
            zd * friction
        }
    }

    override fun setSprite(spriteProvider: SpriteSet) {
        super.setSprite(spriteProvider)
        createShape()
    }

    override fun setSpriteForAge(spriteProvider: SpriteSet) {
        super.setSpriteForAge(spriteProvider)
        createShape()
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


    @Environment(EnvType.CLIENT)
    open class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            world: ClientLevel,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = CubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}