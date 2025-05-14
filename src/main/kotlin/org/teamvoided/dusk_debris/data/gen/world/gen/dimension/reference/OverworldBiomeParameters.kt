package org.teamvoided.dusk_debris.data.gen.world.gen.dimension.reference

import com.mojang.datafixers.util.Pair
import net.minecraft.SharedConstants
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.VanillaDynamicRegistries
import net.minecraft.util.annotation.Debug
import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.noise.NoiseRouterData
import java.util.function.Consumer

class OverworldBiomeParameters {
    private val fullRange: MultiNoiseUtil.ParameterRange = MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f)

    @get:Debug
    val temperatureThresholds: Array<MultiNoiseUtil.ParameterRange> = arrayOf(
        MultiNoiseUtil.ParameterRange.of(-1.0f, -0.45f),
        MultiNoiseUtil.ParameterRange.of(-0.45f, -0.15f),
        MultiNoiseUtil.ParameterRange.of(-0.15f, 0.2f),
        MultiNoiseUtil.ParameterRange.of(0.2f, 0.55f),
        MultiNoiseUtil.ParameterRange.of(0.55f, 1.0f)
    )

    @get:Debug
    val humidityThresholds: Array<MultiNoiseUtil.ParameterRange> = arrayOf(
        MultiNoiseUtil.ParameterRange.of(-1.0f, -0.35f),
        MultiNoiseUtil.ParameterRange.of(-0.35f, -0.1f),
        MultiNoiseUtil.ParameterRange.of(-0.1f, 0.1f),
        MultiNoiseUtil.ParameterRange.of(0.1f, 0.3f),
        MultiNoiseUtil.ParameterRange.of(0.3f, 1.0f)
    )

    @get:Debug
    val erosionThresholds: Array<MultiNoiseUtil.ParameterRange> = arrayOf(
        MultiNoiseUtil.ParameterRange.of(-1.0f, -0.78f),
        MultiNoiseUtil.ParameterRange.of(-0.78f, -0.375f),
        MultiNoiseUtil.ParameterRange.of(-0.375f, -0.2225f),
        MultiNoiseUtil.ParameterRange.of(-0.2225f, 0.05f),
        MultiNoiseUtil.ParameterRange.of(0.05f, 0.45f),
        MultiNoiseUtil.ParameterRange.of(0.45f, 0.55f),
        MultiNoiseUtil.ParameterRange.of(0.55f, 1.0f)
    )
    private val frozenTemperature = temperatureThresholds[0]
    private val unfrozenTemperature: MultiNoiseUtil.ParameterRange = MultiNoiseUtil.ParameterRange.combine(
        temperatureThresholds[1],
        temperatureThresholds[4]
    )
    private val mushroomFieldsContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-1.2f, -1.05f)
    private val deepOceanContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-1.05f, -0.455f)
    private val oceanContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-0.455f, -0.19f)
    private val coastContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-0.19f, -0.11f)
    private val inlandContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-0.11f, 0.55f)
    private val nearInlandContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(-0.11f, 0.03f)
    private val midInlandContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(0.03f, 0.3f)
    private val farInlandContinentalness: MultiNoiseUtil.ParameterRange =
        MultiNoiseUtil.ParameterRange.of(0.3f, 1.0f)
    private val oceanBiomes: Array<Array<RegistryKey<Biome>>>
    private val middleBiomes: Array<Array<RegistryKey<Biome>>>
    private val middleBiomesVariant: Array<Array<RegistryKey<Biome>?>>
    private val plateauBiomes: Array<Array<RegistryKey<Biome>>>
    private val plateauBiomesVariant: Array<Array<RegistryKey<Biome>?>>
    private val shatteredBiomes: Array<Array<RegistryKey<Biome>?>>

    init {
        this.oceanBiomes = arrayOf<Array<RegistryKey<Biome>>>(
            arrayOf(
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN,
                Biomes.WARM_OCEAN
            ),
            arrayOf(Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN)
        )
        this.middleBiomes = arrayOf<Array<RegistryKey<Biome>>>(
            arrayOf(Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA),
            arrayOf(Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
            arrayOf(Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST),
            arrayOf(Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE),
            arrayOf(Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT)
        )
        this.middleBiomesVariant = arrayOf<Array<RegistryKey<Biome>?>>(
            arrayOf(Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null),
            arrayOf(null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA),
            arrayOf(Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null),
            arrayOf(null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE),
            arrayOf(null, null, null, null, null)
        )
        this.plateauBiomes = arrayOf<Array<RegistryKey<Biome>>>(
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
        this.plateauBiomesVariant = arrayOf<Array<RegistryKey<Biome>?>>(
            arrayOf(Biomes.ICE_SPIKES, null, null, null, null),
            arrayOf(Biomes.CHERRY_GROVE, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA),
            arrayOf(Biomes.CHERRY_GROVE, Biomes.CHERRY_GROVE, Biomes.FOREST, Biomes.BIRCH_FOREST, null),
            arrayOf(null, null, null, null, null),
            arrayOf(Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null)
        )
        this.shatteredBiomes = arrayOf<Array<RegistryKey<Biome>?>>(
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

    val spawnSuitabilityNoises: List<MultiNoiseUtil.NoiseHypercube>
        get() {
            val parameterRange = MultiNoiseUtil.ParameterRange.of(0.0f)
            val f = 0.16f
            return java.util.List.of(
                MultiNoiseUtil.NoiseHypercube(
                    this.fullRange, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
                        this.inlandContinentalness, this.fullRange
                    ), this.fullRange, parameterRange, MultiNoiseUtil.ParameterRange.of(-1.0f, -0.16f), 0L
                ), MultiNoiseUtil.NoiseHypercube(
                    this.fullRange, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
                        this.inlandContinentalness, this.fullRange
                    ), this.fullRange, parameterRange, MultiNoiseUtil.ParameterRange.of(0.16f, 1.0f), 0L
                )
            )
        }

    protected fun addBiomesTo(biomeEntryConsumer: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
        if (SharedConstants.generateSquareTerrainWithoutNoise) {
            this.addDebugBiomesTo(biomeEntryConsumer)
        } else {
            this.addOffCoastBiomesTo(biomeEntryConsumer)
            this.addInlandBiomesTo(biomeEntryConsumer)
            this.addUndergroundBiomesTo(biomeEntryConsumer)
        }
    }

    private fun HolderProvider<DensityFunction>.get(key: RegistryKey<DensityFunction>): DensityFunctions.Spline.FunctionWrapper =
        DensityFunctions.Spline.FunctionWrapper(this.getHolderOrThrow(key))

    private fun addDebugBiomesTo(biomeEntryConsumer: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
        val provider = VanillaDynamicRegistries.createLookup()
        val dense: HolderProvider<DensityFunction> = provider.getLookupOrThrow(RegistryKeys.DENSITY_FUNCTION)
        val continents = dense.get(NoiseRouterData.CONTINENTS_OVERWORLD)
        val erosion = dense.get(NoiseRouterData.EROSION_OVERWORLD)
        val ridgesFolded = dense.get(NoiseRouterData.RIDGES_FOLDED_OVERWORLD)
        biomeEntryConsumer.accept(
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    this.fullRange,
                    this.fullRange,
                    this.fullRange,
                    this.fullRange,
                    MultiNoiseUtil.ParameterRange.of(0.0f),
                    this.fullRange,
                    0.01f
                ),
                Biomes.PLAINS
            )
        )
        val splineEros: Spline<*, *> = VanillaTerrainParametersCreator.method_42051(
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
        if (splineEros is Spline.Multipoint<*, *>) {
            var registryKey = Biomes.DESERT
            splineEros.locations().forEach {
                biomeEntryConsumer.accept(
                    Pair.of(
                        MultiNoiseUtil.createNoiseHypercube(
                            this.fullRange,
                            this.fullRange,
                            this.fullRange,
                            MultiNoiseUtil.ParameterRange.of(it),
                            MultiNoiseUtil.ParameterRange.of(0.0f),
                            this.fullRange,
                            0.0f
                        ),
                        registryKey
                    )
                )
                registryKey = if (registryKey == Biomes.DESERT) Biomes.BADLANDS else Biomes.DESERT
            }
        }

        val splineCont: Spline<*, *> =
            VanillaTerrainParametersCreator.method_42056(continents, erosion, ridgesFolded, false)
        if (splineCont is Spline.Multipoint<*, *>) {
            splineCont.locations().forEach {
                biomeEntryConsumer.accept(
                    Pair.of(
                        MultiNoiseUtil.createNoiseHypercube(
                            this.fullRange,
                            this.fullRange,
                            MultiNoiseUtil.ParameterRange.of(it),
                            this.fullRange,
                            MultiNoiseUtil.ParameterRange.of(0.0f),
                            this.fullRange,
                            0.0f
                        ),
                        Biomes.SNOWY_TAIGA
                    )
                )
            }
        }
    }

    private fun addOffCoastBiomesTo(parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
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

    private fun addInlandBiomesTo(parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
        this.addMidBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(-1.0f, -0.93333334f))
        this.addHighBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.93333334f, -0.7666667f))
        this.addPeaksTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.7666667f, -0.56666666f))
        this.addHighBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.56666666f, -0.4f))
        this.addMidBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.4f, -0.26666668f))
        this.addLowBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.26666668f, -0.05f))
        this.addValleysTo(parameters, MultiNoiseUtil.ParameterRange.of(-0.05f, 0.05f))
        this.addLowBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(0.05f, 0.26666668f))
        this.addMidBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(0.26666668f, 0.4f))
        this.addHighBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(0.4f, 0.56666666f))
        this.addPeaksTo(parameters, MultiNoiseUtil.ParameterRange.of(0.56666666f, 0.7666667f))
        this.addHighBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(0.7666667f, 0.93333334f))
        this.addMidBiomesTo(parameters, MultiNoiseUtil.ParameterRange.of(0.93333334f, 1.0f))
    }

    private fun addPeaksTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        weirdness: MultiNoiseUtil.ParameterRange
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ), MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[4], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey6
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[6], weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addHighBiomesTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        weirdness: MultiNoiseUtil.ParameterRange
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
                    MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey8
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, this.nearInlandContinentalness,
                    erosionThresholds[1], weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[1], weirdness, 0.0f, registryKey7
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ), MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[4], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey6
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[5], weirdness, 0.0f, registryKey5
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[6], weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addMidBiomesTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        weirdness: MultiNoiseUtil.ParameterRange
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            this.coastContinentalness,
            MultiNoiseUtil.ParameterRange.combine(
                erosionThresholds[0],
                erosionThresholds[2]
            ),
            weirdness,
            0.0f,
            Biomes.STONY_SHORE
        )
        this.addSurfaceBiomeTo(
            parameters,
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness),
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.nearInlandContinentalness, this.farInlandContinentalness
                    ),
                    erosionThresholds[0], weirdness, 0.0f, registryKey9
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.coastContinentalness, this.nearInlandContinentalness
                    ),
                    erosionThresholds[3], weirdness, 0.0f, registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                        parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[4], weirdness, 0.0f, registryKey
                    )
                } else {
                    this.addSurfaceBiomeTo(
                        parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                        parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[6], weirdness, 0.0f, registryKey
                    )
                }
            }
        }
    }

    private fun addLowBiomesTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        weirdness: MultiNoiseUtil.ParameterRange
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            this.coastContinentalness,
            MultiNoiseUtil.ParameterRange.combine(
                erosionThresholds[0],
                erosionThresholds[2]
            ),
            weirdness,
            0.0f,
            Biomes.STONY_SHORE
        )
        this.addSurfaceBiomeTo(
            parameters,
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness),
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
                    MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ),
                    weirdness,
                    0.0f,
                    registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ), weirdness, 0.0f, registryKey3
                )
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.nearInlandContinentalness,
                    MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ),
                    weirdness,
                    0.0f,
                    registryKey
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[2],
                        erosionThresholds[3]
                    ), weirdness, 0.0f, registryKey2
                )
                this.addSurfaceBiomeTo(
                    parameters,
                    parameterRange,
                    parameterRange2,
                    this.coastContinentalness,
                    MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[3],
                        erosionThresholds[4]
                    ),
                    weirdness,
                    0.0f,
                    registryKey4
                )
                this.addSurfaceBiomeTo(
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
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
                        parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                            this.nearInlandContinentalness, this.farInlandContinentalness
                        ),
                        erosionThresholds[6], weirdness, 0.0f, registryKey
                    )
                }
            }
        }
    }

    private fun addValleysTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        weirdness: MultiNoiseUtil.ParameterRange
    ) {
        this.addSurfaceBiomeTo(
            parameters,
            this.frozenTemperature,
            this.fullRange,
            this.coastContinentalness,
            MultiNoiseUtil.ParameterRange.combine(
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
            MultiNoiseUtil.ParameterRange.combine(
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
            MultiNoiseUtil.ParameterRange.combine(
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
            MultiNoiseUtil.ParameterRange.combine(
                erosionThresholds[0],
                erosionThresholds[1]
            ),
            weirdness,
            0.0f,
            Biomes.RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.frozenTemperature, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
                this.coastContinentalness, this.farInlandContinentalness
            ), MultiNoiseUtil.ParameterRange.combine(
                erosionThresholds[2],
                erosionThresholds[5]
            ), weirdness, 0.0f, Biomes.FROZEN_RIVER
        )
        this.addSurfaceBiomeTo(
            parameters, this.unfrozenTemperature, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
                this.coastContinentalness, this.farInlandContinentalness
            ), MultiNoiseUtil.ParameterRange.combine(
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
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[1],
                temperatureThresholds[2]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.inlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters,
            MultiNoiseUtil.ParameterRange.combine(
                temperatureThresholds[3],
                temperatureThresholds[4]
            ),
            this.fullRange,
            MultiNoiseUtil.ParameterRange.combine(this.inlandContinentalness, this.farInlandContinentalness),
            erosionThresholds[6],
            weirdness,
            0.0f,
            Biomes.MANGROVE_SWAMP
        )
        this.addSurfaceBiomeTo(
            parameters, this.frozenTemperature, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
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
                    parameters, parameterRange, parameterRange2, MultiNoiseUtil.ParameterRange.combine(
                        this.midInlandContinentalness, this.farInlandContinentalness
                    ), MultiNoiseUtil.ParameterRange.combine(
                        erosionThresholds[0],
                        erosionThresholds[1]
                    ), weirdness, 0.0f, registryKey
                )
            }
        }
    }

    private fun addUndergroundBiomesTo(parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
        this.addUndergroundBiomeTo(
            parameters,
            this.fullRange,
            this.fullRange,
            MultiNoiseUtil.ParameterRange.of(0.8f, 1.0f),
            this.fullRange,
            this.fullRange,
            0.0f,
            Biomes.DRIPSTONE_CAVES
        )
        this.addUndergroundBiomeTo(
            parameters,
            this.fullRange,
            MultiNoiseUtil.ParameterRange.of(0.7f, 1.0f),
            this.fullRange,
            this.fullRange,
            this.fullRange,
            0.0f,
            Biomes.LUSH_CAVES
        )
        this.addBottomBiomeTo(
            parameters, this.fullRange, this.fullRange, this.fullRange, MultiNoiseUtil.ParameterRange.combine(
                erosionThresholds[0],
                erosionThresholds[1]
            ), this.fullRange, 0.0f, Biomes.DEEP_DARK
        )
    }

    private fun pickRegularBiome(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
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
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
        return if (temperature == 4) this.pickBadlandsBiome(humidity, weirdness) else this.pickRegularBiome(
            temperature,
            humidity,
            weirdness
        )
    }

    private fun pickRegularBiomeOrBadlandsIfHotOrSlopeIfCold(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
        return if (temperature == 0) this.pickSlopeBiome(
            temperature,
            humidity,
            weirdness
        ) else this.pickRegularBiomeOrBadlandsIfHot(temperature, humidity, weirdness)
    }

    private fun maybePickWindsweptSavanna(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange,
        fallback: RegistryKey<Biome>
    ): RegistryKey<Biome> {
        return if (temperature > 1 && humidity < 4 && weirdness.max() >= 0L) Biomes.WINDSWEPT_SAVANNA else fallback
    }

    private fun pickShatteredCoastBiome(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
        val registryKey =
            if (weirdness.max() >= 0L) this.pickRegularBiome(temperature, humidity, weirdness) else this.pickBeachBiome(
                temperature,
                humidity
            )
        return this.maybePickWindsweptSavanna(temperature, humidity, weirdness, registryKey)
    }

    private fun pickBeachBiome(temperature: Int, humidity: Int): RegistryKey<Biome> {
        return if (temperature == 0) {
            Biomes.SNOWY_BEACH
        } else {
            if (temperature == 4) Biomes.DESERT else Biomes.BEACH
        }
    }

    private fun pickBadlandsBiome(humidity: Int, weirdness: MultiNoiseUtil.ParameterRange): RegistryKey<Biome> {
        return if (humidity < 2) {
            if (weirdness.max() < 0L) Biomes.BADLANDS else Biomes.ERODED_BADLANDS
        } else {
            if (humidity < 3) Biomes.BADLANDS else Biomes.WOODED_BADLANDS
        }
    }

    private fun pickPlateauBiome(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
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
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
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
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
        return if (temperature >= 3) {
            pickPlateauBiome(temperature, humidity, weirdness)
        } else {
            if (humidity <= 1) Biomes.SNOWY_SLOPES else Biomes.GROVE
        }
    }

    private fun pickShatteredBiome(
        temperature: Int,
        humidity: Int,
        weirdness: MultiNoiseUtil.ParameterRange
    ): RegistryKey<Biome> {
        val registryKey = shatteredBiomes[temperature][humidity]
        return registryKey ?: this.pickRegularBiome(temperature, humidity, weirdness)
    }

    private fun addSurfaceBiomeTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        temperature: MultiNoiseUtil.ParameterRange,
        humidity: MultiNoiseUtil.ParameterRange,
        continentalness: MultiNoiseUtil.ParameterRange,
        erosion: MultiNoiseUtil.ParameterRange,
        weirdness: MultiNoiseUtil.ParameterRange,
        offset: Float,
        biome: RegistryKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    MultiNoiseUtil.ParameterRange.of(0.0f),
                    weirdness,
                    offset
                ), biome
            )
        )
        parameters.accept(
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    MultiNoiseUtil.ParameterRange.of(1.0f),
                    weirdness,
                    offset
                ), biome
            )
        )
    }

    private fun addUndergroundBiomeTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        temperature: MultiNoiseUtil.ParameterRange,
        humidity: MultiNoiseUtil.ParameterRange,
        continentalness: MultiNoiseUtil.ParameterRange,
        erosion: MultiNoiseUtil.ParameterRange,
        weirdness: MultiNoiseUtil.ParameterRange,
        offset: Float,
        biome: RegistryKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    temperature,
                    humidity,
                    continentalness,
                    erosion,
                    MultiNoiseUtil.ParameterRange.of(0.2f, 0.9f),
                    weirdness,
                    offset
                ), biome
            )
        )
    }

    private fun addBottomBiomeTo(
        parameters: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>,
        temperature: MultiNoiseUtil.ParameterRange,
        humidity: MultiNoiseUtil.ParameterRange,
        continentialness: MultiNoiseUtil.ParameterRange,
        erosion: MultiNoiseUtil.ParameterRange,
        depth: MultiNoiseUtil.ParameterRange,
        weirdness: Float,
        registryKey: RegistryKey<Biome>
    ) {
        parameters.accept(
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    temperature,
                    humidity,
                    continentialness,
                    erosion,
                    MultiNoiseUtil.ParameterRange.of(1.1f),
                    depth,
                    weirdness
                ), registryKey
            )
        )
    }

    fun getContinentalnessDescription(continentalness: Double): String {
        val d = MultiNoiseUtil.quantizeCoord(continentalness.toFloat()).toDouble()
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

    @get:Debug
    val continentalnessThresholds: Array<MultiNoiseUtil.ParameterRange>
        get() = arrayOf(
            this.mushroomFieldsContinentalness,
            this.deepOceanContinentalness,
            this.oceanContinentalness,
            this.coastContinentalness,
            this.nearInlandContinentalness,
            this.midInlandContinentalness,
            this.farInlandContinentalness
        )

    @get:Debug
    val peaksAndValleysThresholds: Array<MultiNoiseUtil.ParameterRange>
        get() = arrayOf(
            MultiNoiseUtil.ParameterRange.of(-2.0f, NoiseRouterData.getPeaksAndValleys(0.05f)),
            MultiNoiseUtil.ParameterRange.of(
                NoiseRouterData.getPeaksAndValleys(0.05f), NoiseRouterData.getPeaksAndValleys(0.26666668f)
            ),
            MultiNoiseUtil.ParameterRange.of(
                NoiseRouterData.getPeaksAndValleys(0.26666668f), NoiseRouterData.getPeaksAndValleys(0.4f)
            ),
            MultiNoiseUtil.ParameterRange.of(
                NoiseRouterData.getPeaksAndValleys(0.4f), NoiseRouterData.getPeaksAndValleys(0.56666666f)
            ),
            MultiNoiseUtil.ParameterRange.of(
                NoiseRouterData.getPeaksAndValleys(0.56666666f), 2.0f
            )
        )

    @get:Debug
    val weirdnessThresholds: Array<MultiNoiseUtil.ParameterRange>
        get() = arrayOf(MultiNoiseUtil.ParameterRange.of(-2.0f, 0.0f), MultiNoiseUtil.ParameterRange.of(0.0f, 2.0f))

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
            return if (erosion < NoiseRouterData.getPeaksAndValleys(0.05f).toDouble()) {
                "Valley"
            } else if (erosion < NoiseRouterData.getPeaksAndValleys(0.26666668f).toDouble()) {
                "Low"
            } else if (erosion < NoiseRouterData.getPeaksAndValleys(0.4f).toDouble()) {
                "Mid"
            } else {
                if (erosion < NoiseRouterData.getPeaksAndValleys(0.56666666f)
                        .toDouble()
                ) "High" else "Peak"
            }
        }

        private fun getNoiseValueDescription(value: Double, parameters: Array<MultiNoiseUtil.ParameterRange>): String {
            val d = MultiNoiseUtil.quantizeCoord(value.toFloat()).toDouble()

            for (i in parameters.indices) {
                if (d < parameters[i].max().toDouble()) {
                    return "" + i
                }
            }

            return "?"
        }
    }
}

