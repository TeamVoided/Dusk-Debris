package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

open class MushroomFeatureConfig(
    val replaceable: TagKey<Block>,
    val ignores: TagKey<Block>,
    val stemBlock: BlockStateProvider,
    val stemSize: IntProvider,
    val capBlock: BlockStateProvider,
    val capHeight: IntProvider,
) : FeatureConfig {
    companion object {
        val MAP_CODEC: MapCodec<MushroomFeatureConfig> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    TagKey.createHashedCodec(RegistryKeys.BLOCK).fieldOf("replaceable").forGetter { it.replaceable },
                    TagKey.createHashedCodec(RegistryKeys.BLOCK).fieldOf("ignores").forGetter { it.ignores },
                    BlockStateProvider.TYPE_CODEC.fieldOf("stem_block").forGetter { it.stemBlock },
                    IntProvider.method_35004(1, 32).fieldOf("stem_size").forGetter { it.stemSize },
                    BlockStateProvider.TYPE_CODEC.fieldOf("cap_block").forGetter { it.capBlock },
                    IntProvider.method_35004(1, 16).fieldOf("cap_height").forGetter { it.capHeight },
                ).apply(instance, ::MushroomFeatureConfig)
            }
        val CODEC: Codec<MushroomFeatureConfig> = MAP_CODEC.codec()
    }
}
