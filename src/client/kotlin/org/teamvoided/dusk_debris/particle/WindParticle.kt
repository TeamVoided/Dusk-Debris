package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils.rotate360
import kotlin.math.sqrt

class WindParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    distance: Double,
    private val direction: Direction,
    maxAge: Int,
    private val spriteProvider: SpriteSet
) : TextureSheetParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private var endPos = Vec3(0.0, 0.0, 0.0)
    private var rotation = 0f
    private var prevRotation = 0f
    private var pitch = 0f
    private var prevPitch = 0f
    private var rotationOffset = 0f
    private var rotationMultiplier = 0f

    init {
        this.quadSize = world.random.nextFloat() * 0.2f + 0.1f
        this.lifetime = maxAge
        this.rotationOffset = Mth.nextFloat(random, 0f, rotate360)
        this.rotationMultiplier =
            (world.random.nextFloat() - world.random.nextFloat()) * (distance.toFloat() / this.lifetime)

        val vec3d = this.direction.normal
        endPos = Vec3(vec3d.x * distance + x, vec3d.y * distance + y, vec3d.z * distance + z)
        //point means point towards, relative to current pos
        val xPoint = this.direction.normal.x * -1.0
        val yPoint = this.direction.normal.y * -1.0
        val zPoint = this.direction.normal.z * -1.0
        this.rotation = Mth.atan2(xPoint, zPoint).toFloat()
        this.prevRotation = this.rotation
        this.pitch = Mth.atan2(yPoint, sqrt(xPoint * xPoint + zPoint * zPoint)).toFloat()
        this.prevPitch = this.pitch
        this.setSpriteFromAge(this.spriteProvider)
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
//        val rotateY =
//            (rotationOffset + MathHelper.sin((this.age.toFloat() + tickDelta - 6.2831855f) * 0.05f) * 2.0f * rotationMultiplier)

//        val rotateY = MathHelper.sin((age.toFloat() + tickDelta - 6.2831855f) * 0.05f) * 2.0f

        val rotateY = (Mth.sin((age.toFloat() + tickDelta - 6.2831855f) * rotationMultiplier)) + rotationOffset
        val rotation = Mth.lerp(tickDelta, this.prevRotation, this.rotation)
        val pitch = Mth.lerp(tickDelta, this.prevPitch, this.pitch) + 1.5707964f
        val quaternionf = Quaternionf()
        quaternionf.rotationY(rotation).rotateX(-pitch).rotateY(rotateY)
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
        quaternionf.rotationY(-3.1415927f + rotation).rotateX(pitch).rotateY(rotateY)
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        this.setSpriteFromAge(this.spriteProvider)
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            val i: Int = this.lifetime - this.age
            val lerp = 1.0 / i.toDouble()
            this.x = Mth.lerp(lerp, this.x, endPos.x) + (random.nextDouble() - random.nextDouble()) / this.lifetime
            this.y = Mth.lerp(lerp, this.y, endPos.y) + (random.nextDouble() - random.nextDouble()) / this.lifetime
            this.z = Mth.lerp(lerp, this.z, endPos.z) + (random.nextDouble() - random.nextDouble()) / this.lifetime

            val xPoint = this.direction.normal.x * -1.0
            val yPoint = this.direction.normal.y * -1.0
            val zPoint = this.direction.normal.z * -1.0
            this.prevRotation = this.rotation
            this.rotation = Mth.atan2(xPoint, zPoint).toFloat()
            this.prevPitch = this.pitch
            this.pitch = Mth.atan2(yPoint, sqrt(xPoint * xPoint + zPoint * zPoint)).toFloat()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<WindParticleEffect> {
        override fun createParticle(
            particleEffect: WindParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            val windParticle = WindParticle(
                world,
                posX,
                posY,
                posZ,
                particleEffect.distance,
                particleEffect.direction,
                particleEffect.arrivalTicks,
                spriteProvider
            )
            windParticle.setAlpha(world.random.nextFloat() * 0.25f + 0.5f)
            return windParticle
        }
    }
}