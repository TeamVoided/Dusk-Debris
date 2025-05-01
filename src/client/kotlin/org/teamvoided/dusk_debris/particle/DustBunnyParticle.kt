package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper.lerp
import org.joml.Vector3f
import org.teamvoided.dusk_debris.particle.color.DustBunnyParticleEffect
import java.awt.Color

class DustBunnyParticle(
    world: ClientWorld,
    x: Double, y: Double, z: Double,
    velX: Double, velY: Double, velZ: Double,
    val color1: Color,
    val color2: Color
) : SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {
    init {
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        this.scale = (this.random.nextFloat() * this.random.nextFloat() * 0.5f + 0.25f)
        this.maxAge = (this.random.nextFloat() * 80).toInt() + 20
        this.gravityStrength = -0.01f
        this.collidesWithWorld = false
        this.velocityMultiplier = 1.0f
        val lerp = random.nextFloat()
        colorRed = lerp(colorToVector(color1).x, colorToVector(color2).x, lerp)
        colorGreen = lerp(colorToVector(color1).y, colorToVector(color2).y, lerp)
        colorBlue = lerp(colorToVector(color1).z, colorToVector(color2).z, lerp)
        this.colorAlpha = random.nextFloat() * 0.6f + 0.4f
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        super.tick()
        this.velocityX *= 0.9
        this.velocityY *= 0.9
        this.velocityZ *= 0.9
    }

    override fun getBrightness(tint: Float): Int {
        val blockPos = BlockPos.create(this.x, this.y, this.z)
        var brightness = 200
        if (world.isChunkLoaded(blockPos)) {
            val brightness2 = WorldRenderer.getLightmapCoordinates(this.world, blockPos)
            brightness = Math.max(brightness, brightness2)
        }
        return brightness
    }

    fun colorToVector(color: Color): Vector3f {
        return Vector3f(color.red / 255f, color.green / 255f, color.blue / 255f)
    }


    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DustBunnyParticleEffect> {
        override fun createParticle(
            type: DustBunnyParticleEffect,
            world: ClientWorld,
            xPos: Double,
            yPos: Double,
            zPos: Double,
            xVel: Double,
            yVel: Double,
            zVel: Double
        ): Particle {
            val particle = DustBunnyParticle(world, xPos, yPos, zPos, xVel, yVel, zVel, type.color1, type.color2)
            particle.setSprite(this.spriteProvider)
            return particle
        }
    }
}