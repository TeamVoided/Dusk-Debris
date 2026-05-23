package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import org.teamvoided.dusk_debris.particle.color.GunpowderExplosionSmokeParticleEffect
import java.awt.Color

@Environment(EnvType.CLIENT)
open class GunpowderExplosionSmokeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    val color: Color
) :
    TextureSheetParticle(world, x, y, z) {

    init {
        this.yd += Math.random() * 0.05
        this.xd += (Math.random() - Math.random()) / 3
        this.zd += (Math.random() - Math.random()) / 3
        this.gravity = 0f
        this.rCol = color.red / 255f
        this.gCol = color.green / 255f
        this.bCol = color.blue / 255f
        this.quadSize = 0.33f * (random.nextFloat() * random.nextFloat() * 6.0f + 1.0f)
        this.lifetime = ((random.nextFloat() * 80).toInt() + 60)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    override fun tick() {
        val multiplier = -((this.age / this.lifetime.toFloat()) * (this.age / this.lifetime.toFloat())) * 0.8f + 1f
        if (this.rCol > 0.2) this.rCol *= multiplier
        if (this.gCol > 0.2) this.gCol *= multiplier
        if (this.bCol > 0.2) this.bCol *= multiplier
        this.alpha *=
            -((this.age / this.lifetime.toFloat()) * (this.age / this.lifetime.toFloat() * (this.age / this.lifetime.toFloat()))) + 1f
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.xd *= 0.75
            this.yd *= 0.95
            this.zd *= 0.75
            this.move(this.xd, this.yd, this.zd)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<GunpowderExplosionSmokeParticleEffect> {
        override fun createParticle(
            type: GunpowderExplosionSmokeParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GunpowderExplosionSmokeParticle(world, posX, posY, posZ, type.color)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}