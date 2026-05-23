package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth.lerp
import org.joml.Vector3f
import org.teamvoided.dusk_debris.particle.color.DustBunnyParticleEffect
import java.awt.Color

class DustBunnyParticle(
    world: ClientLevel,
    x: Double, y: Double, z: Double,
    velX: Double, velY: Double, velZ: Double,
    val color1: Color,
    val color2: Color
) : TextureSheetParticle(world, x, y, z, velX, velY, velZ) {
    init {
        this.xd = velX
        this.yd = velY
        this.zd = velZ
        this.quadSize = (this.random.nextFloat() * this.random.nextFloat() * 0.5f + 0.25f)
        this.lifetime = (this.random.nextFloat() * 80).toInt() + 20
        this.gravity = -0.01f
        this.hasPhysics = false
        this.friction = 1.0f
        val lerp = random.nextFloat()
        rCol = lerp(colorToVector(color1).x, colorToVector(color2).x, lerp)
        gCol = lerp(colorToVector(color1).y, colorToVector(color2).y, lerp)
        bCol = lerp(colorToVector(color1).z, colorToVector(color2).z, lerp)
        this.alpha = random.nextFloat() * 0.6f + 0.4f
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        super.tick()
        this.xd *= 0.9
        this.yd *= 0.9
        this.zd *= 0.9
    }

    override fun getLightColor(tint: Float): Int {
        val blockPos = BlockPos.containing(this.x, this.y, this.z)
        var brightness = 200
        if (level.hasChunkAt(blockPos)) {
            val brightness2 = LevelRenderer.getLightColor(this.level, blockPos)
            brightness = Math.max(brightness, brightness2)
        }
        return brightness
    }

    fun colorToVector(color: Color): Vector3f {
        return Vector3f(color.red / 255f, color.green / 255f, color.blue / 255f)
    }


    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<DustBunnyParticleEffect> {
        override fun createParticle(
            type: DustBunnyParticleEffect,
            world: ClientLevel,
            xPos: Double,
            yPos: Double,
            zPos: Double,
            xVel: Double,
            yVel: Double,
            zVel: Double
        ): Particle {
            val particle = DustBunnyParticle(world, xPos, yPos, zPos, xVel, yVel, zVel, type.color1, type.color2)
            particle.pickSprite(this.spriteProvider)
            return particle
        }
    }
}