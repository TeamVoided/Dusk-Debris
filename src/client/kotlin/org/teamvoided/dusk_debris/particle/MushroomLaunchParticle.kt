package org.teamvoided.dusk_debris.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth.lerp
import org.joml.Vector3f
import java.awt.Color

class MushroomLaunchParticle(
    world: ClientLevel,
    x: Double, y: Double, z: Double,
    velX: Double, velY: Double, velZ: Double
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
        rCol = lerp(colorOption1.x, colorOption2.x, lerp)
        gCol = lerp(colorOption1.y, colorOption2.y, lerp)
        bCol = lerp(colorOption1.z, colorOption2.z, lerp)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE

    override fun tick() {
        super.tick()
        this.xd *= 0.8
        this.yd *= 0.8
        this.zd *= 0.8
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

    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
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
            mushroomLaunchParticle.pickSprite(spriteProvider)
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