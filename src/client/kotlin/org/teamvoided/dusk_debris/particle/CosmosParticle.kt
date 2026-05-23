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
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.max

class CosmosParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    init {
        val colorChoice = random.nextFloat()
        this.rCol = lerp(colorChoice, colorOption1.x, colorOption2.x)
        this.gCol = lerp(colorChoice, colorOption1.y, colorOption2.y)
        this.bCol = lerp(colorChoice, colorOption1.z, colorOption2.z)
        this.alpha = 0f
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.quadSize = random.nextFloat() * 0.25f + 0.25f
        this.lifetime = 40 + random.nextInt(360)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getLightColor(tint: Float): Int {
        return 240
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            if (this.age >= this.lifetime - 20) {
                setAlpha(max(alpha - 0.05f, 0f))
            } else if (alpha < 1f) {
                setAlpha(alpha + 0.05f)
            }
            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
        }
    }


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
        val age2 = age.toDouble() + tickDelta.toDouble()
        val mult = if (age2 >= lifetime - 20) {
            ((age2 - (lifetime - 20)) / -20.0) - 1
        } else if (age2 <= 20) {
            (age2 / 20.0) - 2
        } else -1.0
        val offsetPos = Vec3(
            particlePos.x - cameraPos.x,
            particlePos.y - cameraPos.y,
            particlePos.z - cameraPos.z
        ).normalize().scale(mult)
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
            val particle = CosmosParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }

    companion object {
        val color1 = Color(0x371699)
        val color2 = Color(0x20153D)
        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
    }
}