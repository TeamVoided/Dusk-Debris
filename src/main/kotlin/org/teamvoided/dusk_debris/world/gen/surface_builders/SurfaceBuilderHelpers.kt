package org.teamvoided.dusk_debris.world.gen.surface_builders

import net.minecraft.world.level.levelgen.synth.NormalNoise

object SurfaceBuilderHelpers {



    fun halfNegative(double: Double): Double {
        return if (double < 0) double / 2 else double
    }

    fun NormalNoise.sample(x: Number, y: Number, z: Number): Double {
        return this.getValue(x.toDouble(), y.toDouble(), z.toDouble())
    }
}