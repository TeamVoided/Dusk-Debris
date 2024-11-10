package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

class GlassSpikeFeatureConfig(
    val blockstate: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val outerWallDistanceXZ: IntProvider,
    val outerWallDistanceY: IntProvider,
    val distributionPoints: IntProvider,
    val pointOffset: IntProvider,
    val minGenOffset: Int,
    val maxGenOffset: Int,
    val noiseMultiplier: Double,
) :
    FeatureConfig {
    companion object {
        private val RANGE: Codec<Double> = Codec.doubleRange(0.0, 1.0)
        val CODEC: Codec<GlassSpikeFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<GlassSpikeFeatureConfig> ->
                instance.group(
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("blockstate")
                        .forGetter { it.blockstate },
                    TagKey.createHashedCodec(RegistryKeys.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider
                        .method_35004(-20, 20)
                        .fieldOf("outer_wall_distance_xz")
                        .orElse(UniformIntProvider.create(-5, 5))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.outerWallDistanceXZ },
                    IntProvider
                        .method_35004(-20, 20)
                        .fieldOf("outer_wall_distance_y")
                        .orElse(UniformIntProvider.create(-5, 10))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.outerWallDistanceY },
                    IntProvider
                        .method_35004(1, 20)
                        .fieldOf("distribution_points")
                        .orElse(UniformIntProvider.create(3, 4))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.distributionPoints },
                    IntProvider
                        .method_35004(0, 10)
                        .fieldOf("point_offset")
                        .orElse(UniformIntProvider.create(1, 2))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.pointOffset },
                    Codec.INT
                        .fieldOf("min_gen_offset")
                        .orElse(-16)
                        .forGetter { config: GlassSpikeFeatureConfig -> config.minGenOffset },
                    Codec.INT
                        .fieldOf("max_gen_offset")
                        .orElse(16)
                        .forGetter { config: GlassSpikeFeatureConfig -> config.maxGenOffset },
                    RANGE.fieldOf("noise_multiplier")
                        .orElse(0.05).forGetter { config: GlassSpikeFeatureConfig -> config.noiseMultiplier }
                ).apply(instance, ::GlassSpikeFeatureConfig)
            }
    }
}