package org.teamvoided.dusk_debris.data.gen.world.gen.dimension

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.source.util.MultiNoiseUtil.*
import org.teamvoided.dusk_debris.util.world_helper.NoiseHyper3
import org.teamvoided.dusk_debris.util.world_helper.range
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainParametersCreator
import java.util.function.Consumer
import java.util.function.Function

class OverworldDebugCreator {

    private val cMushIsle = range(-1.5f, -1.05f)
    private val cDeepOcean = range(-1.05f, -0.45f)
    private val cOcean = range(-0.45f, 0)
    private val cBeach = range(0, 0.1f)
    private val nonOcean = range(0.1f, 1)
    private val fullRange = range(-1, 1)

    private val splitterEros = false
    private val splitterCons = true

    private fun addDebugBiomesTo(biomeEntryConsumer: Consumer<Pair<NoiseHypercube, RegistryKey<Biome>>>) {
        val plains = NoiseHyper3(
            Biomes.PLAINS,
            fullRange,
            fullRange,
            nonOcean,
            fullRange,
            ParameterRange.of(0f),
            fullRange,
            0.01f
        )
        val beach = plains.copy().biome(Biomes.BEACH).continentalness(cBeach)
        val ocean = plains.copy().biome(Biomes.OCEAN).continentalness(cOcean)
        val deepO = plains.copy().biome(Biomes.DEEP_OCEAN).continentalness(cDeepOcean)
        val mushI = plains.copy().biome(Biomes.MUSHROOM_FIELDS).continentalness(cMushIsle)
        biomeEntryConsumer.accept(plains.create())
        biomeEntryConsumer.accept(beach.create())
        biomeEntryConsumer.accept(ocean.create())
        biomeEntryConsumer.accept(deepO.create())
        biomeEntryConsumer.accept(mushI.create())

        if (splitterEros) {
            val eros = NoiseHyper3(
                Biomes.DESERT,
                this.fullRange,
                this.fullRange,
                this.fullRange,
                range(0f),
                range(0f),
                this.fullRange,
                0f
            )
            OverworldTerrainParametersCreator.EROS.forEach {
                eros.biome(if (eros.biome == Biomes.DESERT) Biomes.BADLANDS else Biomes.DESERT).erosion(it)
                biomeEntryConsumer.accept(eros.create())
            }
        }
        if (splitterCons) {
            val cont = NoiseHyper3(
                Biomes.SNOWY_PLAINS,
                this.fullRange,
                this.fullRange,
                range(0f),
                this.fullRange,
                range(0f),
                this.fullRange,
                0f
            )
            OverworldTerrainParametersCreator.CONT.forEach {
                cont.biome(if (cont.biome == Biomes.SNOWY_TAIGA) Biomes.STONY_SHORE else Biomes.SNOWY_TAIGA)
                    .continentalness(it)
                biomeEntryConsumer.accept(cont.create())
            }
        }
    }

    companion object {
        fun <T> addBiomesTo(function: Function<RegistryKey<Biome>, T>): ParameterRangeList<T> {
            val builder = ImmutableList.builder<Pair<NoiseHypercube, T>>()
            OverworldDebugCreator().addDebugBiomesTo { builder.add(it.mapSecond(function)) }
            return ParameterRangeList(builder.build())
        }
    }
}