package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import org.joml.Vector3f
import org.teamvoided.dusk_debris.particle.color.SpiralParticleEffect
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

@Environment(EnvType.CLIENT)
class SpiralParticle internal constructor(
    world: ClientLevel,
    xPos: Double, yPos: Double, zPos: Double,
    xVel: Double, yVel: Double, zVel: Double,
    val color1: Color, val color2: Color
) : TextureSheetParticle(world, xPos, yPos, zPos) {
    val scaleBase: Float
    val offsetterXZ: Float
    val yVelocity2: Double

    init {
        xd = xVel
        yd = yVel
        zd = zVel
        yVelocity2 = 0.0001 + (random.nextFloat() - random.nextFloat()) * 0.0000075
        x = xPos
        y = yPos
        z = zPos
        rCol = 1f
        gCol = 1f
        bCol = 1f
        alpha = 0.01f
        scaleBase = (random.nextFloat() * 0.2f + 0.1f)
        offsetterXZ = (random.nextFloat()) * 0.005f
        quadSize = scaleBase
        oRoll = random.nextFloat()
        roll = oRoll
        hasPhysics = false
        val age2 = 2500
        lifetime = age2 + (random.nextFloat() * (age2 / 3f)).toInt()
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            alpha += -0.01f
            if (alpha < 0)
                remove()
        } else if (alpha < 1) {
            alpha += 0.002f
        }
        val frac = (age.toFloat() / lifetime)
        val speed = 50.0
        val strength = (frac + offsetterXZ) / speed
        val amplitude = age / (speed * 2.5)
        val width = 3
        //val fhte = age / (speed * 1.5)
        //val width = (fhte / 2) * 0.4
        //velocityY = sin(fhte * 0.35) this i think did something cool?
        x += (cos(amplitude) * width * strength) * xd
        y += (sin(amplitude * 3.5) * 0.5 * strength + yVelocity2 * speed) * yd
        z += (sin(amplitude) * width * strength) * zd
        quadSize += 0.0001f
        colorLerp(frac)
    }

    fun colorLerp(frac: Float) {
//        val color2 = Color(0x16E5E5)
//        val color3 = Color(0x16E57E)
        val colorOption1 = Vector3f(1f, 1f, 1f)
        val colorOption2 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption3 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
        val colorChoice: Vector3f =
            if (frac < 0.2f) colorOption1.lerp(colorOption2, frac * 5f)
            else if (frac < 0.4f) colorOption2
            else if (frac < 0.8f) colorOption2.lerp(colorOption3, 2.5f * (frac - 0.4f))
        else colorOption3
        rCol = colorChoice.x()
        gCol = colorChoice.y()
        bCol = colorChoice.z()
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun move(dx: Double, dy: Double, dz: Double) {}

    public override fun getLightColor(tint: Float): Int {
        return 240
    }

//    fun colorLerp(frac: Float) {
//        val color1 = Color(0xD11950)
//        val color2 = Color(0xCA3E84)
//        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
//        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
//        val colorChoice: Vector3f = colorOption1.lerp(colorOption2, frac)
//        colorRed = colorChoice.x()
//        colorGreen = colorChoice.y()
//        colorBlue = colorChoice.z()
//    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SpiralParticleEffect> {
        override fun createParticle(
            type: SpiralParticleEffect, world: ClientLevel,
            xPos: Double, yPos: Double, zPos: Double,
            xVel: Double, yVel: Double, zVel: Double
        ): Particle {
            val particle = SpiralParticle(world, xPos, yPos, zPos, xVel, yVel, zVel, type.color1, type.color2)
            particle.pickSprite(this.spriteProvider)
            return particle
        }
    }
}