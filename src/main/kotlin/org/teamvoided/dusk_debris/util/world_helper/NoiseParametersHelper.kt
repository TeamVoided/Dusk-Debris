package org.teamvoided.dusk_debris.util.world_helper

import com.mojang.datafixers.util.Pair
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate.*
import java.util.function.Function


fun <T> Function<ResourceKey<Biome>, T>.createNH(
    biome: ResourceKey<Biome>,
    temperature: Parameter,
    humidity: Parameter,
    continentalness: Parameter,
    erosion: Parameter,
    depth: Parameter,
    weirdness: Parameter,
): kotlin.Pair<T, ParameterPoint> {
    return this.apply(biome) to ParameterPoint(
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
    var biome: ResourceKey<Biome>,
    var temperature: Parameter = zeroRange,
    var humidity: Parameter = zeroRange,
    var continentalness: Parameter = zeroRange,
    var erosion: Parameter = zeroRange,
    var depth: Parameter = zeroRange,
    var weirdness: Parameter = zeroRange,
    var offset: Float = 0f
) {
    fun create(): Pair<ParameterPoint, ResourceKey<Biome>> =
        Pair(parameters(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biome)

    fun biome(biome: ResourceKey<Biome>): NoiseHyper3 {
        this.biome = biome
        return this
    }

    fun temperature(range: Number): NoiseHyper3 = temperature(range(range))

    fun temperature(range: Parameter): NoiseHyper3 {
        this.temperature = range
        return this
    }

    fun humidity(range: Number): NoiseHyper3 = humidity(range(range))

    fun humidity(range: Parameter): NoiseHyper3 {
        this.humidity = range
        return this
    }

    fun continentalness(range: Number): NoiseHyper3 = continentalness(range(range))

    fun continentalness(range: Parameter): NoiseHyper3 {
        this.continentalness = range
        return this
    }

    fun erosion(range: Number): NoiseHyper3 = erosion(range(range))

    fun erosion(range: Parameter): NoiseHyper3 {
        this.erosion = range
        return this
    }

    fun depth(range: Number): NoiseHyper3 = depth(range(range))

    fun depth(range: Parameter): NoiseHyper3 {
        this.depth = range
        return this
    }

    fun weirdness(range: Number): NoiseHyper3 = weirdness(range(range))

    fun weirdness(range: Parameter): NoiseHyper3 {
        this.weirdness = range
        return this
    }

    fun offset(offset: Float): NoiseHyper3 {
        this.offset = offset
        return this
    }
}