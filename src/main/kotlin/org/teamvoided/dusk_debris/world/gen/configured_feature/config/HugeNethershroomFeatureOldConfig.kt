package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

data class HugeNethershroomFeatureOldConfig(
    var replaceable: TagKey<Block>,
    var ignores: TagKey<Block>,
    val stemBlock: BlockStateProvider,
    val stemSize: IntProvider,
    val capBlock: BlockStateProvider,
    val capRadius: IntProvider,
    val capHeight: IntProvider,
    val capXZInletOffset: IntProvider,
    val capYInletOffset: IntProvider
) : FeatureConfiguration {
    companion object {
        val CODEC: Codec<HugeNethershroomFeatureOldConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<HugeNethershroomFeatureOldConfig> ->
                instance.group(
                    TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter { it.replaceable },
                    TagKey.hashedCodec(Registries.BLOCK).fieldOf("ignores").forGetter { it.ignores },
                    BlockStateProvider.CODEC.fieldOf("stem_block").forGetter { it.stemBlock },
                    IntProvider.codec(1, 32).fieldOf("stem_size").forGetter { it.stemSize },
                    BlockStateProvider.CODEC.fieldOf("cap_block").forGetter { it.capBlock },
                    IntProvider.codec(1, 8).fieldOf("cap_radius").forGetter { it.capRadius },
                    IntProvider.codec(1, 16).fieldOf("cap_height").forGetter { it.capHeight },
                    IntProvider.codec(0, 8).fieldOf("cap_xz_inlet_offset").forGetter { it.capXZInletOffset },
                    IntProvider.codec(0, 8).fieldOf("cap_y_inlet_offset").forGetter { it.capYInletOffset }
                ).apply(instance, ::HugeNethershroomFeatureOldConfig)
            }
    }
}
