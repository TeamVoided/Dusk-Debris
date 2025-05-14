package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.source.util.MultiNoiseUtil.*
import java.util.function.Function
import com.mojang.datafixers.util.Pair


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


val fullRange = range(-1, 1)
val negativeRange = range(-1, 0f)
val positiveRange = range(0f, 1)
val zeroRange = range(0f)

data class NoiseHyper3(
    var biome: RegistryKey<Biome>,
    var temperature: ParameterRange = zeroRange,
    var humidity: ParameterRange = zeroRange,
    var continentalness: ParameterRange = zeroRange,
    var erosion: ParameterRange = zeroRange,
    var depth: ParameterRange = zeroRange,
    var weirdness: ParameterRange = zeroRange,
    var offset: Float = 0f
) {
    fun create(): Pair<NoiseHypercube, RegistryKey<Biome>> =
        Pair(createNoiseHypercube(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biome)

    fun biome(biome: RegistryKey<Biome>): NoiseHyper3 {
        this.biome = biome
        return this
    }

    fun temperature(range: Number): NoiseHyper3 = temperature(range(range))

    fun temperature(range: ParameterRange): NoiseHyper3 {
        this.temperature = range
        return this
    }

    fun humidity(range: Number): NoiseHyper3 = humidity(range(range))

    fun humidity(range: ParameterRange): NoiseHyper3 {
        this.humidity = range
        return this
    }

    fun continentalness(range: Number): NoiseHyper3 = continentalness(range(range))

    fun continentalness(range: ParameterRange): NoiseHyper3 {
        this.continentalness = range
        return this
    }

    fun erosion(range: Number): NoiseHyper3 = erosion(range(range))

    fun erosion(range: ParameterRange): NoiseHyper3 {
        this.erosion = range
        return this
    }

    fun depth(range: Number): NoiseHyper3 = depth(range(range))

    fun depth(range: ParameterRange): NoiseHyper3 {
        this.depth = range
        return this
    }

    fun weirdness(range: Number): NoiseHyper3 = weirdness(range(range))

    fun weirdness(range: ParameterRange): NoiseHyper3 {
        this.weirdness = range
        return this
    }

    fun offset(offset: Float): NoiseHyper3 {
        this.offset = offset
        return this
    }
}