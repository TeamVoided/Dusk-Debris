package org.teamvoided.dusk_debris.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils
import java.awt.Color

class SpellParticle(
    world: ClientLevel,
    xPos: Double, yPos: Double, zPos: Double,
    xVel: Double, yVel: Double, zVel: Double
) : TextureSheetParticle(world, xPos, yPos, zPos) {
    private var angleRate: Float

    init {
        val color = Color(0xF9EEF1)
        this.rCol = color.red / 255f
        this.gCol = color.green / 255f
        this.bCol = color.blue / 255f

        gravity = random.nextFloat() + 0.5f
        friction = 0.9f
        xd = xVel
        yd = yVel
        zd = zVel
        x = xPos
        y = yPos
        z = zPos

        oRoll = random.nextFloat()
        roll = oRoll
        angleRate = (random.nextFloat() - 0.5f) * Utils.rotate30
        lifetime = MIN_AGE + (random.nextFloat() * MIN_AGE).toInt()
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
        } else {
            oRoll = roll
            roll += angleRate
            y += gravity * 0.02
            x += xd //* 0.05
            y += yd //* 0.05
            z += zd //* 0.05
            xd *= friction
            yd *= friction
            zd *= friction
            angleRate *= 0.99f
        }
    }

    override fun getQuadSize(tickDelta: Float): Float {
        val age: Float = this.age + tickDelta
        val scaleProgress = if (age - (lifetime - THRESHOLD) >= 0) {
            val square = ((-age + lifetime + 1f) / (THRESHOLD + 1)) - 1f
            -(square * square) + 1f
        } else if (age <= START) {
            val square = (age / START) - 1
            -(square * square) + 1f
        } else 1f
        return scaleProgress * quadSize
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    public override fun getLightColor(tickDelta: Float): Int = 240

    companion object {
        const val THRESHOLD: Int = 20
        const val START: Int = 5
        const val MIN_AGE: Int = THRESHOLD + START
    }

    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType, world: ClientLevel,
            xPos: Double, yPos: Double, zPos: Double,
            xVel: Double, yVel: Double, zVel: Double
        ): Particle {
            val particle = SpellParticle(world, xPos, yPos, zPos, xVel, yVel, zVel)
            particle.pickSprite(this.spriteProvider)
            return particle
        }
    }
}