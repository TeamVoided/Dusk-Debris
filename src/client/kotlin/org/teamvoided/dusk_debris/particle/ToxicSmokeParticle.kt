package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import org.teamvoided.dusk_debris.particle.color.NethershroomSporeParticleEffect
import java.awt.Color

@Environment(EnvType.CLIENT)
open class ToxicSmokeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val color: Color
) : TextureSheetParticle(world, x, y, z) {

    init {
        this.xd = (random.nextFloat() - random.nextFloat()) * 0.05
        this.yd = (random.nextFloat() - random.nextFloat()) * 0.05
        this.zd = (random.nextFloat() - random.nextFloat()) * 0.05
        this.gravity = 0f
        this.rCol = (color.red / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
        this.gCol = (color.green / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
        this.bCol = (color.blue / 255f) + (random.nextFloat() - random.nextFloat()) * 0.05f
//        this.colorAlpha = random.nextFloat() * 0.5f + 0.5f
        this.quadSize = 1.0f
//        this.maxAge = 80
        this.lifetime = ((random.nextFloat() * 80).toInt() + 60)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.xd *= 0.975
            this.yd *= 0.975
            this.zd *= 0.975
            this.move(this.xd, this.yd, this.zd)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<NethershroomSporeParticleEffect> {
        override fun createParticle(
            type: NethershroomSporeParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = ToxicSmokeParticle(world, posX, posY, posZ, velX, velY, velZ, type.color)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}