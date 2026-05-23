package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth.lerp
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf

class LazerSourceParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    init {
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.quadSize = random.nextFloat() * 0.25f + 0.25f
        this.lifetime = 10 + random.nextInt(10)
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
        }
    }

    override fun getQuadSize(tickDelta: Float): Float = super.getQuadSize(tickDelta) * mult(tickDelta)

    override fun renderRotatedQuad(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) {
        val cameraPos = camera.position
        val particlePos = Vec3(
            lerp(tickDelta.toDouble(), this.xo, this.x),
            lerp(tickDelta.toDouble(), this.yo, this.y),
            lerp(tickDelta.toDouble(), this.zo, this.z)
        )

        val offsetPos = Vec3(
            particlePos.x - cameraPos.x,
            particlePos.y - cameraPos.y,
            particlePos.z - cameraPos.z
        ).normalize().scale(mult(tickDelta).toDouble())
        val returnPos = Vec3(
            (particlePos.x - (offsetPos.x) - cameraPos.x),
            (particlePos.y - (offsetPos.y) - cameraPos.y),
            (particlePos.z - (offsetPos.z) - cameraPos.z)
        )

        this.renderRotatedQuad(
            vertexConsumer,
            quaternionf,
            returnPos.x.toFloat(),
            returnPos.y.toFloat(),
            returnPos.z.toFloat(),
            tickDelta
        )
    }

    private fun mult(tickDelta: Float): Float {
        val mult = (2f / (lifetime - 1f)) * (age + tickDelta) - 1f
        return 1f - (mult * mult)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_LIT

    override fun getLightColor(tint: Float): Int = 240


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
            val particle = LazerSourceParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}