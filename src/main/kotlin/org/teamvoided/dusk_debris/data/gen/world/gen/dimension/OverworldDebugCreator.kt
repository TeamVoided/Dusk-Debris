package org.teamvoided.dusk_debris.data.gen.world.gen.dimension

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate.*
import org.teamvoided.dusk_debris.util.world_helper.NoiseHyper3
import org.teamvoided.dusk_debris.util.world_helper.range
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
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
    private val splitterCons = false

    private fun addDebugBiomesTo(biomeEntryConsumer: Consumer<Pair<ParameterPoint, ResourceKey<Biome>>>) {
        val plains = NoiseHyper3(
            Biomes.THE_VOID,
            fullRange,
            fullRange,
            nonOcean,
            fullRange,
            Parameter.point(0f),
            fullRange,
            0.01f
        )
        biomeEntryConsumer.accept(plains.create())
        if (splitterCons) {
            biomeEntryConsumer.accept(plains.copy().biome(Biomes.BEACH).continentalness(cBeach).create())
            biomeEntryConsumer.accept(plains.copy().biome(Biomes.OCEAN).continentalness(cOcean).create())
            biomeEntryConsumer.accept(plains.copy().biome(Biomes.DEEP_OCEAN).continentalness(cDeepOcean).create())
            biomeEntryConsumer.accept(plains.copy().biome(Biomes.MUSHROOM_FIELDS).continentalness(cMushIsle).create())
        }

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
            OverworldTerrainCreator.Eros.entries.forEach {
                eros.biome(if (eros.biome == Biomes.DESERT) Biomes.BADLANDS else Biomes.DESERT).erosion(it.f)
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
            OverworldTerrainCreator.Cont.entries.forEach {
                cont.biome(if (cont.biome == Biomes.SNOWY_TAIGA) Biomes.STONY_SHORE else Biomes.SNOWY_TAIGA)
                    .continentalness(it.f)
                biomeEntryConsumer.accept(cont.create())
            }
        }
    }

    companion object {
        fun <T> addBiomesTo(function: Function<ResourceKey<Biome>, T>): ParameterList<T> {
            val builder = ImmutableList.builder<Pair<ParameterPoint, T>>()
            OverworldDebugCreator().addDebugBiomesTo { builder.add(it.mapSecond(function)) }
            return ParameterList(builder.build())
        }
    }
}