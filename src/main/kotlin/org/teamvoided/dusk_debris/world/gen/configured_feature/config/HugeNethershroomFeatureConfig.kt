package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

data class HugeNethershroomFeatureConfig(
    var replaceable: TagKey<Block>,
    var ignores: TagKey<Block>,
    val stemBlock: BlockStateProvider,
    val stemSize: IntProvider,
    val capBlock: BlockStateProvider,
    val capRadius: IntProvider,
    val capHeight: IntProvider,
    val capXZInletOffset: IntProvider,
    val capYInletOffset: IntProvider
) : FeatureConfig {
    companion object {
        val CODEC: Codec<HugeNethershroomFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<HugeNethershroomFeatureConfig> ->
                instance.group(
                    TagKey.createHashedCodec(RegistryKeys.BLOCK).fieldOf("replaceable").forGetter { it.replaceable },
                    TagKey.createHashedCodec(RegistryKeys.BLOCK).fieldOf("ignores").forGetter { it.ignores },
                    BlockStateProvider.TYPE_CODEC.fieldOf("stem_block").forGetter { it.stemBlock },
                    IntProvider.method_35004(1, 32).fieldOf("stem_size").forGetter { it.stemSize },
                    BlockStateProvider.TYPE_CODEC.fieldOf("cap_block").forGetter { it.capBlock },
                    IntProvider.method_35004(1, 8).fieldOf("cap_radius").forGetter { it.capRadius },
                    IntProvider.method_35004(1, 16).fieldOf("cap_height").forGetter { it.capHeight },
                    IntProvider.method_35004(0, 8).fieldOf("cap_xz_inlet_offset").forGetter { it.capXZInletOffset },
                    IntProvider.method_35004(0, 8).fieldOf("cap_y_inlet_offset").forGetter { it.capYInletOffset }
                ).apply(instance, ::HugeNethershroomFeatureConfig)
            }
    }
}
