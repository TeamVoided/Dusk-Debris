package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils
import java.awt.Color

class SpellParticle(
    world: ClientWorld,
    xPos: Double, yPos: Double, zPos: Double,
    xVel: Double, yVel: Double, zVel: Double
) : SpriteBillboardParticle(world, xPos, yPos, zPos) {
    private var angleRate: Float

    init {
        val color = Color(0xF9EEF1)
        this.colorRed = color.red / 255f
        this.colorGreen = color.green / 255f
        this.colorBlue = color.blue / 255f

        gravityStrength = random.nextFloat() + 0.5f
        velocityMultiplier = 0.9f
        velocityX = xVel
        velocityY = yVel
        velocityZ = zVel
        x = xPos
        y = yPos
        z = zPos

        prevAngle = random.nextFloat()
        angle = prevAngle
        angleRate = (random.nextFloat() - 0.5f) * Utils.rotate30
        maxAge = MIN_AGE + (random.nextFloat() * MIN_AGE).toInt()
    }

    override fun tick() {
        prevPosX = x
        prevPosY = y
        prevPosZ = z
        if (age++ >= maxAge) {
            markDead()
        } else {
            prevAngle = angle
            angle += angleRate
            y += gravityStrength * 0.02
            x += velocityX //* 0.05
            y += velocityY //* 0.05
            z += velocityZ //* 0.05
            velocityX *= velocityMultiplier
            velocityY *= velocityMultiplier
            velocityZ *= velocityMultiplier
            angleRate *= 0.99f
        }
    }

    override fun getSize(tickDelta: Float): Float {
        val age: Float = this.age + tickDelta
        val scaleProgress = if (age - (maxAge - THRESHOLD) >= 0) {
            val square = ((-age + maxAge + 1f) / (THRESHOLD + 1)) - 1f
            -(square * square) + 1f
        } else if (age <= START) {
            val square = (age / START) - 1
            -(square * square) + 1f
        } else 1f
        return scaleProgress * scale
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tickDelta: Float): Int = 240

    companion object {
        const val THRESHOLD: Int = 20
        const val START: Int = 5
        const val MIN_AGE: Int = THRESHOLD + START
    }

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType, world: ClientWorld,
            xPos: Double, yPos: Double, zPos: Double,
            xVel: Double, yVel: Double, zVel: Double
        ): Particle {
            val particle = SpellParticle(world, xPos, yPos, zPos, xVel, yVel, zVel)
            particle.setSprite(this.spriteProvider)
            return particle
        }
    }
}