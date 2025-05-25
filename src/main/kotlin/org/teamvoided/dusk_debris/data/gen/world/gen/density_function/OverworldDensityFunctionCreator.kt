package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.Spline
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
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.routerParameters
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import voidlib.devin.world.gen.*

object OverworldDensityFunctionCreator {
    private val debug = true
    private val debugSize = 300

    fun BootstrapContext<DensityFunction>.overworldCreator() {
        this.routerParameters()
        this.shapingParameters()
        this.stoneTowers()
        this.unchangingShapers()
        this.shapers(false, false)
    }


    fun BootstrapContext<DensityFunction>.routerParameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)
        this.register(DuskDensityFunctions.TEMPERATURE, noi2D(NoiseParametersKeys.TEMPERATURE))
        this.register(DuskDensityFunctions.HUMIDITY, noi2D(NoiseParametersKeys.VEGETATION))
        this.register(
            DuskDensityFunctions.UR_TYPE,
            flatCacheNoi2D(DuskNoiseParametersKeys.UR_TYPE)
        )
        this.register(
            DuskDensityFunctions.CONTINENT_ROUTER,
            const(OverworldTerrainCreator.Cont.Midland.f) //this.dense(NoiseRouterData.CONTINENTS_OVERWORLD)

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
        this.register(
            DuskDensityFunctions.EROSION_ROUTER,
            const(OverworldTerrainCreator.Eros.Flats2.f) //this.dense(NoiseRouterData.CONTINENTS_OVERWORLD)
        )
        this.register(
            DuskDensityFunctions.RIDGES_ROUTER,
            this.dense(NoiseRouterData.RIDGES_OVERWORLD).abs()
        )
    }

    fun BootstrapContext<DensityFunction>.shapingParameters() {
        this.register(
            DuskDensityFunctions.PLATEAU_TYPE,
            flatCacheNoi2D(DuskNoiseParametersKeys.PLATEAU_TYPE)
        )
        this.register(
            DuskDensityFunctions.FLATS_TYPE,
            flatCacheNoi2D(DuskNoiseParametersKeys.FLATS_TYPE)
        )
        this.register(
            DuskDensityFunctions.FLATS_ELEV,
            flatCacheNoi2D(DuskNoiseParametersKeys.FLATS_ELEV)
        )

        val grandCanyonBias = 0.825f
        val grandCanyonShifter = min(
            1,
            add(
                multiply(
                    0.1,
                    add(
                        1,
                        this.dense(NoiseRouterData.RIDGES_FOLDED_OVERWORLD)
                    )
                ),
                multiply(
                    4,
                    this.noi2D(DuskNoiseParametersKeys.GRAND_CANYON, 0.75).square()
                )
            )
        )
        this.register(
            DuskDensityFunctions.GRAND_CANYON_RIDGES_FOLDED,
            cacheOnce(
                min(
                    this.dense(NoiseRouterData.RIDGES_FOLDED_OVERWORLD),
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
        )
    }


    private fun BootstrapContext<DensityFunction>.unchangingShapers() {
        this.register(
            DuskDensityFunctions.AQU_BARRIER,
            noise(this.noiseHold(NoiseParametersKeys.AQUIFER_BARRIER), 0.5)
        )
        this.register(
            DuskDensityFunctions.AQU_LAVA,
            noise(this.noiseHold(NoiseParametersKeys.AQUIFER_LAVA))
        )
    }

    private fun BootstrapContext<DensityFunction>.shapers(
        amplified: Boolean,
        largeBiome: Boolean,
        caves: Boolean = false
    ) {
        //every single one of these values has to change depending on *amplified* or *largeBiome*
        val continents: RegistryKey<DensityFunction> = DuskDensityFunctions.CONTINENT_ROUTER
        val erosion: RegistryKey<DensityFunction> = DuskDensityFunctions.EROSION_ROUTER
        val depth: RegistryKey<DensityFunction> = DuskDensityFunctions.DEPTH
        val offset: RegistryKey<DensityFunction> = DuskDensityFunctions.OFFSET
        val jaggedness: RegistryKey<DensityFunction> = DuskDensityFunctions.JAGGEDNESS
        val factor: RegistryKey<DensityFunction> = DuskDensityFunctions.FACTOR
        val cheese: RegistryKey<DensityFunction> = DuskDensityFunctions.SLOPED_CHEESE
        val idwj: RegistryKey<DensityFunction> = DuskDensityFunctions.OVERWORLD_IDWJ
        val finalDensity: RegistryKey<DensityFunction> = DuskDensityFunctions.OVERWORLD_FINAL_DENSITY
        val urCondition: RegistryKey<DensityFunction> = DuskDensityFunctions.UR_CONDITION
        val urDensity: RegistryKey<DensityFunction> = DuskDensityFunctions.UR_DENSITY
        val aquiferFloodedness: RegistryKey<DensityFunction> = DuskDensityFunctions.AQU_FLOODEDNESS
        val aquiferFluidSpread: RegistryKey<DensityFunction> = DuskDensityFunctions.AQU_FLUID_SPREAD

        val data = OverworldTerrainCreator.TerrainParametersData(
            this.wrap(continents),
            this.wrap(erosion),
            this.wrap(NoiseRouterData.RIDGES_OVERWORLD),
            this.wrap(NoiseRouterData.RIDGES_FOLDED_OVERWORLD),
            this.wrap(DuskDensityFunctions.PLATEAU_TYPE),
            this.wrap(DuskDensityFunctions.GRAND_CANYON_RIDGES_FOLDED),
            this.wrap(DuskDensityFunctions.FLATS_TYPE),
            this.wrap(DuskDensityFunctions.FLATS_ELEV)
        )

        this.caveRiver(data, urCondition, urDensity)
        this.register(
            aquiferFloodedness,
            rangeChoice(
                this.dense(urCondition),
                0.5,
                1.1,
                const(1),
                noise(this.noiseHold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67)
            )
        )
        this.register(
            aquiferFluidSpread,
            rangeChoice(
                this.dense(urCondition),
                0.5,
                1.5,
                const(0.5),
                noise(this.noiseHold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857)
            )
        )

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
                copySpline(OverworldTerrainCreator.jaggednessSpline(data, amplified)),
                getBlendOffset()
            )
        )
        this.register(
            factor,
            NoiseRouterData.splineWithBlending(
                add(10, copySpline(OverworldTerrainCreator.factorSpline(data, amplified))),
                getBlendOffset()
            )
        )

        val jaggednessFunction = noise(this.noiseHold(NoiseParametersKeys.JAGGED), 1500.0, 0.0)
        val jagged = multiply(this.dense(jaggedness), jaggednessFunction.halfNegative())
        val jaggednessAndExtra = add(jagged, this.dense(DuskDensityFunctions.STONE_TOWERS_TOWER))
        this.register(
            cheese,
            NoiseRouterData.noiseGradientDensity(this.dense(factor), add(this.dense(depth), jaggednessAndExtra))
        )
        val withoutJagged = NoiseRouterData.noiseGradientDensity(cache2D(this.dense(factor)), this.dense(depth))
        this.register(
            idwj,
            surfaceSlide(amplified, add(-45.0 / 64.0, withoutJagged).clamp(-64.0, 64.0))
        )

        if (caves) {
            val cheeseAndEntrance = min(
                this.dense(cheese),
                multiply(
                    5,
                    this.dense(NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
                )
            )
            val slopedCaves = rangeChoice(
                this.dense(cheese),
                -1000000,
                1.5625,
                cheeseAndEntrance,
                this.underground(this.dense(cheese))
            )
            this.register(
                finalDensity,
                min(
                    multiply(
                        0.64,
                        min(
                            this.dense(urDensity),
                            interpolated(blendDensity(surfaceSlide(amplified, slopedCaves)))
                        )
                    ).squeeze(),
                    this.dense(NoiseRouterData.CAVES_NOODLE_OVERWORLD)
                )
            )
        } else {
            this.register(
                finalDensity,
                multiply(
                    0.64,
                    min(
                        this.dense(urDensity),
                        interpolated(blendDensity(surfaceSlide(amplified, this.dense(cheese))))
                    )
                ).squeeze()
            )
        }
    }

    private fun BootstrapContext<DensityFunction>.stoneTowers() {
        val offset = this.noiseHold(DuskNoiseParametersKeys.STONE_TOWERS_OFFSET)
        val y: DensityFunction = multiply(0.1, this.dense(NoiseRouterData.Y))
        val x: DensityFunction = multiply(y, shiftX(offset))
        val z: DensityFunction = multiply(y, shiftZ(offset))
        this.register(
            DuskDensityFunctions.STONE_TOWERS,
            cacheOnce(shiftedNoise2d(x, z, 1.0, this.noiseHold(DuskNoiseParametersKeys.STONE_TOWERS)))
        )
        //TEST TO SEE IF I CAN SEPERATE THE TOWER HEIGHT FROM TERRAIN BENEATH IT
        //this.register(
        //    DuskDensityFunctions.STONE_TOWERS_MOUND,
        //    copySpline(
        //        Spline.builder(this.wrap(DuskDensityFunctions.STONE_TOWERS))
        //            .add(0f, 0f)
        //            .add(0.5f, 0.2f, 0.4f)
        //            .build()
        //    )
        //)
        //this.register(
        //    DuskDensityFunctions.STONE_TOWERS_TOWER,
        //    copySpline(
        //        Spline.builder(this.wrap(DuskDensityFunctions.STONE_TOWERS))
        //            .add(0.5f, 0f)
        //            .add(0.75f, 1f, 0.1f)
        //            .build()
        //    )
        //)

        //val stoneTowersHeight = Spline.builder(this.wrap()) //do 0.3 to 1, its around 100 to 190
        val stoneTowersSpline = Spline.builder(this.wrap(DuskDensityFunctions.STONE_TOWERS))
            .add(0.3f, 0f)
            .add(0.45f, 0.2f, 2f)
            .add(0.5f, 0.5f, 0.5f)
        this.register(
            DuskDensityFunctions.STONE_TOWERS_TOWER,
            copySpline(
                Spline.builder(this.wrap(NoiseRouterData.RIDGES_FOLDED_OVERWORLD))
                    .add(-0.8f, 0f)
                    .add(-0.6f, stoneTowersSpline.build())
                    .build()
            )
        )
    }


    private fun BootstrapContext<DensityFunction>.caveRiver(
        data: OverworldTerrainCreator.TerrainParametersData<DensityFunctions.Spline.Point, DensityFunctions.Spline.FunctionWrapper>,
        condition: RegistryKey<DensityFunction>,
        density: RegistryKey<DensityFunction>,
    ) {
        this.register(
            condition,
            rangeChoice(
                this.dense(NoiseRouterData.Y),
                45,
                10000,
                flatCache(cache2D(copySpline(OverworldTerrainCreator.undergroundRiverCondition(data)))),
                const(0)
            )
        )
        this.register(
            density,
            rangeChoice(
                this.dense(condition),
                0.5,
                1.1,
                add(
                    -0.003,
                    add(
                        interpolated(
                            multiply(
                                add(
                                    0.7,
                                    multiply(
                                        0.3,
                                        flatCacheNoi2D(DuskNoiseParametersKeys.UR_HEIGHT)
                                    )
                                ),
                                rangeChoice(
                                    this.dense(DuskDensityFunctions.UR_TYPE),
                                    0,
                                    10000,
                                    clampedGradientY(-10, 62, -1, 0),
                                    clampedGradientY(-10, 138, -1, 1),
                                )
                            )
                        ).square(),
                        interpolated(
                            multiply(
                                this.dense(NoiseRouterData.RIDGES_OVERWORLD),
                                copySpline(
                                    Spline.builder(this.wrap(NoiseRouterData.Y))
                                        .add(60f, 1.5f)
                                        .add(70f, 1.5f)
                                        .add(90f, 3f, 0.05f)
                                        .add(100f, 3.25f, 0.05f)
                                        .build()
                                )
                            )
                        ).square()
                    )
                ),
                const(1000000)
            )
        )
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

    private fun BootstrapContext<DensityFunction>.flatCacheNoi2D(
        noise: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        scaleXZ: Double = 0.25
    ): DensityFunction =
        flatCache(
            cache2D(
                shiftedNoise2d(
                    this.dense(NoiseRouterData.SHIFT_X),
                    this.dense(NoiseRouterData.SHIFT_Z),
                    scaleXZ,
                    this.noiseHold(noise)
                )
            )
        )


    private fun BootstrapContext<*>.underground(slopedCheese: DensityFunction): DensityFunction {
        val spaghetti2D = this.dense(NoiseRouterData.CAVES_SPAGHETTI_2D_OVERWORLD)
        val spaghettiRough = this.dense(NoiseRouterData.CAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD)
        val caveLayerNoise = noise(this.noiseHold(NoiseParametersKeys.CAVE_LAYER), 8.0)
        val caveLayer = multiply(4, caveLayerNoise.square())
        val caveCheese = noise(this.noiseHold(NoiseParametersKeys.CAVE_CHEESE), 0.6666666666666666)
        val surfaceOrCave = add(
            add(
                0.27,
                caveCheese
            ).clamp(-1.0, 1.0),
            add(
                1.5,
                multiply(
                    -0.64,
                    slopedCheese
                )
            ).clamp(0.0, 0.5)
        )
        val surfaceAndCave = add(caveLayer, surfaceOrCave)
        val entrances = min(
            min(
                surfaceAndCave,
                this.dense(NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
            ),
            add(
                spaghetti2D,
                spaghettiRough
            )
        )
        val cavePillars = this.dense(NoiseRouterData.CAVES_PILLARS_OVERWORLD)
        val cavePillarsSelector = rangeChoice(
            cavePillars,
            -1000000,
            0.03,
            const(-1000000),
            cavePillars
        )
        return max(entrances, cavePillarsSelector)
    }


    fun BootstrapContext<ChunkGeneratorSettings>.overworld(largeBiome: Boolean, amplified: Boolean): NoiseRouter {
        return NoiseRouter(
            this.dense(DuskDensityFunctions.AQU_BARRIER),
            this.dense(DuskDensityFunctions.AQU_FLOODEDNESS),
            this.dense(DuskDensityFunctions.AQU_FLUID_SPREAD),
            this.dense(DuskDensityFunctions.AQU_LAVA),
            this.dense(DuskDensityFunctions.TEMPERATURE),
            this.dense(DuskDensityFunctions.HUMIDITY),
            this.dense(DuskDensityFunctions.CONTINENT_ROUTER),
            this.dense(DuskDensityFunctions.EROSION_ROUTER),
            this.dense(DuskDensityFunctions.DEPTH),
            this.dense(DuskDensityFunctions.RIDGES_ROUTER),
            this.dense(DuskDensityFunctions.OVERWORLD_IDWJ),
            this.dense(DuskDensityFunctions.OVERWORLD_FINAL_DENSITY),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0),
            DensityFunctions.constant(0.0)
        )
    }

    /*
    fun BootstrapContext<ChunkGeneratorSettings>.overworldConvert(
        largeBiome: Boolean,
        amplified: Boolean
    ): NoiseRouter {
        val aquiferBarrier = noise(this.noiseHold(NoiseParametersKeys.AQUIFER_BARRIER), 0.5)
        val aquiferFloodedness = noise(this.noiseHold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67)
        val aquiferSpread = noise(this.noiseHold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7143)
        val aquiferLava = noise(this.noiseHold(NoiseParametersKeys.AQUIFER_LAVA))
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)
        val temperature = shiftedNoise2d(
            shiftX, shiftZ, 0.25,
            this.noiseHold(if (largeBiome) NoiseParametersKeys.TEMPERATURE_LARGE else NoiseParametersKeys.TEMPERATURE)
        )
        val vegetation = shiftedNoise2d(
            shiftX,
            shiftZ,
            0.25,
            this.noiseHold(if (largeBiome) NoiseParametersKeys.VEGETATION_LARGE else NoiseParametersKeys.VEGETATION)
        )
        val factor = this.dense(
            if (largeBiome) NoiseRouterData.FACTOR_OVERWORLD_LARGE_BIOME
            else if (amplified) NoiseRouterData.FACTOR_OVERWORLD_AMPLIFIED
            else NoiseRouterData.FACTOR_OVERWORLD
        )
        val depth = this.dense(
            if (largeBiome) NoiseRouterData.DEPTH_OVERWORLD_LARGE_BIOME
            else if (amplified) NoiseRouterData.DEPTH_OVERWORLD_AMPLIFIED
            else NoiseRouterData.DEPTH_OVERWORLD
        )
        val idwj = NoiseRouterData.noiseGradientDensity(cache2D(factor), depth)
        val slopedCheese = this.dense(
            if (largeBiome) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_LARGE_BIOME
            else if (amplified) NoiseRouterData.SLOPED_CHEESE_OVERWORLD_AMPLIFIED
            else NoiseRouterData.SLOPED_CHEESE_OVERWORLD
        )
        val cheeseAndEntrance = min(
            slopedCheese,
            multiply(
                5,
                this.dense(NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
            )
        )
        val slopedCaves = rangeChoice(
            slopedCheese,
            -1000000,
            1.5625,
            cheeseAndEntrance,
            underground(slopedCheese)
        )
        val finalDensity = min(
            NoiseRouterData.postProcess(
                surfaceSlide(
                    amplified,
                    slopedCaves
                )
            ), this.dense(NoiseRouterData.CAVES_NOODLE_OVERWORLD)
        )
        return NoiseRouter(
            aquiferBarrier,
            aquiferFloodedness,
            aquiferSpread,
            aquiferLava,
            temperature,
            vegetation,
            this.dense(
                if (largeBiome) NoiseRouterData.CONTINENTS_OVERWORLD_LARGE_BIOME
                else NoiseRouterData.CONTINENTS_OVERWORLD
            ),
            this.dense(
                if (largeBiome) NoiseRouterData.EROSION_OVERWORLD_LARGE_BIOME
                else NoiseRouterData.EROSION_OVERWORLD
            ),
            depth,
            this.dense(NoiseRouterData.RIDGES_OVERWORLD),
            surfaceSlide(
                amplified,
                add(-0.703125, idwj).clamp(-64.0, 64.0)
            ),
            finalDensity,
            const(0),
            const(0),
            const(0)
        )
    }

    fun overworld(
        densityFunction: RegistryKey<DensityFunction>,
        parameters: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
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