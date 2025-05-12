package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noise
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.NetherDensityFunctionCreator.parameters
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.parameters
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys

object OverworldDensityFunctionCreator {
    fun BootstrapContext<DensityFunction>.overworldCreator() {
        this.parameters()
    }


    fun BootstrapContext<DensityFunction>.parameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)

        this.register(
            DuskDensityFunctions.CONTINENTALNESS,
            DensityFunctions.cacheOnce(
                DensityFunctions.add(
                    this.dense(NoiseRouterData.CONTINENTS_OVERWORLD),
                    DensityFunctions.rangeChoice(
                        this.dense(NoiseRouterData.CONTINENTS_OVERWORLD),
                        0.0,
                        2.0,
                        DensityFunctions.multiply(
                            this.noi2D(DuskNoiseParametersKeys.CONTINENTAL_WEIRDNESS),
                            DensityFunctions.constant(0.5)
                        ).cube(),
                        DensityFunctions.constant(0.0)
                    )
                )
            )
        )

        //  |-(|1.5x-1|-1)+1|-1
        this.register(
            DuskDensityFunctions.RIDGES_WEIRD,
            DensityFunctions.cacheOnce(
                DensityFunctions.add(
                    DensityFunctions.add(
                        DensityFunctions.multiply(
                            DensityFunctions.add(
                                DensityFunctions.add(
                                    DensityFunctions.multiply(
                                        this.dense(NoiseRouterData.RIDGES_OVERWORLD),
                                        DensityFunctions.constant(1.5)
                                    ),
                                    DensityFunctions.constant(-1.0)
                                ).abs(),
                                DensityFunctions.constant(-1.0)
                            ),
                            DensityFunctions.constant(-1.0)
                        ),
                        DensityFunctions.constant(1.0)
                    ).abs(),
                    DensityFunctions.constant(-1.0)
                )
            )
        )
    }

    private fun BootstrapContext<DensityFunction>.noi2D(noise: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>): DensityFunction =
        DensityFunctions.shiftedNoise2d(
            this.dense(NoiseRouterData.SHIFT_X),
            this.dense(NoiseRouterData.SHIFT_Z),
            0.25,
            this.noiseHold(noise)
        )

    private fun createFoldedRidgesOverworld(function: DensityFunction): DensityFunction {
        return DensityFunctions.multiply(
            DensityFunctions.add(
                DensityFunctions.add(
                    function.abs(),
                    DensityFunctions.constant(-2.0 / 3.0)
                ).abs(), DensityFunctions.constant(-1.0 / 3.0)
            ), DensityFunctions.constant(-3.0)
        )
    }

    fun overworld(
        largeBiome: Boolean,
        amplified: Boolean
    ): NoiseRouter {
        return NoiseRouter(
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
        )
    }

    /*fun overworld(
         densityFunction: HolderProvider<DensityFunction>,
         parameters: HolderProvider<DoublePerlinNoiseSampler.NoiseParameters>,
         largeBiome: Boolean,
         amplified: Boolean
     ): NoiseRouter {
         val aquiferBarrier =
             DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5)
         val aquiferFloodedness =            DensityFunctions.noise(
                 parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS),
                 0.67
             )
         val aquiferSpread =
             DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7143)
         val aquiferLava = DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_LAVA))
         val shiftX = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.SHIFT_X)
         val shiftZ = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.SHIFT_Z)
         val temperature = DensityFunctions.shiftedNoise2d(
             shiftX,            shiftZ,            0.25,
             parameters.getHolderOrThrow(if (largeBiome) NoiseParametersKeys.TEMPERATURE_LARGE else NoiseParametersKeys.TEMPERATURE)
         )
         val vegetation = DensityFunctions.shiftedNoise2d(
             shiftX,
             shiftZ,
             0.25,
             parameters.getHolderOrThrow(if (largeBiome) NoiseParametersKeys.VEGETATION_LARGE else NoiseParametersKeys.VEGETATION)
         )
         val factor = NoiseRouterData.getFunction(
             densityFunction,
             if (largeBiome) NoiseRouterData.FACTOR_OVERWORLD_LARGE_BIOME else (if (amplified) NoiseRouterData.FACTOR_OVERWORLD_AMPLIFIED else NoiseRouterData.FACTOR_OVERWORLD)
         )
         val depth = NoiseRouterData.getFunction(
             densityFunction,
             if (largeBiome) NoiseRouterData.DEPTH_OVERWORLD_LARGE_BIOME else (if (amplified) NoiseRouterData.DEPTH_OVERWORLD_AMPLIFIED else NoiseRouterData.DEPTH_OVERWORLD)
         )
         val densityFunction12 =
             NoiseRouterData.noiseGradientDensity(DensityFunctions.cache2D(factor), depth)
         val slopedCheese = NoiseRouterData.getFunction(
             densityFunction,
             if (largeBiome) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_LARGE_BIOME else (if (amplified) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_AMPLIFIED else NoiseRouterData.SLOPED_CHEESE_OVERWORLD)
         )
         val idwj = DensityFunctions.min(
             slopedCheese,
             DensityFunctions.multiply(
                 DensityFunctions.constant(5.0),
                 NoiseRouterData.getFunction(densityFunction, NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
             )
         )
         val slopedCaves = DensityFunctions.rangeChoice(
             slopedCheese,
             -1000000.0,
             1.5625,
             idwj,
             NoiseRouterData.underground(densityFunction, parameters, slopedCheese)
         )
         val finalDensity = DensityFunctions.min(
             NoiseRouterData.postProcess(
                 NoiseRouterData.surfaceSlide(
                     amplified,
                     slopedCaves
                 )
             ), NoiseRouterData.getFunction(densityFunction, NoiseRouterData.CAVES_NOODLE_OVERWORLD)
         )
         val densityFunction17 = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.Y)
         val veinMin = Stream.of(*OreVeinCreator.VeinType.entries.toTypedArray())
             .mapToInt { it.minY }
             .min().orElse(-DimensionType.MIN_Y * 2)
         val veinMax = Stream.of(*OreVeinCreator.VeinType.entries.toTypedArray())
             .mapToInt { it.maxY }
             .max().orElse(-DimensionType.MIN_Y * 2)
         val veinToggle = NoiseRouterData.yLimitedInterpolatable(
             densityFunction17, DensityFunctions.noise(
                 parameters.getHolderOrThrow(
                     NoiseParametersKeys.ORE_VEININESS
                 ), 1.5, 1.5
             ), veinMin, veinMax, 0
         )
         val veinScale = 4.0
         val densityFunction19 = NoiseRouterData.yLimitedInterpolatable(
             densityFunction17, DensityFunctions.noise(
                 parameters.getHolderOrThrow(
                     NoiseParametersKeys.ORE_VEIN_A
                 ), veinScale, veinScale
             ), veinMin, veinMax, 0
         ).abs()
         val densityFunction20 = NoiseRouterData.yLimitedInterpolatable(
             densityFunction17, DensityFunctions.noise(
                 parameters.getHolderOrThrow(
                     NoiseParametersKeys.ORE_VEIN_B
                 ), veinScale, veinScale
             ), veinMin, veinMax, 0
         ).abs()
         val veinRidged = DensityFunctions.add(
             DensityFunctions.constant(-0.08),
             DensityFunctions.max(densityFunction19, densityFunction20)
         )
         val veinGap = DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.ORE_GAP))
         return NoiseRouter(
             aquiferBarrier,
             aquiferFloodedness,
             aquiferSpread,
             aquiferLava,
             temperature,
             vegetation,
             NoiseRouterData.getFunction(
                 densityFunction,
                 if (largeBiome) NoiseRouterData.CONTINENTS_OVERWORLD_LARGE_BIOME else NoiseRouterData.CONTINENTS_OVERWORLD
             ),
             NoiseRouterData.getFunction(
                 densityFunction,
                 if (largeBiome) NoiseRouterData.EROSION_OVERWORLD_LARGE_BIOME else NoiseRouterData.EROSION_OVERWORLD
             ),
             depth,
             NoiseRouterData.getFunction(densityFunction, NoiseRouterData.RIDGES_OVERWORLD),
             NoiseRouterData.surfaceSlide(
                 amplified,
                 DensityFunctions.add(densityFunction12, DensityFunctions.constant(-0.703125)).clamp(-64.0, 64.0)
             ),
             finalDensity,
             veinToggle,
             veinRidged,
             veinGap
         )}*/
}