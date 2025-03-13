package org.teamvoided.dusk_debris.world.gen.surface_builders

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler

object SurfaceBuilderHelpers {



    fun halfNegative(double: Double): Double {
        return if (double < 0) double / 2 else double
    }

    fun DoublePerlinNoiseSampler.sample(x: Number, y: Number, z: Number): Double {
        return this.sample(x.toDouble(), y.toDouble(), z.toDouble())
    }
}