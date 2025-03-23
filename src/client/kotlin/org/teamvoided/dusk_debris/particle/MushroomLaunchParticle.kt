package org.teamvoided.dusk_debris.particle

import net.minecraft.client.particle.*
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper.lerp
import org.joml.Vector3f
import java.awt.Color

class MushroomLaunchParticle(
    world: ClientWorld,
    x: Double, y: Double, z: Double,
    velX: Double, velY: Double, velZ: Double
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
        colorRed = lerp(colorOption1.x, colorOption2.x, lerp)
        colorGreen = lerp(colorOption1.y, colorOption2.y, lerp)
        colorBlue = lerp(colorOption1.z, colorOption2.z, lerp)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE

    override fun tick() {
        super.tick()
        this.velocityX *= 0.8
        this.velocityY *= 0.8
        this.velocityZ *= 0.8
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

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velX: Double,
            velY: Double,
            velZ: Double
        ): Particle {
            val mushroomLaunchParticle = MushroomLaunchParticle(
                world,
                x, y, z,
                velX, velY, velZ
            )
            mushroomLaunchParticle.setSprite(spriteProvider)
            return mushroomLaunchParticle
        }
    }

    companion object {
        val color1 = Color(0x8C1DE3)
        val color2 = Color(0x3B0E7E)
        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
    }
}