package org.teamvoided.dusk_debris.init.worldgen

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.density_functions.*

object DuskDensityFunction {

    val FOLD = register("fold", Fold.CODEC)
    val NOISE_RANGE = register("noise_range", NoiseRange.CODEC)
    val SHIFTED_NOISE_RANGE = register("shifted_noise_range", ShiftedNoiseRange.CODEC)
//    val MIN_RANGE_CHOICE = register("min_range_choice", MinRangeChoice.CODEC)

    fun init() {}
    private fun <C : DensityFunction, F : CodecHolder<C>> register(id: String, densityFunction: F): MapCodec<C> =
        Registry.register(Registries.DENSITY_FUNCTION, id(id), densityFunction.codec())
}