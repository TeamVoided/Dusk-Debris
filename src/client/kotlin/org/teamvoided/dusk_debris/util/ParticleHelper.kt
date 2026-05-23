package org.teamvoided.dusk_debris.util

import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.util.RandomSource
import org.joml.Vector3f
import java.awt.Color

object ParticleHelper {
    val NONE: SingleQuadParticle.FacingCameraMode = SingleQuadParticle.FacingCameraMode { _, _, _ -> }



    fun chooseColor(color1:Color,color2:Color, random: RandomSource): Vector3f {
        val f = (random.nextFloat())
        val colorOption1 = Vector3f(color1.red / 255f, color1.green / 255f, color1.blue / 255f)
        val colorOption2 = Vector3f(color2.red / 255f, color2.green / 255f, color2.blue / 255f)
        return colorOption1.lerp(colorOption2, f)
    }
}