package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

open class MushroomFeatureConfig(
    val replaceable: TagKey<Block>,
    val ignores: TagKey<Block>,
    val stemBlock: BlockStateProvider,
    val stemSize: IntProvider,
    val capBlock: BlockStateProvider,
    val capHeight: IntProvider,
) : FeatureConfiguration {
    companion object {
        val MAP_CODEC: MapCodec<MushroomFeatureConfig> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter { it.replaceable },
                    TagKey.hashedCodec(Registries.BLOCK).fieldOf("ignores").forGetter { it.ignores },
                    BlockStateProvider.CODEC.fieldOf("stem_block").forGetter { it.stemBlock },
                    IntProvider.codec(1, 32).fieldOf("stem_size").forGetter { it.stemSize },
                    BlockStateProvider.CODEC.fieldOf("cap_block").forGetter { it.capBlock },
                    IntProvider.codec(1, 16).fieldOf("cap_height").forGetter { it.capHeight },
                ).apply(instance, ::MushroomFeatureConfig)
            }
        val CODEC: Codec<MushroomFeatureConfig> = MAP_CODEC.codec()
    }
}
