package org.teamvoided.dusk_debris.data.gen.world.gen.dimension.reference

import com.mojang.datafixers.util.Pair
import net.minecraft.SharedConstants
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.util.VisibleForDebug
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.NoiseRouterData
import java.util.function.Consumer

class OverworldBiomeParameters {
    private val fullRange: Climate.Parameter = Climate.Parameter.span(-1.0f, 1.0f)

    @get:VisibleForDebug
    val temperatureThresholds: Array<Climate.Parameter> = arrayOf(
        Climate.Parameter.span(-1.0f, -0.45f),
        Climate.Parameter.span(-0.45f, -0.15f),
        Climate.Parameter.span(-0.15f, 0.2f),
        Climate.Parameter.span(0.2f, 0.55f),
        Climate.Parameter.span(0.55f, 1.0f)
    )

    @get:VisibleForDebug
    val humidityThresholds: Array<Climate.Parameter> = arrayOf(
        Climate.Parameter.span(-1.0f, -0.35f),
        Climate.Parameter.span(-0.35f, -0.1f),
        Climate.Parameter.span(-0.1f, 0.1f),
        Climate.Parameter.span(0.1f, 0.3f),
        Climate.Parameter.span(0.3f, 1.0f)
    )

    @get:VisibleForDebug
    val erosionThresholds: Array<Climate.Parameter> = arrayOf(
        Climate.Parameter.span(-1.0f, -0.78f),
        Climate.Parameter.span(-0.78f, -0.375f),
        Climate.Parameter.span(-0.375f, -0.2225f),
        Climate.Parameter.span(-0.2225f, 0.05f),
        Climate.Parameter.span(0.05f, 0.45f),
        Climate.Parameter.span(0.45f, 0.55f),
        Climate.Parameter.span(0.55f, 1.0f)
    )
    private val frozenTemperature = temperatureThresholds[0]
    private val unfrozenTemperature: Climate.Parameter = Climate.Parameter.span(
        temperatureThresholds[1],
        temperatureThresholds[4]
    )
    private val mushroomFieldsContinentalness: Climate.Parameter =
        Climate.Parameter.span(-1.2f, -1.05f)
    private val deepOceanContinentalness: Climate.Parameter =
        Climate.Parameter.span(-1.05f, -0.455f)
    private val oceanContinentalness: Climate.Parameter =
        Climate.Parameter.span(-0.455f, -0.19f)
    private val coastContinentalness: Climate.Parameter =
        Climate.Parameter.span(-0.19f, -0.11f)
    private val inlandContinentalness: Climate.Parameter =
        Climate.Parameter.span(-0.11f, 0.55f)
    private val nearInlandContinentalness: Climate.Parameter =
        Climate.Parameter.span(-0.11f, 0.03f)
    private val midInlandContinentalness: Climate.Parameter =
        Climate.Parameter.span(0.03f, 0.3f)
    private val farInlandContinentalness: Climate.Parameter =
        Climate.Parameter.span(0.3f, 1.0f)
    private val oceanBiomes: Array<Array<ResourceKey<Biome>>>
    private val middleBiomes: Array<Array<ResourceKey<Biome>>>
    private val middleBiomesVariant: Array<Array<ResourceKey<Biome>?>>
    private val plateauBiomes: Array<Array<ResourceKey<Biome>>>
    private val plateauBiomesVariant: Array<Array<ResourceKey<Biome>?>>
    private val shatteredBiomes: Array<Array<ResourceKey<Biome>?>>

