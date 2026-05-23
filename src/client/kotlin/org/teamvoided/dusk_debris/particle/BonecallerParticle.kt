package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import org.joml.Vector3f
import org.teamvoided.dusk_debris.particle.color.BonecallerParticleEffect
import java.awt.Color

@Environment(EnvType.CLIENT)
open class BonecallerParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val color1: Color,
    val color2: Color
) :
    TextureSheetParticle(world, x, y, z) {
    init {
        this.xd += ((random.nextFloat() - random.nextFloat()) * 0.2)
        this.yd += (random.nextFloat().toDouble() * 0.75)
        this.zd += ((random.nextFloat() - random.nextFloat()) * 0.2)
        this.gravity = 0f
        chooseColor()
        this.quadSize = random.nextFloat() * 0.3f + 0.3f
        this.lifetime = (random.nextFloat() * 80).toInt() + 60
    }

    private fun chooseColor() {
        val f = (Math.random().toFloat())
        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
        val colorChoice: Vector3f = colorOption1.lerp(colorOption2, f)
        this.rCol = colorChoice.x()
        this.gCol = colorChoice.y()
        this.bCol = colorChoice.z()
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            xd *= 0.8
            yd *= if (this.yd > 0.01) 0.8 else 0.95
            zd *= 0.8
            this.move(this.xd, this.yd, this.zd)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<BonecallerParticleEffect> {
        override fun createParticle(
            type: BonecallerParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = BonecallerParticle(world, posX, posY, posZ, velX, velY, velZ, type.color1, type.color2)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}