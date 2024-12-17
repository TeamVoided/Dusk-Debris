package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.doubles.DoubleLists
import it.unimi.dsi.fastutil.ints.IntLists
import net.minecraft.registry.Holder
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.ThresholdPlacedFeature
import java.util.stream.Stream

class NoiseFeatureConfig(
    val firstNoiseOctave: Int,
    val amplitudes: List<Double>,
    val features: List<ThresholdPlacedFeature>,
    val defaultFeature: Holder<PlacedFeature>
) : FeatureConfig {

    constructor(
        firstNoiseOctave: Int,
        features: List<ThresholdPlacedFeature>,
        defaultFeature: Holder<PlacedFeature>
    ) : this(firstNoiseOctave, listOf(1.0), features, defaultFeature)

    override fun getDecoratedFeatures(): Stream<ConfiguredFeature<*, *>> {
        return Stream.concat(
            features.stream().flatMap { it.feature.value().decoratedFeatures },
            defaultFeature.value().decoratedFeatures
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
                    PlacedFeature.REGISTRY_CODEC.fieldOf("default").forGetter { it.defaultFeature }
                ).apply(instance, ::NoiseFeatureConfig)
            }
    }
}