    init {
        this.oceanBiomes = arrayOf<Array<ResourceKey<Biome>>>(
            arrayOf(
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN,
                Biomes.WARM_OCEAN
            ),
            arrayOf(Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN)
        )
        this.middleBiomes = arrayOf<Array<ResourceKey<Biome>>>(
            arrayOf(Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA),
            arrayOf(Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
            arrayOf(Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST),
            arrayOf(Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE),
            arrayOf(Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT)
        )
        this.middleBiomesVariant = arrayOf<Array<ResourceKey<Biome>?>>(
            arrayOf(Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null),
            arrayOf(null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA),
            arrayOf(Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null),
            arrayOf(null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE),
            arrayOf(null, null, null, null, null)
        )
        this.plateauBiomes = arrayOf<Array<ResourceKey<Biome>>>(
            arrayOf(
                Biomes.SNOWY_PLAINS,
                Biomes.SNOWY_PLAINS,
                Biomes.SNOWY_PLAINS,
                Biomes.SNOWY_TAIGA,
                Biomes.SNOWY_TAIGA
            ),
            arrayOf(Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
            arrayOf(Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST),
            arrayOf(Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE),
            arrayOf(Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS)
        )
        this.plateauBiomesVariant = arrayOf<Array<ResourceKey<Biome>?>>(
            arrayOf(Biomes.ICE_SPIKES, null, null, null, null),
            arrayOf(Biomes.CHERRY_GROVE, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA),
            arrayOf(Biomes.CHERRY_GROVE, Biomes.CHERRY_GROVE, Biomes.FOREST, Biomes.BIRCH_FOREST, null),
            arrayOf(null, null, null, null, null),
            arrayOf(Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null)
        )
        this.shatteredBiomes = arrayOf<Array<ResourceKey<Biome>?>>(
            arrayOf(
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_FOREST
            ), arrayOf(
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_FOREST
            ), arrayOf(
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_HILLS,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_FOREST
            ),
            arrayOf(null, null, null, null, null),
            arrayOf(null, null, null, null, null)
        )
    }

    val spawnSuitabilityNoises: List<Climate.ParameterPoint>
        get() {
            val parameterRange = Climate.Parameter.point(0.0f)
            val f = 0.16f
            return java.util.List.of(
                Climate.ParameterPoint(
                    this.fullRange, this.fullRange, Climate.Parameter.span(
                        this.inlandContinentalness, this.fullRange
                    ), this.fullRange, parameterRange, Climate.Parameter.span(-1.0f, -0.16f), 0L
                ), Climate.ParameterPoint(
                    this.fullRange, this.fullRange, Climate.Parameter.span(
                        this.inlandContinentalness, this.fullRange
                    ), this.fullRange, parameterRange, Climate.Parameter.span(0.16f, 1.0f), 0L
                )
            )
        }

    protected fun addBiomesTo(biomeEntryConsumer: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) {
        if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
            this.addDebugBiomesTo(biomeEntryConsumer)
        } else {
            this.addOffCoastBiomesTo(biomeEntryConsumer)
            this.addInlandBiomesTo(biomeEntryConsumer)
            this.addUndergroundBiomesTo(biomeEntryConsumer)
        }
    }

    private fun HolderGetter<DensityFunction>.get(key: ResourceKey<DensityFunction>): DensityFunctions.Spline.Coordinate =
        DensityFunctions.Spline.Coordinate(this.getOrThrow(key))

    private fun addDebugBiomesTo(biomeEntryConsumer: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) {
        val provider = VanillaRegistries.createLookup()
        val dense: HolderGetter<DensityFunction> = provider.lookupOrThrow(Registries.DENSITY_FUNCTION)
        val continents = dense.getOrThrow(NoiseRouterData.CONTINENTS)
        val erosion = dense.getOrThrow(NoiseRouterData.EROSION)
        val ridgesFolded = dense.getOrThrow(NoiseRouterData.RIDGES_FOLDED)
        biomeEntryConsumer.accept(
            Pair.of(
                Climate.parameters(
                    this.fullRange,
                    this.fullRange,
                    this.fullRange,
                    this.fullRange,
                    Climate.Parameter.point(0.0f),
                    this.fullRange,
                    0.01f
                ),
                Biomes.PLAINS
            )
        )
        // TODO this breaks and i dont want to fix it
       /* val splineEros: CubicSpline<*, *> = TerrainProvider.buildErosionOffsetSpline(
            erosion,
            ridgesFolded,
            -0.15f,
            0.0f,
            0.0f,
            0.1f,
            0.0f,
            -0.03f,
            false,
            false,
            ToFloatFunction.IDENTITY
        )
        if (splineEros is CubicSpline.Multipoint<*, *>) {
            var registryKey = Biomes.DESERT
            splineEros.locations().forEach {
                biomeEntryConsumer.accept(
                    Pair.of(
                        Climate.parameters(
                            this.fullRange,
                            this.fullRange,
                            this.fullRange,
                            Climate.Parameter.point(it),
                            Climate.Parameter.point(0.0f),
                            this.fullRange,
                            0.0f
                        ),
                        registryKey
                    )
                )
                registryKey = if (registryKey == Biomes.DESERT) Biomes.BADLANDS else Biomes.DESERT
            }
        }

        val splineCont: CubicSpline<*, *> =
            TerrainProvider.overworldOffset(continents, erosion, ridgesFolded, false)
        if (splineCont is CubicSpline.Multipoint<*, *>) {
            splineCont.locations().forEach {
                biomeEntryConsumer.accept(
                    Pair.of(
                        Climate.parameters(
                            this.fullRange,
                            this.fullRange,
                            Climate.Parameter.point(it),
                            this.fullRange,
                            Climate.Parameter.point(0.0f),
                            this.fullRange,
                            0.0f
                        ),
                        Biomes.SNOWY_TAIGA
                    )
                )
            }
        }*/
    }

    private fun addOffCoastBiomesTo(parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) {
        this.addSurfaceBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            this.mushroomFieldsContinentalness,
            this.fullRange,
            this.fullRange,
            0.0f,
            Biomes.MUSHROOM_FIELDS
        )

        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]
            this.addSurfaceBiomeTo(
                parameters,
                parameterRange,
                this.fullRange,
                this.deepOceanContinentalness,
                this.fullRange,
                this.fullRange,
                0.0f,
                oceanBiomes[0][i]
            )
            this.addSurfaceBiomeTo(
                parameters,
                parameterRange,
                this.fullRange,
                this.oceanContinentalness,
                this.fullRange,
                this.fullRange,
                0.0f,
                oceanBiomes[1][i]
            )
        }
    }

    private fun addInlandBiomesTo(parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) {
        this.addMidBiomesTo(parameters, Climate.Parameter.span(-1.0f, -0.93333334f))
        this.addHighBiomesTo(parameters, Climate.Parameter.span(-0.93333334f, -0.7666667f))
        this.addPeaksTo(parameters, Climate.Parameter.span(-0.7666667f, -0.56666666f))
        this.addHighBiomesTo(parameters, Climate.Parameter.span(-0.56666666f, -0.4f))
        this.addMidBiomesTo(parameters, Climate.Parameter.span(-0.4f, -0.26666668f))
        this.addLowBiomesTo(parameters, Climate.Parameter.span(-0.26666668f, -0.05f))
        this.addValleysTo(parameters, Climate.Parameter.span(-0.05f, 0.05f))
        this.addLowBiomesTo(parameters, Climate.Parameter.span(0.05f, 0.26666668f))
        this.addMidBiomesTo(parameters, Climate.Parameter.span(0.26666668f, 0.4f))
        this.addHighBiomesTo(parameters, Climate.Parameter.span(0.4f, 0.56666666f))
        this.addPeaksTo(parameters, Climate.Parameter.span(0.56666666f, 0.7666667f))
        this.addHighBiomesTo(parameters, Climate.Parameter.span(0.7666667f, 0.93333334f))
        this.addMidBiomesTo(parameters, Climate.Parameter.span(0.93333334f, 1.0f))
    }

    private fun addPeaksTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        weirdness: Climate.Parameter
    ) {
        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]

            for (j in humidityThresholds.indices) {
                val parameterRange2 = humidityThresholds[j]
                val registryKey = this.pickRegularBiome(i, j, weirdness)
                val registryKey2 = this.pickRegularBiomeOrBadlandsIfHot(i, j, weirdness)
                val registryKey3 = this.pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness)
                val registryKey4 = this.pickPlateauBiome(i, j, weirdness)
                val registryKey5 = this.pickShatteredBiome(i, j, weirdness)
                val registryKey6 = this.maybePickWindsweptSavanna(i, j, weirdness, registryKey5)
                val registryKey7 = this.pickPeakBiome(i, j, weirdness)
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ), Climate.Parameter.span(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[2], weirdness, 0.0f, registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.midInlandContinentalness,
                    erosionThresholds[3], weirdness, 0.0f, registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.farInlandContinentalness,
                    erosionThresholds[3], weirdness, 0.0f, registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[4], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey6
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[6], weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addHighBiomesTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        weirdness: Climate.Parameter
    ) {
        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]

            for (j in humidityThresholds.indices) {
                val parameterRange2 = humidityThresholds[j]
                val registryKey = this.pickRegularBiome(i, j, weirdness)
                val registryKey2 = this.pickRegularBiomeOrBadlandsIfHot(i, j, weirdness)
                val registryKey3 = this.pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness)
                val registryKey4 = this.pickPlateauBiome(i, j, weirdness)
                val registryKey5 = this.pickShatteredBiome(i, j, weirdness)
                val registryKey6 = this.maybePickWindsweptSavanna(i, j, weirdness, registryKey)
                val registryKey7 = this.pickSlopeBiome(i, j, weirdness)
                val registryKey8 = this.pickPeakBiome(i, j, weirdness)
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.coastContinentalness,
                    Climate.Parameter.span(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ),
                    weirdness,
                    0.0f,
                    registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[0], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey8
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[1], weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ), Climate.Parameter.span(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[2], weirdness, 0.0f, registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.midInlandContinentalness,
                    erosionThresholds[3], weirdness, 0.0f, registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.farInlandContinentalness,
                    erosionThresholds[3], weirdness, 0.0f, registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[4], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey6
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[6], weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addMidBiomesTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        weirdness: Climate.Parameter
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            this.coastContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[2]
            ),
            weirdness,
            0.0f,
            Biomes.STONY_SHORE
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.MANGROVE_SWAMP
        )

        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]

            for (j in humidityThresholds.indices) {
                val parameterRange2 = humidityThresholds[j]
                val registryKey = this.pickRegularBiome(i, j, weirdness)
                val registryKey2 = this.pickRegularBiomeOrBadlandsIfHot(i, j, weirdness)
                val registryKey3 = this.pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness)
                val registryKey4 = this.pickShatteredBiome(i, j, weirdness)
                val registryKey5 = this.pickPlateauBiome(i, j, weirdness)
                val registryKey6 = this.pickBeachBiome(i, j)
                val registryKey7 = this.maybePickWindsweptSavanna(i, j, weirdness, registryKey)
                val registryKey8 = this.pickShatteredCoastBiome(i, j, weirdness)
                val registryKey9 = this.pickSlopeBiome(i, j, weirdness)
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.nearInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey9
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.nearInlandContinentalness, this.midInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.farInlandContinentalness,
                    erosionThresholds[1], weirdness, 0.0f, if (i == 0) registryKey9 else registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[2], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.midInlandContinentalness,
                    erosionThresholds[2], weirdness, 0.0f, registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.farInlandContinentalness,
                    erosionThresholds[2], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[3], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[3], weirdness, 0.0f, registryKey2
                )
                if (weirdness.max() < 0L) {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, this.coastContinentalness,
                        erosionThresholds[4], weirdness, 0.0f, registryKey6
                    )
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[4], weirdness, 0.0f, registryKey
                    )
                } else {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                            this.coastContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[4], weirdness, 0.0f, registryKey
                    )
                }

                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.coastContinentalness,
                    erosionThresholds[5], weirdness, 0.0f, registryKey8
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[5], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey4
                )
                if (weirdness.max() < 0L) {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, this.coastContinentalness,
                        erosionThresholds[6], weirdness, 0.0f, registryKey6
                    )
                } else {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, this.coastContinentalness,
                        erosionThresholds[6], weirdness, 0.0f, registryKey
                    )
                }

                if (i == 0) {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[6], weirdness, 0.0f, registryKey
                    )
                }
            }
        }
    }

    private fun addLowBiomesTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        weirdness: Climate.Parameter
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            this.coastContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[2]
            ),
            weirdness,
            0.0f,
            Biomes.STONY_SHORE
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.MANGROVE_SWAMP
        )

        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]

            for (j in humidityThresholds.indices) {
                val parameterRange2 = humidityThresholds[j]
                val registryKey = this.pickRegularBiome(i, j, weirdness)
                val registryKey2 = this.pickRegularBiomeOrBadlandsIfHot(i, j, weirdness)
                val registryKey3 = this.pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness)
                val registryKey4 = this.pickBeachBiome(i, j)
                val registryKey5 = this.maybePickWindsweptSavanna(i, j, weirdness, registryKey)
                val registryKey6 = this.pickShatteredCoastBiome(i, j, weirdness)
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.nearInlandContinentalness,
                    Climate.Parameter.span(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ),
                    weirdness,
                    0.0f,
                    registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), Climate.Parameter.span(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ), weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.nearInlandContinentalness,
                    Climate.Parameter.span(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ),
                    weirdness,
                    0.0f,
                    registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), Climate.Parameter.span(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.coastContinentalness,
                    Climate.Parameter.span(
                        erosionThresholds[3],
                        erosionThresholds[4]
                    ),
                    weirdness,
                    0.0f,
                    registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.nearInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[4], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.coastContinentalness,
                    erosionThresholds[5], weirdness, 0.0f, registryKey6
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[5], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.coastContinentalness,
                    erosionThresholds[6], weirdness, 0.0f, registryKey4
                )
                if (i == 0) {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[6], weirdness, 0.0f, registryKey
                    )
                }
            }
        }
    }

    private fun addValleysTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        weirdness: Climate.Parameter
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.frozenTemperature,
            this.fullRange,
            this.coastContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[1]
            ),
            weirdness,
            0.0f,
            if (weirdness.max() < 0L) Biomes.STONY_SHORE else Biomes.FROZEN_RIVER
        )
        this.addSurfaceBiomeTo(
            parameters,
            this.unfrozenTemperature,
            this.fullRange,
            this.coastContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[1]
            ),
            weirdness,
            0.0f,
            if (weirdness.max() < 0L) Biomes.STONY_SHORE else Biomes.RIVER
        )
        this.addSurfaceBiomeTo(
            parameters,
            this.frozenTemperature,
            this.fullRange,
            this.nearInlandContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[1]
            ),
            weirdness,
            0.0f,
            Biomes.FROZEN_RIVER
        )
        this.addSurfaceBiomeTo(
            parameters,
            this.unfrozenTemperature,
            this.fullRange,
            this.nearInlandContinentalness,
            Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[1]
            ),
            weirdness,
            0.0f,
            Biomes.RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.frozenTemperature, this.fullRange, Climate.Parameter.span(
                this.coastContinentalness, this.farInlandContinentalness
            ), Climate.Parameter.span(
                erosionThresholds[2],
                erosionThresholds[5]
            ), weirdness, 0.0f, Biomes.FROZEN_RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.unfrozenTemperature, this.fullRange, Climate.Parameter.span(
                this.coastContinentalness, this.farInlandContinentalness
            ), Climate.Parameter.span(
                erosionThresholds[2],
                erosionThresholds[5]
            ), weirdness, 0.0f, Biomes.RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.frozenTemperature, this.fullRange, this.coastContinentalness,
            erosionThresholds[6], weirdness, 0.0f, Biomes.FROZEN_RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.unfrozenTemperature, this.fullRange, this.coastContinentalness,
            erosionThresholds[6], weirdness, 0.0f, Biomes.RIVER
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            Climate.Parameter.span(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.MANGROVE_SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters, this.frozenTemperature, this.fullRange, Climate.Parameter.span(
                this.inlandContinentalness, this.farInlandContinentalness
            ),
            erosionThresholds[6], weirdness, 0.0f, Biomes.FROZEN_RIVER
        )

        for (i in temperatureThresholds.indices) {
            val parameterRange = temperatureThresholds[i]

            for (j in humidityThresholds.indices) {
                val parameterRange2 = humidityThresholds[j]
                val registryKey = this.pickRegularBiomeOrBadlandsIfHot(i, j, weirdness)
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, Climate.Parameter.span(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), Climate.Parameter.span(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ), weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addUndergroundBiomesTo(parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) {
        this.addUndergroundBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            Climate.Parameter.span(0.8f, 1.0f),
            this.fullRange,
            this.fullRange,
            0.0f,
            Biomes.DRIPSTONE_CAVES
        )
        this.addUndergroundBiomeTo(
            parameters,
            this.fullRange,
            Climate.Parameter.span(0.7f, 1.0f),
            this.fullRange,
            this.fullRange,
            this.fullRange,
            0.0f,
            Biomes.LUSH_CAVES
        )
        this.addBottomBiomeTo(
            parameters, this.fullRange, this.fullRange, this.fullRange, Climate.Parameter.span(
                erosionThresholds[0],
                erosionThresholds[1]
            ), this.fullRange, 0.0f, Biomes.DEEP_DARK
        )
    }

    private fun pickRegularBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        if (weirdness.max() < 0L) {
            return middleBiomes[temperature][humidity]
        } else {
            val registryKey = middleBiomesVariant[temperature][humidity]
            return registryKey ?: middleBiomes[temperature][humidity]
        }
    }

    private fun pickRegularBiomeOrBadlandsIfHot(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        return if (temperature == 4) this.pickBadlandsBiome(humidity, weirdness) else this.pickRegularBiome(
            temperature,
            humidity,
            weirdness
        )
    }

    private fun pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        return if (temperature == 0) this.pickSlopeBiome(
            temperature,
            humidity,
            weirdness
        ) else this.pickRegularBiomeOrBadlandsIfHot(temperature, humidity, weirdness)
    }

    private fun maybePickWindsweptSavanna(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter,
        fallback: ResourceKey<Biome>
    ): ResourceKey<Biome> {
        return if (temperature > 1 && humidity < 4 && weirdness.max() >= 0L) Biomes.WINDSWEPT_SAVANNA else fallback
    }

    private fun pickShatteredCoastBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        val registryKey =
            if (weirdness.max() >= 0L) this.pickRegularBiome(temperature, humidity, weirdness) else this.pickBeachBiome(
                temperature,
                humidity
            )
        return this.maybePickWindsweptSavanna(temperature, humidity, weirdness, registryKey)
    }

    private fun pickBeachBiome(temperature: Int, humidity: Int): ResourceKey<Biome> {
        return if (temperature == 0) {
            Biomes.SNOWY_BEACH
        } else {
            if (temperature == 4) Biomes.DESERT else Biomes.BEACH
        }
    }

    private fun pickBadlandsBiome(humidity: Int, weirdness: Climate.Parameter): ResourceKey<Biome> {
        return if (humidity < 2) {
            if (weirdness.max() < 0L) Biomes.BADLANDS else Biomes.ERODED_BADLANDS
        } else {
            if (humidity < 3) Biomes.BADLANDS else Biomes.WOODED_BADLANDS
        }
    }

    private fun pickPlateauBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        if (weirdness.max() >= 0L) {
            val registryKey = plateauBiomesVariant[temperature][humidity]
            if (registryKey != null) {
                return registryKey
            }
        }

        return plateauBiomes[temperature][humidity]
    }

    private fun pickPeakBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        return if (temperature <= 2) {
            if (weirdness.max() < 0L) Biomes.JAGGED_PEAKS else Biomes.FROZEN_PEAKS
        } else {
            if (temperature == 3) Biomes.STONY_PEAKS else pickBadlandsBiome(
                humidity,
                weirdness
            )
        }
    }

    private fun pickSlopeBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        return if (temperature >= 3) {
            pickPlateauBiome(temperature, humidity, weirdness)
        } else {
            if (humidity <= 1) Biomes.SNOWY_SLOPES else Biomes.GROVE
        }
    }

    private fun pickShatteredBiome(
        temperature: Int,
        humidity: Int,
        weirdness: Climate.Parameter
    ): ResourceKey<Biome> {
        val registryKey = shatteredBiomes[temperature][humidity]
        return registryKey ?: this.pickRegularBiome(temperature, humidity, weirdness)
    }

    private fun addSurfaceBiomeTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        temperature: Climate.Parameter,
        humidity: Climate.Parameter,
        continentalness: Climate.Parameter,
        erosion: Climate.Parameter,
        weirdness: Climate.Parameter,
        offset: Float,
        biome: ResourceKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                Climate.parameters(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    Climate.Parameter.point(0.0f),
                    weirdness,
                    offset
                ), biome
            )
        )
        parameters.accept(
            Pair.of(
                Climate.parameters(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    Climate.Parameter.point(1.0f),
                    weirdness,
                    offset
                ), biome
            )
        )
    }

    private fun addUndergroundBiomeTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        temperature: Climate.Parameter,
        humidity: Climate.Parameter,
        continentalness: Climate.Parameter,
        erosion: Climate.Parameter,
        weirdness: Climate.Parameter,
        offset: Float,
        biome: ResourceKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                Climate.parameters(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    Climate.Parameter.span(0.2f, 0.9f),
                    weirdness,
                    offset
                ), biome
            )
        )
    }

    private fun addBottomBiomeTo(
        parameters: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>,
        temperature: Climate.Parameter,
        humidity: Climate.Parameter,
        continentialness: Climate.Parameter,
        erosion: Climate.Parameter,
        depth: Climate.Parameter,
        weirdness: Float,
        registryKey: ResourceKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                Climate.parameters(
                    temperature,
                    humidity,
                    continentialness,
                    erosion,
                    Climate.Parameter.point(1.1f),
                    depth,
                    weirdness
                ), registryKey
            )
        )
    }

    fun getContinentalnessDescription(continentalness: Double): String {
        val d = Climate.quantizeCoord(continentalness.toFloat()).toDouble()
        return if (d < mushroomFieldsContinentalness.max().toDouble()) {
            "Mushroom fields"
        } else if (d < deepOceanContinentalness.max().toDouble()) {
            "Deep ocean"
        } else if (d < oceanContinentalness.max().toDouble()) {
            "Ocean"
        } else if (d < coastContinentalness.max().toDouble()) {
            "Coast"
        } else if (d < nearInlandContinentalness.max().toDouble()) {
            "Near inland"
        } else if (d < midInlandContinentalness.max().toDouble())
            "Mid inland"
        else
            "Far inland"
    }

    fun getErosionDescription(erosion: Double): String {
        return getNoiseValueDescription(erosion, this.erosionThresholds)
    }

    fun getTemperatureDescription(temperature: Double): String {
        return getNoiseValueDescription(temperature, this.temperatureThresholds)
    }

    fun getHumidityDescription(humidity: Double): String {
        return getNoiseValueDescription(humidity, this.humidityThresholds)
    }

    @get:VisibleForDebug
    val continentalnessThresholds: Array<Climate.Parameter>
        get() = arrayOf(
            this.mushroomFieldsContinentalness,
            this.deepOceanContinentalness,
            this.oceanContinentalness,
            this.coastContinentalness,
            this.nearInlandContinentalness,
            this.midInlandContinentalness,
            this.farInlandContinentalness
        )

    @get:VisibleForDebug
    val peaksAndValleysThresholds: Array<Climate.Parameter>
        get() = arrayOf(
            Climate.Parameter.span(-2.0f, NoiseRouterData.peaksAndValleys(0.05f)),
            Climate.Parameter.span(
                NoiseRouterData.peaksAndValleys(0.05f), NoiseRouterData.peaksAndValleys(0.26666668f)
            ),
            Climate.Parameter.span(
                NoiseRouterData.peaksAndValleys(0.26666668f), NoiseRouterData.peaksAndValleys(0.4f)
            ),
            Climate.Parameter.span(
                NoiseRouterData.peaksAndValleys(0.4f), NoiseRouterData.peaksAndValleys(0.56666666f)
            ),
            Climate.Parameter.span(
                NoiseRouterData.peaksAndValleys(0.56666666f), 2.0f
            )
        )

    @get:VisibleForDebug
    val weirdnessThresholds: Array<Climate.Parameter>
        get() = arrayOf(Climate.Parameter.span(-2.0f, 0.0f), Climate.Parameter.span(0.0f, 2.0f))

    companion object {
        private const val VALLEY_SIZE = 0.05f
        private const val LOW_START = 0.26666668f
        const val HIGH_START: Float = 0.4f
        private const val HIGH_END = 0.93333334f
        private const val PEAK_SIZE = 0.1f
        const val PEAK_START: Float = 0.56666666f
        private const val PEAK_END = 0.7666667f
        const val NEAR_INLAND_START: Float = -0.11f
        const val MID_INLAND_START: Float = 0.03f
        const val FAR_INLAND_START: Float = 0.3f
        const val EROSION_INDEX_1_START: Float = -0.78f
        const val EROSION_INDEX_2_START: Float = -0.375f
        private const val EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225f
        private const val DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9f
        fun deepDarkRegion(
            erosion: DensityFunction,
            depth: DensityFunction,
            pos: DensityFunction.FunctionContext?
        ): Boolean {
            return erosion.compute(pos) < -0.22499999403953552 && depth.compute(pos) > 0.8999999761581421
        }

        fun getPeaksAndValleysDescription(erosion: Double): String {
            return if (erosion < NoiseRouterData.peaksAndValleys(0.05f).toDouble()) {
                "Valley"
            } else if (erosion < NoiseRouterData.peaksAndValleys(0.26666668f).toDouble()) {
                "Low"
            } else if (erosion < NoiseRouterData.peaksAndValleys(0.4f).toDouble()) {
                "Mid"
            } else {
                if (erosion < NoiseRouterData.peaksAndValleys(0.56666666f)
                        .toDouble()
                ) "High" else "Peak"
            }
        }

        private fun getNoiseValueDescription(value: Double, parameters: Array<Climate.Parameter>): String {
            val d = Climate.quantizeCoord(value.toFloat()).toDouble()

            for (i in parameters.indices) {
                if (d < parameters[i].max().toDouble()) {
                    return "" + i
                }
            }

            return "?"
        }
    }
}

