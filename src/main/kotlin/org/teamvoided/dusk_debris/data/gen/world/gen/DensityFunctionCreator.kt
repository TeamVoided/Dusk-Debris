package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunction.NoiseHolder
import net.minecraft.world.level.levelgen.DensityFunctions.*
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.NetherDensityFunctionCreator.theNetherCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.overworldCreator
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.world.gen.density_functions.SingleDensityFunctionModifier

object DensityFunctionCreator {

    fun bootstrap(c: BootstrapContext<DensityFunction>) {
        c.overworldCreator()
        c.theNetherCreator()
        c.register(
            DuskDensityFunctions.EXAMPLE,
            noise(c.noiseHold(DuskNoiseParametersKeys.EXAMPLE), 0.25, 0.0)
        )
    }
//    NoiseRouterData.class


    fun BootstrapContext<*>.noise(noi: ResourceKey<NoiseParameters>): NoiseHolder =
        NoiseHolder(this.noiseHold(noi))

    fun BootstrapContext<*>.noiseHold(noi: ResourceKey<NoiseParameters>): Holder.Reference<NoiseParameters> =
        this.lookup(Registries.NOISE).getOrThrow(noi)

    fun BootstrapContext<*>.dense(den: ResourceKey<DensityFunction>): DensityFunction =
        HolderHolder(this.denseHold(den))

    fun BootstrapContext<*>.denseHold(noi: ResourceKey<DensityFunction>): Holder.Reference<DensityFunction> =
        this.lookup(Registries.DENSITY_FUNCTION).getOrThrow(noi)


    fun BootstrapContext<DensityFunction>.registerAndWrap(
        registryKey: ResourceKey<DensityFunction>,
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

    fun DensityFunction.round(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.ROUND)

    fun DensityFunction.floor(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.FLOOR)

    fun DensityFunction.ceil(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.CEIL)

    fun DensityFunction.squareRoot(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.SQUARE_ROOT)

    fun DensityFunction.cubeRoot(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.CUBE_ROOT)

    fun DensityFunction.sin(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.SIN)

    fun DensityFunction.cos(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.COS)

    fun DensityFunction.tan(): DensityFunction = mapped(this, SingleDensityFunctionModifier.Type.TAN)

    private fun mapped(input: DensityFunction, type: SingleDensityFunctionModifier.Type): DensityFunction =
        SingleDensityFunctionModifier.create(type, input)
}
