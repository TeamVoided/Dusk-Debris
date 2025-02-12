package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.registry.*
import net.minecraft.util.math.Direction
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.*
import net.minecraft.util.math.noise.InterpolatedNoiseSampler
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.*
import net.minecraft.world.gen.DensityFunctions.*
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.cheeseMaker
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.shapers
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.world.gen.density_functions.DebugAxis
import org.teamvoided.dusk_debris.world.gen.density_functions.Fold
import org.teamvoided.dusk_debris.world.gen.density_functions.ShiftedNoiseRange
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.NetherTerrainParametersCreator

object DensityFunctionCreator {

    fun bootstrap(c: BootstrapContext<DensityFunction>) {
        val noiseParameters = c.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)
        val densityFunctions = c.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION)
        c.register(
            DuskDensityFunctions.EXAMPLE,
            InterpolatedNoiseSampler.createUnseeded(0.25, 0.125, 80.0, 160.0, 8.0)
        )
        c.parameters()
        c.shapers()
        c.pillar()

        c.register(
            DuskDensityFunctions.NETHER_FINAL_DENSITY,
            interpolated(blendDensity(c.createNetherFinalDensity(false, false, 0, 256))).squeeze()
        )
    }

    fun BootstrapContext<DensityFunction>.parameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)

        this.register(
            DuskDensityFunctions.LAVA_LEVEL,
            cacheOnce(
                ShiftedNoise(
                    shiftX,
                    zero(),
                    shiftZ,
                    1.0,
                    0.0,
                    this.noise(DuskNoiseParametersKeys.LAVA_LEVEL)
                )
            )
        )

        this.register(
            DuskDensityFunctions.TEMPERATURE_NETHER,
            cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    zero(),
                    shiftZ,
                    0.25,
                    0.25,
                    this.noise(DuskNoiseParametersKeys.TEMPERATURE_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.HUMIDITY_NETHER,
            cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    zero(),
                    shiftZ,
                    0.25,
                    0.25,
                    this.noise(DuskNoiseParametersKeys.VEGETATION_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.CONTINENTALNESS_NETHER,
            cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    zero(),
                    shiftZ,
                    (1.0 / 3),
                    0.025,
                    this.noise(DuskNoiseParametersKeys.CONTINENTALNESS_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.EROSION_NETHER,
            cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    zero(),
                    shiftZ,
                    0.25,
                    0.025,
                    this.noise(DuskNoiseParametersKeys.EROSION_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.DROP_CEILING,
            cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    zero(),
                    shiftZ,
                    0.25,
                    0.1,
                    this.noise(DuskNoiseParametersKeys.DROP_CEILING)
                )
            )
        )
        this.register(
            DuskDensityFunctions.RIDGES_NETHER,
            cacheOnce(
                multiply(constant(2.0), add(constant(-0.5), DebugAxis(Direction.Axis.X, 100.0)))
//                shiftedNoiseRangeNether(
//                    shiftX,
//                    zero(),
//                    shiftZ,
//                    0.25,
//                    0.0075,
//                    this.noise(DuskNoiseParametersKeys.RIDGE_NETHER),
//                )
            )
        )
        this.register(
            DuskDensityFunctions.RIDGES_FOLDED_NETHER,
            add(this.dense(DuskDensityFunctions.RIDGES_NETHER), constant(0.0))
            //Fold(this.dense(DuskDensityFunctions.RIDGES_NETHER))
        )
        this.register(
            DuskDensityFunctions.DEPTH_FLOOR_NETHER,
            cacheOnce(
                add(
                    this.dense(DuskDensityFunctions.OFFSET_FLOOR_NETHER),
                    clampedGradientY(
                        -256 - 256 + 32,
                        (256 + 256) + 32,
                        2.0,
                        -2.0
                    ),
                )
            )
        )
        this.register(
            DuskDensityFunctions.DEPTH_CEILING_NETHER,
            cacheOnce(
                add(
                    this.dense(DuskDensityFunctions.OFFSET_CEILING_NETHER),
                    clampedGradientY(
                        0 - 256 - 32,
                        512 + 256 - 32,
                        -2.0,
                        2.0
                    ),
                )
            )
        )
    }

    fun BootstrapContext<DensityFunction>.shapers() {
        val continents = this.denseHold(DuskDensityFunctions.CONTINENTALNESS_NETHER)
        val erosion = this.denseHold(DuskDensityFunctions.EROSION_NETHER)
        val dropCeiling = this.denseHold(DuskDensityFunctions.DROP_CEILING)
//            val continentsLarge = densityFunctions.getHolderOrThrow(DuskDensityFunctions.CONTINENTALNESS_NETHER_LARGE_BIOME)
//            val erosionLarge = densityFunctions.getHolderOrThrow(EROSION_NETHER_LARGE_BIOME)
//            val dropCeilingLarge = densityFunctions.getHolderOrThrow(DROP_CEILING_LARGE_BIOME)


        val jaggedParameterFunction = this.register(
            DuskDensityFunctions.JAGGED_PARAMETER_NETHER,
            noise(this.noiseHold(NoiseParametersKeys.JAGGED), 150.0, 15.0)
        )
        this.cheeseMaker(
            continents,
            erosion,
            dropCeiling,
            jaggedParameterFunction,
            DuskDensityFunctions.OFFSET_FLOOR_NETHER,
            DuskDensityFunctions.OFFSET_CEILING_NETHER,
            DuskDensityFunctions.OFFSET_NETHER,
            DuskDensityFunctions.FACTOR_NETHER,
            DuskDensityFunctions.JAGGEDNESS_NETHER,
            DuskDensityFunctions.DEPTH_NETHER,
            DuskDensityFunctions.SLOPED_CHEESE_NETHER,
            false
        )
//            cheeseMaker(
//                c,
//                densityFunctions,
//                jaggedness,
//                continentsLarge,
//                erosionLarge,
//                dropCeilingLarge,
//                OFFSET_FLOOR_NETHER_LARGE_BIOME,
//                OFFSET_CEILING_NETHER_LARGE_BIOME,
//                FACTOR_NETHER_LARGE_BIOME,
//                JAGGEDNESS_NETHER_LARGE_BIOME,
//                DEPTH_NETHER_LARGE_BIOME,
//                SLOPED_CHEESE_NETHER_LARGE_BIOME,
//                false
//            )
//            cheeseMaker(
//                c,
//                densityFunctions,
//                jaggedness,
//                continents,
//                erosion,
//                dropCeiling,
//                OFFSET_FLOOR_NETHER_AMPLIFIED,
//                OFFSET_CEILING_NETHER_AMPLIFIED,
//                FACTOR_NETHER_AMPLIFIED,
//                JAGGEDNESS_NETHER_AMPLIFIED,
//                DEPTH_NETHER_AMPLIFIED,
//                SLOPED_CHEESE_NETHER_AMPLIFIED,
//                true
//            )
    }


    fun BootstrapContext<DensityFunction>.pillar() {
        val pillarNoise = noise(this.noiseHold(NoiseParametersKeys.PILLAR), 5.0, 0.1)
        val pillarRarenessNoise = mappedNoise(this.noiseHold(NoiseParametersKeys.PILLAR_RARENESS), 1.0, 0.1, 0.0, -2.0)
        val pillarThicknessNoise =
            mappedNoise(this.noiseHold(NoiseParametersKeys.PILLAR_THICKNESS), 0.75, 0.25, 0.5, 1.25)
        val pillar = add(multiply(pillarNoise, constant(2.0)), pillarRarenessNoise)
        this.register(DuskDensityFunctions.NETHER_PILLARS, cacheOnce(multiply(pillar, pillarThicknessNoise.cube())))
    }

    private fun BootstrapContext<DensityFunction>.cheeseMaker(
        continentsKey: Holder<DensityFunction>,
        erosionKey: Holder<DensityFunction>,
        dropCeilingKey: Holder<DensityFunction>,
        jaggedFunction: Holder<DensityFunction>,
        offsetFloorKey: RegistryKey<DensityFunction>,
        offsetCeilingKey: RegistryKey<DensityFunction>,
        offsetKey: RegistryKey<DensityFunction>,
        factorKey: RegistryKey<DensityFunction>,
        jaggednessKey: RegistryKey<DensityFunction>,
        floorKey: RegistryKey<DensityFunction>,
        cheeseKey: RegistryKey<DensityFunction>,
        amplified: Boolean
    ) {
        val continents = Spline.FunctionWrapper(continentsKey)
        val erosion = Spline.FunctionWrapper(erosionKey)
        val dropCeiling = Spline.FunctionWrapper(dropCeilingKey)
        val ridges = Spline.FunctionWrapper(this.denseHold(DuskDensityFunctions.RIDGES_NETHER))
        val ridgesFolded = Spline.FunctionWrapper(this.denseHold(DuskDensityFunctions.RIDGES_FOLDED_NETHER))
        val offsetFloorSpline = registerAndWrap(
            offsetFloorKey,
            copySpline(
                NetherTerrainParametersCreator.offsetFloorSpline(
                    continents,
                    erosion,
                    ridgesFolded,
                    amplified
                )
            )
        )
        val offsetCeilingSpline = registerAndWrap(
            offsetCeilingKey,
            copySpline(
                NetherTerrainParametersCreator.offsetCeilingSpline(
                    continents,
                    erosion,
                    ridgesFolded,
                    dropCeiling,
                    amplified
                )
            )
        )
        val factorSpline = registerAndWrap(
            factorKey,
            copySpline(
                NetherTerrainParametersCreator.factorSpline(
                    continents,
                    erosion,
                    ridges,
                    ridgesFolded,
                    amplified
                )
            )
        )
        val depthFunction = this.registerAndWrap(
            floorKey,
            max(
                this.dense(DuskDensityFunctions.DEPTH_FLOOR_NETHER),
                this.dense(DuskDensityFunctions.DEPTH_CEILING_NETHER)
            )
        )

        val jaggednessSpline = this.registerAndWrap(
            jaggednessKey,
            copySpline(
                NetherTerrainParametersCreator.jaggednessSpline(
                    continents,
                    erosion,
                    ridges,
                    ridgesFolded,
                    Spline.FunctionWrapper(jaggedFunction),
                    amplified
                )
            )
        )
        val jaggednessFunction = noise(this.noiseHold(NoiseParametersKeys.JAGGED), 1500.0, 150.0)
        val jagged = multiply(jaggednessSpline, jaggednessFunction.quarterNegative())
        val depthAndJaggedness = NoiseRouterData.noiseGradientDensity(
            factorSpline,
//            max(
            add(depthFunction, jagged),
//                maxRangeChoice(this.dense(DuskDensityFunctions.NETHER_PILLARS), 0.03)
//            )
        )
        this.register(
            cheeseKey,
//            add(
            depthAndJaggedness
//                , this.dense(NoiseRouterData.BASE_3D_NOISE_NETHER))
        )
    }

    fun BootstrapContext<*>.createNether(
        amplified: Boolean,
        largeBiome: Boolean
    ): NoiseRouter {
        return this.netherNoiseRouter(
            amplified,
            largeBiome,
            this.dense(DuskDensityFunctions.NETHER_FINAL_DENSITY)
        )
    }

    private fun BootstrapContext<*>.netherNoiseRouter(
        amplified: Boolean,
        largeBiome: Boolean,
        finalDensity: DensityFunction
    ): NoiseRouter {
        return NoiseRouter(
            zero(),
            zero(),//  this.dense(DuskDensityFunctions.LAVA_LEVEL),
            zero(),
            constant(6.0),
            this.dense(
                //if (largeBiome) DuskDensityFunctions.TEMPERATURE_NETHER_LARGE_BIOME else
                DuskDensityFunctions.TEMPERATURE_NETHER
            ),
            this.dense(
                //if (largeBiome) DuskDensityFunctions.HUMIDITY_NETHER_LARGE_BIOME else
                DuskDensityFunctions.HUMIDITY_NETHER
            ),
            this.dense(
                //if (largeBiome) DuskDensityFunctions.CONTINENTALNESS_NETHER_LARGE_BIOME else
                DuskDensityFunctions.CONTINENTALNESS_NETHER
            ),
            this.dense(
                //if (largeBiome) DuskDensityFunctions.EROSION_NETHER_LARGE_BIOME else
                DuskDensityFunctions.EROSION_NETHER
            ),
            this.dense(
                //if (largeBiome) DuskDensityFunctions.DEPTH_NETHER_LARGE_BIOME else
                //   if (amplified) DuskDensityFunctions.DEPTH_NETHER_AMPLIFIED else
                DuskDensityFunctions.DEPTH_NETHER
            ),
            this.dense(DuskDensityFunctions.RIDGES_FOLDED_NETHER),
            this.dense(DuskDensityFunctions.DEPTH_FLOOR_NETHER),
            finalDensity,
            zero(),
            zero(),
            zero()
        )
    }

    private fun BootstrapContext<DensityFunction>.createNetherFinalDensity(
        amplified: Boolean,
        largeBiome: Boolean,
        minHeight: Int,
        maxHeight: Int
    ): DensityFunction {
        return NoiseRouterData.slide(
            this.dense(DuskDensityFunctions.SLOPED_CHEESE_NETHER),
            minHeight,
            maxHeight,
            24,
            0,
            0.9375,
            -8,
            24,
            2.5
        )
    }


    private fun BootstrapContext<*>.noise(noi: RegistryKey<NoiseParameters>): NoiseHolder =
        NoiseHolder(this.noiseHold(noi))

    private fun BootstrapContext<*>.noiseHold(noi: RegistryKey<NoiseParameters>): Holder.Reference<NoiseParameters> =
        this.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS).getHolderOrThrow(noi)

    private fun BootstrapContext<*>.dense(den: RegistryKey<DensityFunction>): DensityFunction =
        HolderHolder(this.denseHold(den))

    private fun BootstrapContext<*>.denseHold(noi: RegistryKey<DensityFunction>): Holder.Reference<DensityFunction> =
        this.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION).getHolderOrThrow(noi)


    private fun BootstrapContext<DensityFunction>.registerAndWrap(
        registryKey: RegistryKey<DensityFunction>,
        den: DensityFunction
    ): DensityFunction {
        return HolderHolder(this.register(registryKey, den))
    }

    private fun shiftedNoiseRangeNether(
        shiftX: DensityFunction,
        shiftY: DensityFunction,
        shiftZ: DensityFunction,
        xzScale: Double,
        yScale: Double,
        noise: NoiseHolder
    ): ShiftedNoiseRange = ShiftedNoiseRange(
        shiftX,
        shiftY,
        shiftZ,
        xzScale,
        yScale,
        0.0,
        256.0,
        noise
    )

    fun maxRangeChoice(input: DensityFunction, maxInclusive: Double): DensityFunction {
        val minInclusive = -1000000.0
        return rangeChoice(input, minInclusive, maxInclusive, constant(minInclusive), input)
    }


    fun minRangeChoice(input: DensityFunction, minInclusive: Double): DensityFunction {
        val maxInclusive = 1000000.0
        return rangeChoice(input, minInclusive, maxInclusive, constant(maxInclusive), input)
    }

//    NoiseRouterData.class
}
