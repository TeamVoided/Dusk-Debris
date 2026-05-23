package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate.*
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import org.teamvoided.dusk_debris.data.gen.world.gen.dimension.OverworldDebugCreator
import org.teamvoided.dusk_debris.data.worldgen.DuskDimension
import org.teamvoided.dusk_debris.data.worldgen.DuskDimensionType
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseSettings

object DimensionCreator {

    fun bootstrap(c: BootstrapContext<LevelStem>) {
        val biome: HolderGetter<Biome> = c.lookup(Registries.BIOME)

        c.register(
            DuskDimension.OVERWORLD,
            DuskDimensionType.OVERWORLD,
            DuskNoiseSettings.OVERWORLD,
            //OverworldDimensionCreator.noiseBiomeSource { biome.getHolderOrThrow(it) }
            OverworldDebugCreator.addBiomesTo { biome.getOrThrow(it) }
        )
    }

    fun BootstrapContext<LevelStem>.register(
        dimension: ResourceKey<LevelStem>,
        dimensionType: ResourceKey<DimensionType>,
        noiseSettings: ResourceKey<NoiseGeneratorSettings>,
        parameters: ParameterList<Holder<Biome>>
    ) {
        val dimensionProvider: HolderGetter<DimensionType> = this.lookup(Registries.DIMENSION_TYPE)
        val chunkGenSettingsProvider = this.lookup(Registries.NOISE_SETTINGS)
        this.register(
            dimension,
            LevelStem(
                dimensionProvider.getOrThrow(dimensionType),
                NoiseBasedChunkGenerator(
                    MultiNoiseBiomeSource.createFromList(parameters),
                    chunkGenSettingsProvider.getOrThrow(noiseSettings)
                )
            )
        )
    }

    fun createNoiseHypercube(
        temperature: Parameter,
        humidity: Parameter,
        continentalness: Parameter,
        erosion: Parameter,
        depth: Parameter,
        weirdness: Parameter,
    ): ParameterPoint {
        return ParameterPoint(
            temperature,
            humidity,
            continentalness,
            erosion,
            depth,
            weirdness,
            0L
        )
    }
}