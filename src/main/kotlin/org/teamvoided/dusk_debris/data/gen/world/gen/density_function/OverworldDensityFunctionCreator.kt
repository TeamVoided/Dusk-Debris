package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.util.CubicSpline
import net.minecraft.world.level.levelgen.*
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.denseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.overworld.UndergroundDensityCreator.caveLake
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.overworld.UndergroundDensityCreator.caveRiver
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.overworld.UndergroundDensityCreator.underground
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import voidlib.devin.world.gen.*

object OverworldDensityFunctionCreator {

    fun BootstrapContext<DensityFunction>.overworldCreator() {
        this.routerParameters()
        this.shapingParameters()
        this.stoneTowers()
        this.caveLake()
        this.unchangingShapers()
        this.shapers(false, false, false)
    }


    fun BootstrapContext<DensityFunction>.routerParameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)
        this.register(DuskDensityFunctions.TEMPERATURE, noi2D(Noises.TEMPERATURE))
        this.register(DuskDensityFunctions.HUMIDITY, noi2D(Noises.VEGETATION))
        this.register(
            DuskDensityFunctions.UR_TYPE,
            flatCacheNoi2D(DuskNoiseParametersKeys.UR_TYPE)
        )
        this.register(
            DuskDensityFunctions.CONTINENT_ROUTER,
            const(OverworldTerrainCreator.Cont.Midland.f)
            //this.dense(NoiseRouterData.CONTINENTS_OVERWORLD)

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
            //const(OverworldTerrainCreator.Eros.Flats2.f)
            add(0, this.dense(NoiseRouterData.EROSION))
        )

        // (-||1.5x|-1|)+1, or (RF+1)/2
        // add(1, multiply(-1, add(-1, multiply(1.5, this.dense(NoiseRouterData.RIDGES)).abs()).abs()))
        val rf = this.dense(NoiseRouterData.RIDGES_FOLDED)
        this.register(
            DuskDensityFunctions.RIDGES_ROUTER,
            rangeChoice(
                flatCacheNoi2D(DuskNoiseParametersKeys.RIDGES_WEIRD),
                1,
                100000,
                multiply(-0.5, add(3, rf)),
                rangeChoice(
                    flatCacheNoi2D(DuskNoiseParametersKeys.RIDGES_RARE),
                    1,
                    100000,
                    multiply(0.5, add(3, rf)),
                    rangeChoice(
                        flatCacheNoi2D(DuskNoiseParametersKeys.RIDGES_ALT),
                        0,
                        100000,
                        multiply(-0.5, add(1, rf)),
                        multiply(0.5, add(1, rf))
                    )
                )
            )
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
                        this.dense(NoiseRouterData.RIDGES_FOLDED)
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
                    this.dense(NoiseRouterData.RIDGES_FOLDED),
                    add(
                        -grandCanyonBias,
                        multiply(
                            grandCanyonShifter,
                            add(
                                grandCanyonBias,
                                this.dense(NoiseRouterData.RIDGES_FOLDED)
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
            noise(this.noiseHold(Noises.AQUIFER_BARRIER), 0.5)
        )
        this.register(
            DuskDensityFunctions.AQU_LAVA,
            noise(this.noiseHold(Noises.AQUIFER_LAVA))
        )
    }

    private fun BootstrapContext<DensityFunction>.shapers(
        amplified: Boolean,
        largeBiome: Boolean,
        caves: Boolean = true
    ) {
        //every single one of these values has to change depending on *amplified* or *largeBiome*
        val continents: ResourceKey<DensityFunction> = DuskDensityFunctions.CONTINENT_ROUTER
        val erosion: ResourceKey<DensityFunction> = DuskDensityFunctions.EROSION_ROUTER
        val depth: ResourceKey<DensityFunction> = DuskDensityFunctions.DEPTH

        val offset: ResourceKey<DensityFunction> = DuskDensityFunctions.OFFSET
        val jaggedness: ResourceKey<DensityFunction> = DuskDensityFunctions.JAGGEDNESS
        val factor: ResourceKey<DensityFunction> = DuskDensityFunctions.FACTOR
        val cheese: ResourceKey<DensityFunction> = DuskDensityFunctions.SLOPED_CHEESE

        val idwj: ResourceKey<DensityFunction> = DuskDensityFunctions.OVERWORLD_IDWJ
        val finalDensity: ResourceKey<DensityFunction> = DuskDensityFunctions.OVERWORLD_FINAL_DENSITY

        val aquiferFloodedness: ResourceKey<DensityFunction> = DuskDensityFunctions.AQU_FLOODEDNESS
        val aquiferFluidSpread: ResourceKey<DensityFunction> = DuskDensityFunctions.AQU_FLUID_SPREAD

        val urCondition: ResourceKey<DensityFunction> = DuskDensityFunctions.UR_CONDITION
        val urDensity: ResourceKey<DensityFunction> = DuskDensityFunctions.UR_DENSITY

        //val caveLakeCondition: RegistryKey<DensityFunction> = DuskDensityFunctions.LAKE_CAVE_CONDITION
        //val caveLakeAquifer: RegistryKey<DensityFunction> = DuskDensityFunctions.LAKE_CAVE_AQUIFER
        //val caveLakeDensity: RegistryKey<DensityFunction> = DuskDensityFunctions.LAKE_CAVE_DENSITY

        val data = OverworldTerrainCreator.TerrainParametersData(
            this.wrap(continents),
            this.wrap(erosion),
            this.wrap(NoiseRouterData.RIDGES),
            this.wrap(NoiseRouterData.RIDGES_FOLDED),
            this.wrap(DuskDensityFunctions.PLATEAU_TYPE),
            this.wrap(DuskDensityFunctions.GRAND_CANYON_RIDGES_FOLDED),
            this.wrap(DuskDensityFunctions.FLATS_TYPE),
            this.wrap(DuskDensityFunctions.FLATS_ELEV)
        )

        this.caveRiver(data, urCondition, urDensity)
        //this.caveLake(caveLakeCondition, caveLakeAquifer, caveLakeDensity)
        this.register(
            aquiferFloodedness,
            this.everyAquiferModifier(
                noise(this.noiseHold(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67),
                1,
                urCondition
            )
        )
        this.register(
            aquiferFluidSpread,
            this.everyAquiferModifier(
                noise(this.noiseHold(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857),
                0.5,
                urCondition
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

        val jaggednessFunction = noise(this.noiseHold(Noises.JAGGED), 1500.0, 0.0)
        val jagged = multiply(this.dense(jaggedness), jaggednessFunction.halfNegative())
        //val jaggednessAndExtra = add(jagged, this.dense(DuskDensityFunctions.STONE_TOWERS_TOWER))
        this.register(
            cheese,
            NoiseRouterData.noiseGradientDensity(this.dense(factor), add(this.dense(depth), jagged))
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
                    this.dense(NoiseRouterData.ENTRANCES)
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
                    this.dense(NoiseRouterData.NOODLE)
                )
            )
        } else {
            this.register(
                finalDensity,
                multiply(
                    0.64,
                    min(
                        this.dense(urDensity),
                        interpolated(
                            blendDensity(surfaceSlide(amplified, this.dense(cheese)))
                        )
                    )
                ).squeeze()
            )
        }
    }

    private fun BootstrapContext<DensityFunction>.everyAquiferModifier(
        aquifer: DensityFunction,
        conditionValue: Number,
        vararg functions: ResourceKey<DensityFunction>
    ): DensityFunction {
        var retorn: DensityFunction = aquifer
        functions.forEachIndexed { idx, it ->
            retorn = rangeChoice(
                this.dense(it),
                0.5,
                1.1,
                const(conditionValue),
                retorn
            )
        }
        return retorn
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
        val stoneTowersSpline = CubicSpline.builder(this.wrap(DuskDensityFunctions.STONE_TOWERS))
            .add(0.3f, 0f)
            .addPoint(0.45f, 0.2f, 2f)
            .addPoint(0.5f, 0.5f, 0.5f)
        this.register(
            DuskDensityFunctions.STONE_TOWERS_TOWER,
            copySpline(
                CubicSpline.builder(this.wrap(NoiseRouterData.RIDGES_FOLDED))
                    .add(-0.8f, 0f)
                    .add(-0.6f, stoneTowersSpline.build())
                    .build()
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

    fun BootstrapContext<DensityFunction>.wrap(df: ResourceKey<DensityFunction>): DensityFunctions.Spline.Coordinate =
        DensityFunctions.Spline.Coordinate(this.denseHold(df))


    private fun BootstrapContext<DensityFunction>.noi2D(
        noise: ResourceKey<NormalNoise.NoiseParameters>,
        scaleXZ: Double = 0.25
    ): DensityFunction =
        shiftedNoise2d(
            const(0),
            const(0),
            scaleXZ,
            this.noiseHold(noise)
        )

    fun BootstrapContext<DensityFunction>.flatCacheNoi2D(
        noise: ResourceKey<NormalNoise.NoiseParameters>,
        scaleXZ: Double = 0.25
    ): DensityFunction = flatCache(cache2D(this.noi2D(noise, scaleXZ)))


    fun BootstrapContext<NoiseGeneratorSettings>.overworld(largeBiome: Boolean, amplified: Boolean): NoiseRouter {
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
                this.dense(NoiseRouterData.ENTRANCES)
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
            ), this.dense(NoiseRouterData.NOODLE)
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
                else NoiseRouterData.EROSION
            ),
            depth,
            this.dense(NoiseRouterData.RIDGES),
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
                NoiseRouterData.getFunction(densityFunction, NoiseRouterData.ENTRANCES)
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
            ), NoiseRouterData.getFunction(densityFunction, NoiseRouterData.NOODLE)
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
                if (largeBiome) NoiseRouterData.EROSION_OVERWORLD_LARGE_BIOME else NoiseRouterData.EROSION
            ),
            depth,
            NoiseRouterData.getFunction(densityFunction, NoiseRouterData.RIDGES),
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