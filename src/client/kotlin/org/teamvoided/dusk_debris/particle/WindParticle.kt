package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils.rotate360
import kotlin.math.sqrt

class WindParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    distance: Double,
    direction: Int,
    maxAge: Int,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private var direction: Direction = Direction.UP
    private var endPos = Vec3d(0.0, 0.0, 0.0)
    private var rotation = 0f
    private var prevRotation = 0f
    private var pitch = 0f
    private var prevPitch = 0f
    private var rotationOffset = 0.0f
    private var rotationMultiplier = 0.0f

    init {
        this.scale = 0.3f
        this.direction = Direction.byId(direction)
        this.maxAge = maxAge
        this.rotationOffset = MathHelper.nextFloat(random, 0f, rotate360)
        this.rotationMultiplier = world.random.nextFloat() - world.random.nextFloat()

        val vec3d = this.direction.vector
        endPos = Vec3d(vec3d.x * distance + x, vec3d.y * distance + y, vec3d.z * distance + z)
        val d = x - endPos.getX()
        val e = y - endPos.getY()
        val f = z - endPos.getZ()
        this.rotation = MathHelper.atan2(d, f).toFloat()
        this.prevRotation = this.rotation
        this.pitch = MathHelper.atan2(e, sqrt(d * d + f * f)).toFloat()
        this.prevPitch = this.pitch
        this.setSpriteForAge(this.spriteProvider)
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val rotateY =
            (rotationOffset + MathHelper.sin((this.age.toFloat() + tickDelta - 6.2831855f) * 0.05f) * 2.0f * rotationMultiplier)
        val rotation = MathHelper.lerp(tickDelta, this.prevRotation, this.rotation)
        val pitch = MathHelper.lerp(tickDelta, this.prevPitch, this.pitch) + 1.5707964f
        val quaternionf = Quaternionf()
        quaternionf.rotationY(rotation).rotateX(-pitch).rotateY(rotateY)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        quaternionf.rotationY(-3.1415927f + rotation).rotateX(pitch).rotateY(rotateY)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
    }

    override fun getBrightness(tint: Float): Int {
        return 240
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        this.setSpriteForAge(this.spriteProvider)
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            val i: Int = this.maxAge - this.age
            val lerp = 1.0 / i.toDouble()
            this.x = MathHelper.lerp(lerp, this.x, endPos.x)
            this.y = MathHelper.lerp(lerp, this.y, endPos.y)
            this.z = MathHelper.lerp(lerp, this.z, endPos.z)
//            this.setColorAlpha(0.5f - 0.5f * lerp.toFloat())
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<WindParticleEffect> {
        override fun createParticle(
            particleEffect: WindParticleEffect,
            world: ClientWorld,
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
                particleEffect.distance(),
                particleEffect.direction(),
                particleEffect.arrivalTicks(),
                spriteProvider
            )
            windParticle.setColorAlpha(0.5f)
            return windParticle
        }
    }
}