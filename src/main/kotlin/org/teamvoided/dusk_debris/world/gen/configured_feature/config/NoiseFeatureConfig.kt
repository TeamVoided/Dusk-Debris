package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.ThresholdPlacedFeature
import java.util.stream.Stream

class NoiseFeatureConfig(
    val firstNoiseOctave: Int,
    val amplitudes: List<Double>,
    val features: List<ThresholdPlacedFeature>,
    val defaultFeature: Holder<PlacedFeature>
) : FeatureConfiguration {

    constructor(
        firstNoiseOctave: Int,
        features: List<ThresholdPlacedFeature>,
        defaultFeature: Holder<PlacedFeature>
    ) : this(firstNoiseOctave, listOf(1.0), features, defaultFeature)

    override fun getFeatures(): Stream<ConfiguredFeature<*, *>> {
        return Stream.concat(
            features.stream().flatMap { it.feature.value().features },
            defaultFeature.value().features
        )
    }

    companion object {
        val CODEC: Codec<NoiseFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT
                        .fieldOf("first_noise_octave")
                        .orElse(-2)
                        .forGetter { it.firstNoiseOctave },
                    Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter { it.amplitudes },
                    ThresholdPlacedFeature.CODEC.listOf().fieldOf("features").forGetter { it.features },
                    PlacedFeature.CODEC.fieldOf("default").forGetter { it.defaultFeature }
                ).apply(instance, ::NoiseFeatureConfig)
            }
    }
}
