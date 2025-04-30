package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils

class SpellParticle(
    world: ClientWorld,
    xPos: Double, yPos: Double, zPos: Double,
    xVel: Double, yVel: Double, zVel: Double
) : SpriteBillboardParticle(world, xPos, yPos, zPos) {
    private var angleRate: Float

    init {
        prevAngle = random.nextFloat()
        angle = prevAngle
        angleRate = (random.nextFloat() - 0.5f) * Utils.rotate30
        gravityStrength = random.nextFloat() + 0.5f
        val age = 25
        maxAge = age + (random.nextFloat() * (age / 2f)).toInt()
        velocityMultiplier = 0.9f
        velocityX = xVel
        velocityY = yVel
        velocityZ = zVel
        x = xPos
        y = yPos
        z = zPos
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
        val age = this.age + tickDelta
        val scaleProgress = if (age - (maxAge - 20) >= 0) {
            val square = ((-age + maxAge) / 20f) - 1f
            Math.max(-(square * square) + 1, 0f)
        } else if (age <= 5) {
            val square = (age / 5) - 1
            -(square * square) + 1
        } else 1f
        return scaleProgress * scale
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tint: Float): Int = 240

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