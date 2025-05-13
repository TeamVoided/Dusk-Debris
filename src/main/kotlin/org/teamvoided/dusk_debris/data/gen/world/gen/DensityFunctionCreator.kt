package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.NoiseHolder
import net.minecraft.world.gen.DensityFunctions.*
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.NetherDensityFunctionCreator.theNetherCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.overworldCreator
import org.teamvoided.dusk_debris.world.gen.density_functions.SingleDensityFunctionInput

object DensityFunctionCreator {

    fun bootstrap(c: BootstrapContext<DensityFunction>) {
        c.overworldCreator()
        c.theNetherCreator()
    }

//    NoiseRouterData.class


    fun BootstrapContext<*>.noise(noi: RegistryKey<NoiseParameters>): NoiseHolder =
        NoiseHolder(this.noiseHold(noi))

    fun BootstrapContext<*>.noiseHold(noi: RegistryKey<NoiseParameters>): Holder.Reference<NoiseParameters> =
        this.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS).getHolderOrThrow(noi)

    fun BootstrapContext<*>.dense(den: RegistryKey<DensityFunction>): DensityFunction =
        HolderHolder(this.denseHold(den))

    fun BootstrapContext<*>.denseHold(noi: RegistryKey<DensityFunction>): Holder.Reference<DensityFunction> =
        this.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION).getHolderOrThrow(noi)


    fun BootstrapContext<DensityFunction>.registerAndWrap(
        registryKey: RegistryKey<DensityFunction>,
        den: DensityFunction
    ): DensityFunction {
        return HolderHolder(this.register(registryKey, den))
    }

    fun maxRangeChoice(input: DensityFunction, maxInclusive: Double): DensityFunction {
        val minInclusive = -1000000.0
        return rangeChoice(input, minInclusive, maxInclusive, constant(minInclusive), input)
    }


    fun minRangeChoice(input: DensityFunction, minInclusive: Double): DensityFunction {
        val maxInclusive = 1000000.0
        return rangeChoice(input, minInclusive, maxInclusive, constant(maxInclusive), input)
    }

    fun round(input: DensityFunction): DensityFunction {
        return SingleDensityFunctionInput(SingleDensityFunctionInput.Type.ROUND, input)
    }

    fun floor(input: DensityFunction): DensityFunction {
        return SingleDensityFunctionInput(SingleDensityFunctionInput.Type.FLOOR, input)
    }

    fun ceil(input: DensityFunction): DensityFunction {
        return SingleDensityFunctionInput(SingleDensityFunctionInput.Type.CEIL, input)
    }
}
