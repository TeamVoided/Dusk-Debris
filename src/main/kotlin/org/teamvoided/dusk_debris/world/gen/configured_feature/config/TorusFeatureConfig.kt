package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

class TorusFeatureConfig(
    val blockstate: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val radiusToRingCenter: IntProvider,
    val ringWidth: IntProvider,
    val ringHeight: IntProvider,
    val pitch: FloatProvider,
    val yaw: FloatProvider,
    val noiseMultiplier: FloatProvider,
) :
    FeatureConfig {
    companion object {
        val CODEC: Codec<TorusFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<TorusFeatureConfig> ->
                instance.group(
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("blockstate")
                        .forGetter { it.blockstate },
                    TagKey.createHashedCodec(RegistryKeys.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider
                        .method_35004(-20, 20)
                        .fieldOf("radius_to_ring_center")
                        .orElse(UniformIntProvider.create(4, 13))
                        .forGetter { config: TorusFeatureConfig -> config.radiusToRingCenter },
                    IntProvider
                        .method_35004(-20, 20)
                        .fieldOf("ring_width")
                        .orElse(UniformIntProvider.create(2, 6))
                        .forGetter { config: TorusFeatureConfig -> config.ringWidth },
                    IntProvider
                        .method_35004(1, 20)
                        .fieldOf("ring_height")
                        .orElse(UniformIntProvider.create(2, 6))
                        .forGetter { config: TorusFeatureConfig -> config.ringHeight },
                    FloatProvider.createValidatedCodec(0f,1f)
                        .fieldOf("pitch")
                        .forGetter { config: TorusFeatureConfig -> config.pitch },
                    FloatProvider.createValidatedCodec(0f,1f)
                        .fieldOf("yaw")
                        .forGetter { config: TorusFeatureConfig -> config.yaw },
                    FloatProvider.createValidatedCodec(0f,10f)
                        .fieldOf("noise_multiplier")
                        .forGetter { config: TorusFeatureConfig -> config.noiseMultiplier }
                ).apply(instance, ::TorusFeatureConfig)
            }
    }
}