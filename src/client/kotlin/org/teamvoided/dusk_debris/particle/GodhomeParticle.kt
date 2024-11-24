package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import java.awt.Color
import java.util.*

class GodhomeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    color: Color
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        val colorMult = 1//random.nextFloat() * 0.2f + 0.5f
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.gravityStrength = 1f + random.nextFloat() * 0.025f
        this.colorRed = colorMult * color.red / 255f
        this.colorGreen = colorMult * color.green / 255f
        this.colorBlue = colorMult * color.blue / 255f
        this.scale = random.nextFloat() * 0.25f
        this.maxAge = (random.nextDouble() * 45).toInt() + 45
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getSize(tickDelta: Float): Float {
        var value = (age.toFloat() + tickDelta) / maxAge.toFloat()
        value = 1f - 2 * value
        value *= value
        value = 1f - value
        return this.scale * value
    }

    override fun getBrightness(tint: Float): Int {
        return 240
    }

    override fun getGroup(): Optional<ParticleGroup> {
        return Optional.of(GODHOME_PARTICLE_GROUP)
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            velocityX *= 0.975
            velocityY *= gravityStrength
            velocityZ *= 0.975

            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<GodhomeParticleEffect> {
        override fun createParticle(
            type: GodhomeParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GodhomeParticle(world, posX, posY, posZ, velX, velY, velZ, type.color)
            particle.setSprite(spriteProvider)
            return particle
        }
    }

    companion object {
        val GODHOME_PARTICLE_GROUP: ParticleGroup = ParticleGroup(32768)
    }
}