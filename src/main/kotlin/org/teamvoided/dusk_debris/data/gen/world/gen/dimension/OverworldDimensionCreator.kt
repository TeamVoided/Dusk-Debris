package org.teamvoided.dusk_debris.data.gen.world.gen.dimension

import com.mojang.datafixers.util.Pair
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.source.util.MultiNoiseUtil.*
import org.teamvoided.dusk_debris.data.gen.world.gen.DimensionCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.dimension.OverworldDimensionCreator.ocean
import org.teamvoided.dusk_debris.util.world_helper.mult
import org.teamvoided.dusk_debris.util.world_helper.range
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainParametersCreator.CONT
import java.util.function.Function

object OverworldDimensionCreator {

    val fullRange = range(-1, 1)
    val negativeRange = range(-1, 0f)
    val positiveRange = range(0f, 1)
    val zeroRange = range(0f)

    // - - - depth - - - //
    val dTop = zeroRange
    val dCave = range(0.2f, 0.9f)
    val dBot = range(1)
    val dCaveDeep = range(1.1f)
    val dSurface = listOf(dTop, dBot)

    // - - - continentalness - - - //
    val cMushIsle = range(-1.5f, -1.05f)
    val cDeepOcean = range(-1.05f, -0.45f)
    val cOcean = range(-0.45f, -0.05f)
    val cBeach = range(-0.1f, 0.1f)
    val cOutland = range(0.1f, 0.2f)
    val cMidland = range(0.2f, 0.4f)
    val cInland = range(0.4f, 1)
    val cRiver = range(cOutland.min, cInland.max)

    // - - - erosion - - - //
    val eMount = range(-1, -0.78f)
    val eSwamp = range(0.8f, 1)

    // - - - temperature - - - //
    val tFroz = range(-1, -0.45f)
    val tCold = range(-0.45f, -0.15f)
    val tMild = range(-0.15f, 0.2f)
    val tWarm = range(0.2f, 0.55f)
    val tHot = range(0.55f, 1)

    // - - - humidity - - - //
    val hFroz = range(-1, -0.35f)
    val hCold = range(-0.35f, -0.1f)
    val hMild = range(-0.1f, 0.1f)
    val hWarm = range(0.1f, 0.3f)
    val hOldGrowth = range(0.3f, 1)
    val hLushCave = range(0.7f, 1)

    // - - - weirdness - - - // min-max is -1 1
    val wValley = range(-0.05f, 0.05f)
    val wLowP = range(0.05f, 0.2f)
    val wMedP = range(0.2f, 0.4f)
    val wHighP = range(0.4f, 0.8f)
    val wPeakP = range(0.8f, 1)
    val wLowN = wLowP.mult(-1f)
    val wMedN = wMedP.mult(-1f)
    val wHighN = wHighP.mult(-1f)
    val wPeakN = wPeakP.mult(-1f)
    val wShatter = range(1, 2)
    val wHills = range(-2, -1)

    fun <T> noiseBiomeSource(it: Function<RegistryKey<Biome>, T>): ParameterRangeList<T> {
        val list: MutableList<Pair<NoiseHypercube, T>> = mutableListOf()
        list += it.ocean(dTop)
        return ParameterRangeList(list)
    }

    private fun <T> Function<RegistryKey<Biome>, T>.ocean(depth: ParameterRange): List<Pair<NoiseHypercube, T>> {
        return biomeSource(
            this.createNH(
                Biomes.MUSHROOM_FIELDS,
                fullRange,
                fullRange,
                cMushIsle,
                fullRange,
                depth,
                fullRange,
            ),
            this.createNH(
                Biomes.DEEP_OCEAN,
                fullRange,
                fullRange,
                cDeepOcean,
                fullRange,
                depth,
                fullRange,
            ),
            this.createNH(
                Biomes.OCEAN,
                fullRange,
                fullRange,
                cOcean,
                fullRange,
                depth,
                fullRange,
            )
        )
    }


    fun <T> Function<RegistryKey<Biome>, T>.createNH(
        biome: RegistryKey<Biome>,
        temperature: ParameterRange,
        humidity: ParameterRange,
        continentalness: ParameterRange,
        erosion: ParameterRange,
        depth: ParameterRange,
        weirdness: ParameterRange,
    ): kotlin.Pair<T, NoiseHypercube> {
        return this.apply(biome) to NoiseHypercube(
            temperature,
            humidity,
            continentalness,
            erosion,
            depth,
            weirdness,
            0L
        )
    }

    fun <T> biomeSource(vararg biomes: kotlin.Pair<T, NoiseHypercube>): List<Pair<NoiseHypercube, T>> {
        val list: List<Pair<NoiseHypercube, T>> = listOf()
        biomes.forEach {
            list.addLast(Pair.of(it.second, it.first))
        }
        return list
    }

}