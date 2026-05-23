package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.ParticleGroup
import org.teamvoided.dusk_debris.particle.color.GodhomeParticleEffect
import java.awt.Color
import java.util.*

class GodhomeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    color: Color
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        val colorMult = 1//random.nextFloat() * 0.2f + 0.5f
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.gravity = 1f + random.nextFloat() * 0.025f
        this.rCol = colorMult * color.red / 255f
        this.gCol = colorMult * color.green / 255f
        this.bCol = colorMult * color.blue / 255f
        this.quadSize = random.nextFloat() * 0.25f
        this.lifetime = (random.nextDouble() * 45).toInt() + 45
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getQuadSize(tickDelta: Float): Float {
        var value = (age.toFloat() + tickDelta) / lifetime.toFloat()
        value = 1f - 2 * value
        value *= value
        value = 1f - value
        return this.quadSize * value
    }

    override fun getLightColor(tint: Float): Int {
        return 240
    }

    override fun getParticleGroup(): Optional<ParticleGroup> {
        return Optional.of(GODHOME_PARTICLE_GROUP)
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            xd *= friction
            yd *= gravity
            zd *= friction

            this.x += this.xd
            this.y += this.yd
            this.z += this.zd
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<GodhomeParticleEffect> {
        override fun createParticle(
            type: GodhomeParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = GodhomeParticle(world, posX, posY, posZ, velX, velY, velZ, type.color)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }

    companion object {
        val GODHOME_PARTICLE_GROUP: ParticleGroup = ParticleGroup(32768)
    }
}