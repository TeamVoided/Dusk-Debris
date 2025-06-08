package org.teamvoided.dusk_debris.particle

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
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.PI
import kotlin.math.sqrt

class RisingEmberParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {
    var prevVelocity: Vec3d
    var nextVelocity: Vec3d
    var ageNewOld: Pair<Int, Int>

    init {
        this.maxAge = 12 + random.nextInt(8)
        this.scale = random.nextFloat() * 0.7f + 0.3f
        this.prevVelocity = randomVelocity()
        this.nextVelocity = randomVelocity()
        this.ageNewOld = 0 to random.nextInt(15)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_LIT

    public override fun getBrightness(tint: Float): Int = 15728880

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            val vel = getVelocity()
            this.x += vel.x
            this.y += vel.y
            this.z += vel.z
            if (ageNewOld.second < age) {
                this.prevVelocity = this.nextVelocity
                this.nextVelocity = randomVelocity()
                ageNewOld = age to age + random.nextInt(15)
            }
        }
    }

    private fun randomVelocity(): Vec3d {
        val y = random.nextDouble() + 0.2
        val x = random.nextDouble() - 0.2
        val z = random.nextDouble() - 0.2
        return Vec3d(x, y, z).normalize()
    }

    private fun getVelocity(tickDelta: Float = 0f): Vec3d {
        val delta = (age - ageNewOld.first + tickDelta) / (ageNewOld.second - ageNewOld.first)
        return prevVelocity.lerp(nextVelocity, delta.toDouble())
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
//        val rotateY =
//            (rotationOffset + MathHelper.sin((this.age.toFloat() + tickDelta - 6.2831855f) * 0.05f) * 2.0f * rotationMultiplier)

//        val rotateY = MathHelper.sin((age.toFloat() + tickDelta - 6.2831855f) * 0.05f) * 2.0f
        val vel = getVelocity(tickDelta)
        val rot = MathHelper.atan2(vel.x, vel.z).toFloat()
        val pit = MathHelper.atan2(vel.y, sqrt(vel.x * vel.x + vel.z * vel.z)).toFloat()

        val rotateY = 0f //(MathHelper.sin((age.toFloat() + tickDelta)))
        val quaternionf = Quaternionf()
        quaternionf.rotationY(rot).rotateX(-pit).rotateY(rotateY)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        quaternionf.rotationY(rot - PI).rotateX(pit).rotateY(rotateY)
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
    }

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
            val particle = RisingEmberParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}