package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

@Environment(EnvType.CLIENT)
open class BonecallerParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val color1: Color,
    val color2: Color
) :
    SpriteBillboardParticle(world, x, y, z) {
    init {
        this.velocityX += ((random.nextFloat() - random.nextFloat()) * 0.2)
        this.velocityY += (random.nextFloat().toDouble() * 0.75)
        this.velocityZ += ((random.nextFloat() - random.nextFloat()) * 0.2)
        this.gravityStrength = 0f
        chooseColor()
        this.scale = random.nextFloat() * 0.3f + 0.3f
        this.maxAge = (random.nextFloat() * 80).toInt() + 60
    }

    private fun chooseColor() {
        val f = (Math.random().toFloat())
        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
        val colorChoice: Vector3f = colorOption1.lerp(colorOption2, f)
        this.colorRed = colorChoice.x()
        this.colorGreen = colorChoice.y()
        this.colorBlue = colorChoice.z()
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            velocityX *= 0.8
            velocityY *= if (this.velocityY > 0.01) 0.8 else 0.95
            velocityZ *= 0.8
            this.move(this.velocityX, this.velocityY, this.velocityZ)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<BonecallerParticleEffect> {
        override fun createParticle(
            type: BonecallerParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = BonecallerParticle(world, posX, posY, posZ, velX, velY, velZ, type.color1, type.color2)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}