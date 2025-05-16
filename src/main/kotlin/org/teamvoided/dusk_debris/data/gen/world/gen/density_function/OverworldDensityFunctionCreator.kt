package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.denseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noise
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import voidlib.devin.world.gen.*

object OverworldDensityFunctionCreator {
    private val debug = true
    private val debugSize = 300

    fun BootstrapContext<DensityFunction>.overworldCreator() {
        this.parameters()
        this.shapers(
            DuskDensityFunctions.CONTINENT_WIERD,
            NoiseRouterData.EROSION_OVERWORLD,
            DuskDensityFunctions.DEPTH,
            DuskDensityFunctions.OFFSET,
            DuskDensityFunctions.JAGGEDNESS,
            DuskDensityFunctions.FACTOR,
            DuskDensityFunctions.SLOPED_CHEESE,
            DuskDensityFunctions.OVERWORLD_IDWJ,
            DuskDensityFunctions.OVERWORLD_FINAL_DENSITY,
            false,
            false
        )
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

        this.register(
            DuskDensityFunctions.PLATEAU_TYPE,
            const(-1)
        )

        val grandCanyonBias = 1
        val grandCanyonShifter = min(
            1,
            add(
                multiply(
                    0.5,
                    add(
                        1,
                        this.dense(NoiseRouterData.RIDGES_FOLDED_OVERWORLD)
                    )
                ),
                multiply(
                    4,
                    this.noi2D(DuskNoiseParametersKeys.GRAND_CANYON).square()
                )
            )
        )
        this.register(
            DuskDensityFunctions.GRAND_CANYON_RIDGES_FOLDED,
            cacheOnce(
                add(
                    -grandCanyonBias,
                    multiply(
                        grandCanyonShifter,
                        add(
                            grandCanyonBias,
                            this.dense(NoiseRouterData.RIDGES_FOLDED_OVERWORLD)
                        )
                    )
                )
            )
        )
    }


    private fun BootstrapContext<DensityFunction>.shapers(
        continents: RegistryKey<DensityFunction>,
        erosion: RegistryKey<DensityFunction>,
        depth: RegistryKey<DensityFunction>,
        offset: RegistryKey<DensityFunction>,
        jaggedness: RegistryKey<DensityFunction>,
        factor: RegistryKey<DensityFunction>,
        cheese: RegistryKey<DensityFunction>,
        idwj: RegistryKey<DensityFunction>,
        finalDensity: RegistryKey<DensityFunction>,
        amplified: Boolean,
        largeBiome: Boolean
    ) {
        val data = OverworldTerrainCreator.TerrainParametersData(
            this.wrap(continents),
            this.wrap(erosion),
            this.wrap(NoiseRouterData.RIDGES_OVERWORLD),
            this.wrap(NoiseRouterData.RIDGES_FOLDED_OVERWORLD),
            this.wrap(DuskDensityFunctions.GRAND_CANYON_RIDGES_FOLDED)
        )
        val dataSimple = data.simple()

        this.register(
            depth,
            cacheOnce(
                add(
                    clampedGradientY(-64, 320, 1.5, -1.5),
                    this.dense(offset)
                )
            )
        )
        this.register(
            offset,
            NoiseRouterData.splineWithBlending(
                add(-0.5, copySpline(OverworldTerrainCreator.offsetSpline(data, amplified))),
                getBlendOffset()
            )
        )
        this.register(
            jaggedness,
            NoiseRouterData.splineWithBlending(
                copySpline(OverworldTerrainCreator.jaggednessSpline(dataSimple, amplified)),
                getBlendOffset()
            )
        )
        this.register(
            factor,
            NoiseRouterData.splineWithBlending(
                add(10, copySpline(OverworldTerrainCreator.factorSpline(dataSimple, amplified))),
                getBlendOffset()
            )
        )

        val jaggednessFunction = noise(this.noiseHold(NoiseParametersKeys.JAGGED), 1500.0, 0.0)
        val jagged = multiply(this.dense(jaggedness), jaggednessFunction.halfNegative())
        val depthAndJaggedness =
            NoiseRouterData.noiseGradientDensity(this.dense(factor), add(this.dense(depth), jagged))
        this.register(
            cheese,
            depthAndJaggedness
        )
        val withoutJagged = NoiseRouterData.noiseGradientDensity(cache2D(this.dense(factor)), this.dense(depth))
        this.register(
            idwj,
            surfaceSlide(amplified, add(-45.0 / 64.0, withoutJagged).clamp(-64.0, 64.0))
        )
        this.register(
            finalDensity,
            interpolated(blendDensity(this.createFinalDensity(amplified, largeBiome, cheese))).squeeze()
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
            -5.0 / 64.0,
            0,
            24,
            if (amplified) 0.4 else (15.0 / 128.0)
        )
    }

    private fun BootstrapContext<DensityFunction>.wrap(df: RegistryKey<DensityFunction>): DensityFunctions.Spline.FunctionWrapper =
        DensityFunctions.Spline.FunctionWrapper(this.denseHold(df))


    private fun BootstrapContext<DensityFunction>.noi2D(
        noise: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        scaleXZ: Double = 0.25
    ): DensityFunction =
        shiftedNoise2d(
            this.dense(NoiseRouterData.SHIFT_X),
            this.dense(NoiseRouterData.SHIFT_Z),
            scaleXZ,
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