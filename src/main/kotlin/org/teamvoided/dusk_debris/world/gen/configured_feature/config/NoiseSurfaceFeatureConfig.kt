package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

class NoiseSurfaceFeatureConfig(
    val blockstate: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val threshold: Float,
) : FeatureConfiguration {
//    init {
//        if (thresholdUp < thresholdDown) {
//            error("NoiseSurfaceFeatureConfig datagen error: threshold_up is the max range, threshold_down is the min range. Feature's min: $thresholdDown, Feature's max: $thresholdUp.")
//        }
//    }

    companion object {
        private val NOISE_RANGE = Codec.floatRange(-2f, 2f)
        val CODEC: Codec<NoiseSurfaceFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    BlockStateProvider.CODEC.fieldOf("blockstate").forGetter { it.blockstate },
                    TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter { it.replaceable },
                    NOISE_RANGE.fieldOf("threshold").forGetter { it.threshold }
                ).apply(instance, ::NoiseSurfaceFeatureConfig)
            }
    }
}