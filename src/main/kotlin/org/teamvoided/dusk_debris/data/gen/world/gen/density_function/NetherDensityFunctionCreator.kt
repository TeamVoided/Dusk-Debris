package org.teamvoided.dusk_debris.data.gen.world.gen.density_function

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.Direction
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.denseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.floor
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noise
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.registerAndWrap
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.world.gen.density_functions.DebugAxis
import org.teamvoided.dusk_debris.world.gen.density_functions.Fold
import org.teamvoided.dusk_debris.world.gen.density_functions.ShiftedNoiseRange
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.NetherTerrainParametersCreator

object NetherDensityFunctionCreator {
    private val debug: String? = "erosion"
    private const val LAVA = false
    private const val RANGES_DEBUG_WIDTH = 200
    private const val DEBUG_WIDTH = 200
    fun BootstrapContext<DensityFunction>.theNetherCreator() {
        val noiseParameters = this.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)
        val densityFunctions = this.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION)
        this.parameters()
        this.shapers()
        this.pillar()

        this.register(
            DuskDensityFunctions.NETHER_FINAL_DENSITY,
            DensityFunctions.interpolated(
                DensityFunctions.blendDensity(
                    this.createNetherFinalDensity(
                        false,
                        false,
                        0,
                        256
                    )
                )
            ).squeeze()
        )
    }

    fun BootstrapContext<DensityFunction>.parameters() {
        val shiftX = this.dense(NoiseRouterData.SHIFT_X)
        val shiftZ = this.dense(NoiseRouterData.SHIFT_Z)

        this.register(
            DuskDensityFunctions.LAVA_LEVEL,
            DensityFunctions.cacheOnce(
                if (LAVA) {
                    DensityFunctions.max(
                        DensityFunctions.multiply(
                            DensityFunctions.multiply(
                                DensityFunctions.constant(32.0 / 4.0),
                                DensityFunctions.mapFromUnitToValue(
                                    DensityFunctions.noise(
                                        this.noiseHold(DuskNoiseParametersKeys.LAVA_LEVEL),
                                        1.0,
                                        0.0,
                                    ),
                                    -0.25,
                                    1.0
                                )
                            ).floor(),
                            DensityFunctions.constant(4.0)
                        ),
                        DensityFunctions.zero()
                    )
                } else {
                    DensityFunctions.zero()
                }
            )
        )

        this.register(
            DuskDensityFunctions.TEMPERATURE_NETHER,
            DensityFunctions.cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    DensityFunctions.zero(),
                    shiftZ,
                    0.25,
                    0.25,
                    this.noise(DuskNoiseParametersKeys.TEMPERATURE_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.HUMIDITY_NETHER,
            DensityFunctions.cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    DensityFunctions.zero(),
                    shiftZ,
                    0.25,
                    0.25,
                    this.noise(DuskNoiseParametersKeys.VEGETATION_NETHER)
                )
            )
        )
        this.register(
            DuskDensityFunctions.CONTINENTALNESS_NETHER,
            DensityFunctions.cacheOnce(
                if (debug != null) {
                    if (debug == "continent")
                        DebugAxis(Direction.Axis.Z, DEBUG_WIDTH)
                    else
                        DensityFunctions.constant(0.25)
                } else {
                    shiftedNoiseRangeNether(
                        shiftX,
                        DensityFunctions.zero(),
                        shiftZ,
                        (1.0 / 3),
                        0.025,
                        this.noise(DuskNoiseParametersKeys.CONTINENTALNESS_NETHER)
                    )
                }
            )
        )
        this.register(
            DuskDensityFunctions.EROSION_NETHER,
            DensityFunctions.cacheOnce(
                if (debug != null) {
                    if (debug == "erosion")
                        DebugAxis(Direction.Axis.Z, DEBUG_WIDTH)
                    else
                        DensityFunctions.zero()
                } else {
                    shiftedNoiseRangeNether(
                        shiftX,
                        DensityFunctions.zero(),
                        shiftZ,
                        0.25,
                        0.025,
                        this.noise(DuskNoiseParametersKeys.EROSION_NETHER)
                    )
                }
            )
        )
        this.register(
            DuskDensityFunctions.DROP_CEILING,
            DensityFunctions.cacheOnce(
                shiftedNoiseRangeNether(
                    shiftX,
                    DensityFunctions.zero(),
                    shiftZ,
                    0.25,
                    0.1,
                    this.noise(DuskNoiseParametersKeys.DROP_CEILING)
                )
            )
        )
        this.register(
            DuskDensityFunctions.RIDGES_NETHER,
            DensityFunctions.cacheOnce(
                if (debug != null) {
                    DebugAxis(Direction.Axis.X, RANGES_DEBUG_WIDTH * 2)
                } else {
                    shiftedNoiseRangeNether(
                        shiftX,
                        DensityFunctions.zero(),
                        shiftZ,
                        0.25,
                        0.0075,
                        this.noise(DuskNoiseParametersKeys.RIDGE_NETHER),
                    )
                }
            )
        )
        this.register(
            DuskDensityFunctions.RIDGES_FOLDED_NETHER,
            DensityFunctions.cacheOnce(
                if (debug != null) {
                    DebugAxis(Direction.Axis.X, RANGES_DEBUG_WIDTH)
                } else {
                    Fold(this.dense(DuskDensityFunctions.RIDGES_NETHER))
                }
            )
        )
        this.register(
            DuskDensityFunctions.DEPTH_FLOOR_NETHER,
            DensityFunctions.cacheOnce(
                DensityFunctions.add(
                    this.dense(DuskDensityFunctions.OFFSET_FLOOR_NETHER),
                    DensityFunctions.clampedGradientY(
                        -(256 + 256),
                        (256 + 256),
                        2.0,
                        -2.0
                    ),
                )
            )
        )
        this.register(
            DuskDensityFunctions.DEPTH_CEILING_NETHER,
            DensityFunctions.cacheOnce(
                DensityFunctions.add(
                    this.dense(DuskDensityFunctions.OFFSET_CEILING_NETHER),
                    DensityFunctions.clampedGradientY(
                        -(0 + 256),
                        512 + 256,
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
            DensityFunctions.noise(this.noiseHold(NoiseParametersKeys.JAGGED), 150.0, 15.0)
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
        val pillarNoise = DensityFunctions.noise(this.noiseHold(NoiseParametersKeys.PILLAR), 5.0, 0.1)
        val pillarRarenessNoise =
            DensityFunctions.mappedNoise(this.noiseHold(NoiseParametersKeys.PILLAR_RARENESS), 1.0, 0.1, 0.0, -2.0)
        val pillarThicknessNoise =
            DensityFunctions.mappedNoise(this.noiseHold(NoiseParametersKeys.PILLAR_THICKNESS), 0.75, 0.25, 0.5, 1.25)
        val pillar = DensityFunctions.add(
            DensityFunctions.multiply(pillarNoise, DensityFunctions.constant(2.0)),
            pillarRarenessNoise
        )
        this.register(
            DuskDensityFunctions.NETHER_PILLARS,
            DensityFunctions.cacheOnce(DensityFunctions.multiply(pillar, pillarThicknessNoise.cube()))
        )
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
        val continents = DensityFunctions.Spline.FunctionWrapper(continentsKey)
        val erosion = DensityFunctions.Spline.FunctionWrapper(erosionKey)
        val dropCeiling = DensityFunctions.Spline.FunctionWrapper(dropCeilingKey)
        val ridges = DensityFunctions.Spline.FunctionWrapper(this.denseHold(DuskDensityFunctions.RIDGES_NETHER))
        val ridgesFolded =
            DensityFunctions.Spline.FunctionWrapper(this.denseHold(DuskDensityFunctions.RIDGES_FOLDED_NETHER))
        val offsetFloorSpline = registerAndWrap(
            offsetFloorKey,
//            add(
//                constant(0.125),
//                copySpline(
//                  NetherTerrainParametersCreator.offsetFloorSpline(/
//                      continents,
//                      erosion,
//                      ridgesFolded,
//                      ridges,
//                      amplified
//                  )
//                )
            DensityFunctions.add(
                DensityFunctions.constant(0.15),
                DensityFunctions.copySpline(
                    VanillaTerrainParametersCreator.method_42056(
                        continents,
                        erosion,
                        ridgesFolded,
                        amplified
                    )
                )
            )
        )
        val offsetCeilingSpline = registerAndWrap(
            offsetCeilingKey,
            DensityFunctions.add(
                DensityFunctions.constant(0.125),
                DensityFunctions.copySpline(
                    NetherTerrainParametersCreator.offsetCeilingSpline(
                        continents,
                        erosion,
                        ridgesFolded,
                        dropCeiling,
                        amplified
                    )
                )
            )
        )
        val factorSpline = registerAndWrap(
            factorKey,
            DensityFunctions.copySpline(
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
            DensityFunctions.max(
                this.dense(DuskDensityFunctions.DEPTH_FLOOR_NETHER),
                this.dense(DuskDensityFunctions.DEPTH_CEILING_NETHER)
            )
        )

        val jaggednessSpline = this.registerAndWrap(
            jaggednessKey,
            DensityFunctions.copySpline(
                NetherTerrainParametersCreator.jaggednessSpline(
                    continents,
                    erosion,
                    ridges,
                    ridgesFolded,
                    DensityFunctions.Spline.FunctionWrapper(jaggedFunction),
                    amplified
                )
            )
        )
        val jaggednessFunction = DensityFunctions.noise(this.noiseHold(NoiseParametersKeys.JAGGED), 1500.0, 150.0)
        val jagged = DensityFunctions.multiply(jaggednessSpline, jaggednessFunction.halfNegative())
        val depthAndJaggedness = NoiseRouterData.noiseGradientDensity(
            factorSpline,
            DensityFunctions.add(depthFunction, jagged)
//            if (debug != null) {
//                add(depthFunction, jagged)
//            } else {
//                max(
//                    add(depthFunction, jagged),
//                    maxRangeChoice(this.dense(DuskDensityFunctions.NETHER_PILLARS), 0.03)
//                )
//            }
        )
        this.register(
            cheeseKey,
            depthAndJaggedness
//            if (debug != null) {
//                depthAndJaggedness
//            } else {
//                add(depthAndJaggedness, this.dense(NoiseRouterData.BASE_3D_NOISE_NETHER))
//            }
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
            DensityFunctions.zero(),
            this.dense(DuskDensityFunctions.LAVA_LEVEL),
            DensityFunctions.zero(),
            DensityFunctions.constant(6.0),
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
            DensityFunctions.zero(),
            DensityFunctions.zero(),
            DensityFunctions.zero()
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

    private fun shiftedNoiseRangeNether(
        shiftX: DensityFunction,
        shiftY: DensityFunction,
        shiftZ: DensityFunction,
        xzScale: Double,
        yScale: Double,
        noise: DensityFunction.NoiseHolder
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
}