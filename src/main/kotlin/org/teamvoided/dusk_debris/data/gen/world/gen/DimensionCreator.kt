package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.registry.*
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.MultiNoiseBiomeSource
import net.minecraft.world.biome.source.util.MultiNoiseUtil.*
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.NoiseChunkGenerator
import org.teamvoided.dusk_debris.data.gen.world.gen.dimension.OverworldDimensionCreator
import org.teamvoided.dusk_debris.data.worldgen.DuskDimension
import org.teamvoided.dusk_debris.data.worldgen.DuskDimensionType
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseSettings

object DimensionCreator {

    fun bootstrap(c: BootstrapContext<DimensionOptions>) {
        val biome: HolderProvider<Biome> = c.getRegistryLookup(RegistryKeys.BIOME)

        c.register(
            DuskDimension.OVERWORLD,
            DuskDimensionType.OVERWORLD,
            DuskNoiseSettings.OVERWORLD,
            OverworldDimensionCreator.noiseBiomeSource { biome.getHolderOrThrow(it) }
        )
    }

    fun BootstrapContext<DimensionOptions>.register(
        dimension: RegistryKey<DimensionOptions>,
        dimensionType: RegistryKey<DimensionType>,
        noiseSettings: RegistryKey<ChunkGeneratorSettings>,
        parameters: ParameterRangeList<Holder<Biome>>
    ) {
        val dimensionProvider: HolderProvider<DimensionType> = this.getRegistryLookup(RegistryKeys.DIMENSION_TYPE)
        val chunkGenSettingsProvider = this.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS)
        this.register(
            dimension,
            DimensionOptions(
                dimensionProvider.getHolderOrThrow(dimensionType),
                NoiseChunkGenerator(
                    MultiNoiseBiomeSource.create(parameters),
                    chunkGenSettingsProvider.getHolderOrThrow(noiseSettings)
                )
            )
        )
    }

    fun createNoiseHypercube(
        temperature: ParameterRange,
        humidity: ParameterRange,
        continentalness: ParameterRange,
        erosion: ParameterRange,
        depth: ParameterRange,
        weirdness: ParameterRange,
    ): NoiseHypercube {
        return NoiseHypercube(
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