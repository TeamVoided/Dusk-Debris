package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.util.Utils.rotate90

class MoonPhaseParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {
    init {
        this.setSpriteForAge(spriteProvider)
        this.maxAge = 8
        this.scale = random.nextFloat() * 0.1f + 0.1f
        this.angle = (random.nextFloat()) * rotate360
        this.prevAngle = angle
        this.velocityMultiplier = 0.9f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT
    }

    public override fun getBrightness(tint: Float): Int {
        return 15728880
    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            velocityX *= velocityMultiplier
            velocityY *= velocityMultiplier
            velocityZ *= velocityMultiplier
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
            this.setSpriteForAge(this.spriteProvider)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType?,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            return DrainedSoulParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
        }
    }
}