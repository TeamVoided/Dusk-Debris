package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.PI
import java.awt.Color
import kotlin.math.sqrt

class RisingEmberParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {
    var prevVelocity: Vec3
    var nextVelocity: Vec3
    var ageNewOld: Pair<Int, Int>

    init {
        this.lifetime = 120 + random.nextInt(8)
        this.quadSize = 0.025f
        this.prevVelocity = Vec3(velX, velY, velZ)
        this.nextVelocity = randomVelocity()
        this.ageNewOld = 0 to random.nextInt(10) + 10

        val color = Color(0xFF2244) //0xFFB922 //0xFF8522 //0xFF2244 //0x5D091B
        this.rCol = color.red / 255f
        this.gCol = color.green / 255f
        this.bCol = color.blue / 255f
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_LIT

    override fun getLightColor(tickDelta: Float): Int = 255

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            if (ageNewOld.second < age) {
                this.prevVelocity = this.nextVelocity
                this.nextVelocity = randomVelocity()
                ageNewOld = age to age + random.nextInt(10) + 10
            }
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            val vel = getVelocity()
            this.x += vel.x
            this.y += vel.y
            this.z += vel.z
        }
    }

    private fun randomVelocity(): Vec3 {
        val y = random.nextDouble()
        val x = random.nextDouble() - 0.2
        val z = random.nextDouble() - 0.2
        return Vec3(x, y, z).normalize().scale(0.4)
    }

    private fun getVelocity(tickDelta: Float = 0f): Vec3 {
        val delta = (age - ageNewOld.first + tickDelta) / (ageNewOld.second - ageNewOld.first)
        return prevVelocity.lerp(nextVelocity, delta.toDouble())
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val vel = getVelocity(tickDelta)
        val rol = Mth.atan2(vel.x, vel.z).toFloat()
        val pit = Mth.atan2(vel.y, sqrt(vel.x * vel.x + vel.z * vel.z)).toFloat() - Utils.rotate90
        val yaw = (age + tickDelta) / 3

        val quaternionf = Quaternionf()
        quaternionf.rotationY(rol).rotateX(-pit).rotateY(yaw)
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
        quaternionf.rotationY(rol - PI).rotateX(pit).rotateY(yaw)
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = RisingEmberParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}