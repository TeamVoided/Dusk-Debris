package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

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
    FeatureConfiguration {
    companion object {
        private val RANGE: Codec<Double> = Codec.doubleRange(0.0, 1.0)
        val CODEC: Codec<GlassSpikeFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<GlassSpikeFeatureConfig> ->
                instance.group(
                    BlockStateProvider.CODEC
                        .fieldOf("blockstate")
                        .forGetter { it.blockstate },
                    TagKey.hashedCodec(Registries.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider
                        .codec(-20, 20)
                        .fieldOf("outer_wall_distance_xz")
                        .orElse(UniformInt.of(-5, 5))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.outerWallDistanceXZ },
                    IntProvider
                        .codec(-20, 20)
                        .fieldOf("outer_wall_distance_y")
                        .orElse(UniformInt.of(-5, 10))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.outerWallDistanceY },
                    IntProvider
                        .codec(1, 20)
                        .fieldOf("distribution_points")
                        .orElse(UniformInt.of(3, 4))
                        .forGetter { config: GlassSpikeFeatureConfig -> config.distributionPoints },
                    IntProvider
                        .codec(0, 10)
                        .fieldOf("point_offset")
                        .orElse(UniformInt.of(1, 2))
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