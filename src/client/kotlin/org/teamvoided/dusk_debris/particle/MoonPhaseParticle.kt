package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils.rotate360

class MoonPhaseParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteSet
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {
    init {
        this.setSpriteFromAge(spriteProvider)
        this.lifetime = 8
        this.quadSize = random.nextFloat() * 0.1f + 0.1f
        this.roll = (random.nextFloat()) * rotate360
        this.oRoll = roll
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

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType?,
            world: ClientLevel,
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