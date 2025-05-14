package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.Direction.Axis
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.denseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.world.gen.density_functions.DebugAxis
import org.teamvoided.dusk_debris.world.gen.density_functions.DebugCheckerboard
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainParametersCreator
import voidlib.devin.world.gen.*

object OverworldDensityFunctionCreator {
    private val debug = true
    private val debugSize = 300

    fun BootstrapContext<DensityFunction>.overworldCreator() {
        this.parameters()
        this.shapers()
    }


    fun BootstrapContext<DensityFunction>.parameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)
        this.register(DuskDensityFunctions.TEMPERATURE, noi2D(NoiseParametersKeys.TEMPERATURE))
        this.register(DuskDensityFunctions.HUMIDITY, noi2D(NoiseParametersKeys.VEGETATION))
        this.register(
            DuskDensityFunctions.CONTINENT_WIERD,
            add(
                0,
                this.dense(NoiseRouterData.CONTINENTS_OVERWORLD)
            )
            //cacheOnce(
            //    add(
            //        this.dense(NoiseRouterData.CONTINENTS_OVERWORLD),
            //        rangeChoice(
            //            this.dense(NoiseRouterData.CONTINENTS_OVERWORLD),
            //            0.0,
            //            2.0,
            //            multiply(
            //                0.5,
            //                this.noi2D(DuskNoiseParametersKeys.CONTINENTAL_WEIRDNESS)
            //            ).cube(),
            //            DensityFunctions.constant(0.0)
            //        )
            //    )
            //)
        )

        //  |-(|1.5x-1|-1)+1|-1
        this.register(
            DuskDensityFunctions.RIDGES_WEIRD,
            cacheOnce(
                add(
                    -1,
                    add(
                        1,
                        multiply(
                            -1,
                            add(
                                -1,
                                add(
                                    -1.0,
                                    multiply(
                                        1.5,
                                        this.dense(NoiseRouterData.RIDGES_OVERWORLD)
                                    )
                                ).abs()
                            )
                        )
                    ).abs()
                )
            )
        )
    }


    private fun BootstrapContext<DensityFunction>.shapers() {
        val continents = this.wrap(DuskDensityFunctions.CONTINENT_WIERD)
        val erosion = this.wrap(NoiseRouterData.EROSION_OVERWORLD)
        val ridges = this.wrap(NoiseRouterData.RIDGES_OVERWORLD)
        val ridgesFolded = this.wrap(NoiseRouterData.RIDGES_FOLDED_OVERWORLD)

        this.register(
            DuskDensityFunctions.DEPTH,
            cacheOnce(
                add(
                    clampedGradientY(-64, 320, 1.5, -1.5),
                    this.dense(DuskDensityFunctions.OFFSET)
                )
            )
        )
        this.register(
            DuskDensityFunctions.OFFSET,
            NoiseRouterData.splineWithBlending(
                add(
                    -0.5,
                    copySpline(
                        OverworldTerrainParametersCreator.offsetSpline(
                            continents,
                            erosion,
                            ridgesFolded,
                            false
                        )
                    )
                ),
                getBlendOffset()
            )
        )
        this.register(
            DuskDensityFunctions.JAGGEDNESS,
            NoiseRouterData.splineWithBlending(
                copySpline(
                    OverworldTerrainParametersCreator.jaggednessSpline(
                        continents,
                        erosion,
                        ridges,
                        ridgesFolded,
                        false
                    )
                ),
                getBlendOffset()
            )
        )
        this.register(
            DuskDensityFunctions.FACTOR,
            NoiseRouterData.splineWithBlending(
                add(
                    10,
                    copySpline(
                        OverworldTerrainParametersCreator.factorSpline(
                            continents,
                            erosion,
                            ridges,
                            ridgesFolded,
                            false
                        )
                    )
                ),
                getBlendOffset()
            )
        )

        val jaggednessFunction = noise(this.noiseHold(NoiseParametersKeys.JAGGED), 1500.0, 0.0)
        val jagged = multiply(this.dense(DuskDensityFunctions.JAGGEDNESS), jaggednessFunction.halfNegative())
        val depthAndJaggedness = NoiseRouterData.noiseGradientDensity(
            this.dense(DuskDensityFunctions.FACTOR),
            add(
                this.dense(DuskDensityFunctions.DEPTH),
                jagged
            )
        )
        this.register(
            DuskDensityFunctions.SLOPED_CHEESE,
            depthAndJaggedness
        )
        val idwj = NoiseRouterData.noiseGradientDensity(
            cache2D(this.dense(DuskDensityFunctions.FACTOR)),
            this.dense(DuskDensityFunctions.DEPTH)
        )
        this.register(
            DuskDensityFunctions.OVERWORLD_IDWJ,
            surfaceSlide(
                false,
                add(-0.703125, idwj).clamp(-64.0, 64.0)
            )
        )
        this.register(
            DuskDensityFunctions.OVERWORLD_FINAL_DENSITY,
            interpolated(
                blendDensity(
                    this.createFinalDensity(
                        false,
                        false,
                        DuskDensityFunctions.SLOPED_CHEESE
                    )
                )
            ).squeeze()
        )
    }


    private fun BootstrapContext<DensityFunction>.createFinalDensity(
        amplified: Boolean,
        largeBiome: Boolean,
        cheese: RegistryKey<DensityFunction>
    ): DensityFunction {
        val slide = surfaceSlide(amplified, this.dense(cheese))
        return NoiseRouterData.postProcess(slide)
    }

    private fun surfaceSlide(amplified: Boolean, density: DensityFunction): DensityFunction {
        return NoiseRouterData.slide(
            density,
            -64,
            384,
            if (amplified) 16 else 80,
            if (amplified) 0 else 64,
            -0.078125,
            0,
            24,
            if (amplified) 0.4 else 0.1171875
        )
    }

    private fun BootstrapContext<DensityFunction>.wrap(df: RegistryKey<DensityFunction>): DensityFunctions.Spline.FunctionWrapper =
        DensityFunctions.Spline.FunctionWrapper(this.denseHold(df))


    private fun BootstrapContext<DensityFunction>.noi2D(noise: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>): DensityFunction =
        shiftedNoise2d(
            this.dense(NoiseRouterData.SHIFT_X),
            this.dense(NoiseRouterData.SHIFT_Z),
            0.25,
            this.noiseHold(noise)
        )

    fun BootstrapContext<ChunkGeneratorSettings>.overworld(largeBiome: Boolean, amplified: Boolean): NoiseRouter {
        return NoiseRouter(
            DensityFunctions.constant(1.0),
            DensityFunctions.constant(1.0),
            DensityFunctions.constant(1.0),
            DensityFunctions.constant(1.0),
            this.dense(DuskDensityFunctions.TEMPERATURE),
            this.dense(DuskDensityFunctions.HUMIDITY),
            this.dense(DuskDensityFunctions.CONTINENT_WIERD),
            this.dense(NoiseRouterData.EROSION_OVERWORLD),
            DensityFunctions.constant(0.0),
            this.dense(DuskDensityFunctions.RIDGES_WEIRD),
            this.dense(DuskDensityFunctions.OVERWORLD_IDWJ),
            this.dense(DuskDensityFunctions.OVERWORLD_FINAL_DENSITY),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0)
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
        val aquiferFloodedness = DensityFunctions.noise(
            parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS),
            0.67
        )
        val aquiferSpread =
            DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7143)
        val aquiferLava = DensityFunctions.noise(parameters.getHolderOrThrow(NoiseParametersKeys.AQUIFER_LAVA))
        val shiftX = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.SHIFT_X)
        val shiftZ = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.SHIFT_Z)
        val temperature = DensityFunctions.shiftedNoise2d(
            shiftX, shiftZ, 0.25,
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
        val idwj =
            NoiseRouterData.noiseGradientDensity(DensityFunctions.cache2D(factor), depth)
        val slopedCheese = NoiseRouterData.getFunction(
            densityFunction,
            if (largeBiome) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_LARGE_BIOME else (if (amplified) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_AMPLIFIED else NoiseRouterData.SLOPED_CHEESE_OVERWORLD)
        )
        val cheeseAndEntrance = DensityFunctions.min(
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
            cheeseAndEntrance,
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
        val yLevel = NoiseRouterData.getFunction(densityFunction, NoiseRouterData.Y)
        val veinMin = Stream.of(*OreVeinCreator.VeinType.entries.toTypedArray())
            .mapToInt { it.minY }
            .min().orElse(-DimensionType.MIN_Y * 2)
        val veinMax = Stream.of(*OreVeinCreator.VeinType.entries.toTypedArray())
            .mapToInt { it.maxY }
            .max().orElse(-DimensionType.MIN_Y * 2)
        val veinToggle = NoiseRouterData.yLimitedInterpolatable(
            yLevel, DensityFunctions.noise(
                parameters.getHolderOrThrow(
                    NoiseParametersKeys.ORE_VEININESS
                ), 1.5, 1.5
            ), veinMin, veinMax, 0
        )
        val veinScale = 4.0
        val vein1 = NoiseRouterData.yLimitedInterpolatable(
            yLevel, DensityFunctions.noise(
                parameters.getHolderOrThrow(
                    NoiseParametersKeys.ORE_VEIN_A
                ), veinScale, veinScale
            ), veinMin, veinMax, 0
        ).abs()
        val vein2 = NoiseRouterData.yLimitedInterpolatable(
            yLevel, DensityFunctions.noise(
                parameters.getHolderOrThrow(
                    NoiseParametersKeys.ORE_VEIN_B
                ), veinScale, veinScale
            ), veinMin, veinMax, 0
        ).abs()
        val veinRidged = DensityFunctions.add(
            DensityFunctions.constant(-0.08),
            DensityFunctions.max(vein1, vein2)
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
                DensityFunctions.add(idwj, DensityFunctions.constant(-0.703125)).clamp(-64.0, 64.0)
            ),
            finalDensity,
            veinToggle,
            veinRidged,
            veinGap
        )
    }*/
}