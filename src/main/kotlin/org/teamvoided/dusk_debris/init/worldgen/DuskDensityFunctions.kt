package org.teamvoided.dusk_debris.init.worldgen

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.carver.Carver
import net.minecraft.world.gen.carver.CarverConfig
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.density_functions.Fold
import org.teamvoided.dusk_debris.world.gen.density_functions.ShiftedNoiseRange

object DuskDensityFunctions {

    val SHIFTED_NOISE_RANGE = register("shifted_noise_range", ShiftedNoiseRange.CODEC)
    val FOLD = register("fold", Fold.CODEC)

    fun init() {}
    private fun <C : DensityFunction, F : CodecHolder<C>> register(id: String, densityFunction: F): MapCodec<C> =
        Registry.register(Registries.DENSITY_FUNCTION, id(id), densityFunction.codec())
}