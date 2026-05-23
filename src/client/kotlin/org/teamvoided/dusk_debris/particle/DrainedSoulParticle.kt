package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.PI

class DrainedSoulParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteSet
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {
    val spinSpeed: Float

    init {
        this.setSpriteFromAge(spriteProvider)
        this.lifetime = 12 + random.nextInt(8)
        this.quadSize = random.nextFloat() * 0.667f + 0.333f
        this.roll = (random.nextFloat()) * Utils.rotate360
        this.oRoll = roll
        this.spinSpeed = (random.nextFloat() - 0.5f) * 0.2f * PI
        this.friction = 0.9f
        this.xd = velX
        this.yd = velY
        this.zd = velZ
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    public override fun getLightColor(tint: Float): Int {
        return 15728880
    }

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.oRoll = roll
            this.roll += spinSpeed()
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            xd *= friction
            yd *= friction
            zd *= friction
            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
            this.setSpriteFromAge(this.spriteProvider)
        }
    }

    private fun spinSpeed(): Float {
        var value = age / lifetime.toFloat()
        value = 1f - 2 * value
        value *= value
        value = 1f - value
        return this.spinSpeed * value
    }

    @Environment(EnvType.CLIENT)
    class SmallFactory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle: Particle = DrainedSoulParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.scale(0.15f)
            return particle
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle: Particle = DrainedSoulParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.scale(0.5f)
            return particle
        }
    }
}