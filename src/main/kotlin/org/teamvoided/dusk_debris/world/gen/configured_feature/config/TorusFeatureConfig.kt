package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

class TorusFeatureConfig(
    val blockstate: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val radiusToRingCenter: IntProvider,
    val ringWidth: IntProvider,
    val ringHeight: IntProvider,
    val pitch: FloatProvider,
    val roll: FloatProvider,
    val noiseMultiplier: FloatProvider,
) : FeatureConfiguration {
    companion object {
        val CODEC: Codec<TorusFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<TorusFeatureConfig> ->
                instance.group(
                    BlockStateProvider.CODEC
                        .fieldOf("blockstate")
                        .forGetter { it.blockstate },
                    TagKey.hashedCodec(Registries.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider
                        .codec(-20, 20)
                        .fieldOf("radius_to_ring_center")
                        .orElse(UniformInt.of(4, 13))
                        .forGetter { config: TorusFeatureConfig -> config.radiusToRingCenter },
                    IntProvider
                        .codec(-20, 20)
                        .fieldOf("ring_width")
                        .orElse(UniformInt.of(2, 6))
                        .forGetter { config: TorusFeatureConfig -> config.ringWidth },
                    IntProvider
                        .codec(1, 20)
                        .fieldOf("ring_height")
                        .orElse(UniformInt.of(2, 6))
                        .forGetter { config: TorusFeatureConfig -> config.ringHeight },
                    FloatProvider.codec(0f, 1f)
                        .fieldOf("pitch")
                        .forGetter { config: TorusFeatureConfig -> config.pitch },
                    FloatProvider.codec(0f, 1f)
                        .fieldOf("roll")
                        .forGetter { config: TorusFeatureConfig -> config.roll },
                    FloatProvider.codec(0f, 10f)
                        .fieldOf("noise_multiplier")
                        .forGetter { config: TorusFeatureConfig -> config.noiseMultiplier }
                ).apply(instance, ::TorusFeatureConfig)
            }
    }
}