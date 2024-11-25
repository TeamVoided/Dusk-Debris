package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils.pi
import java.awt.Color
import java.util.*
import kotlin.math.max

class CosmosParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    init {
        val colorChoice = random.nextFloat()
        this.colorRed = lerp(colorChoice, colorOption1.x, colorOption2.x)
        this.colorGreen = lerp(colorChoice, colorOption1.y, colorOption2.y)
        this.colorBlue = lerp(colorChoice, colorOption1.z, colorOption2.z)
        this.colorAlpha = 0f
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.scale = random.nextFloat() * 0.25f + 0.25f
        this.maxAge = 40 + random.nextInt(360)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getBrightness(tint: Float): Int {
        return 240
    }

    override fun getGroup(): Optional<ParticleGroup> {
        return Optional.of(GodhomeParticle.GODHOME_PARTICLE_GROUP)
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            if (this.age >= this.maxAge - 20) {
                setColorAlpha(max(colorAlpha - 0.05f, 0f))
            } else if (colorAlpha < 1f) {
                setColorAlpha(colorAlpha + 0.05f)
            }
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
    }


    override fun method_60373(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) {
        val cameraPos = camera.pos
        val particlePos = Vec3d(
            lerp(tickDelta.toDouble(), this.prevPosX, this.x),
            lerp(tickDelta.toDouble(), this.prevPosY, this.y),
            lerp(tickDelta.toDouble(), this.prevPosZ, this.z)
        )
        val age2 = age.toDouble() + tickDelta.toDouble()
        val mult = if (age2 >= maxAge - 20) {
            ((age2 - (maxAge - 20)) / -20.0) - 1
        } else if (age2 <= 20) {
            (age2 / 20.0) - 2
        } else -1.0
        val offsetPos = Vec3d(
            particlePos.x - cameraPos.x,
            particlePos.y - cameraPos.y,
            particlePos.z - cameraPos.z
        ).normalize().multiply(mult)
        val returnPos = Vec3d(
            (particlePos.x - (offsetPos.x) - cameraPos.x),
            (particlePos.y - (offsetPos.y) - cameraPos.y),
            (particlePos.z - (offsetPos.z) - cameraPos.z)
        )

        this.method_60374(
            vertexConsumer,
            quaternionf,
            returnPos.x.toFloat(),
            returnPos.y.toFloat(),
            returnPos.z.toFloat(),
            tickDelta
        )
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
            val particle = CosmosParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.setSprite(spriteProvider)
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