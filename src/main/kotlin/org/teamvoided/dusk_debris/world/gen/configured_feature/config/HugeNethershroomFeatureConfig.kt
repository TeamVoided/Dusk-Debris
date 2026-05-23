package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

class HugeNethershroomFeatureConfig(
    replaceable: TagKey<Block>,
    ignores: TagKey<Block>,
    stemBlock: BlockStateProvider,
    stemSize: IntProvider,
    capBlock: BlockStateProvider,
    val capRadius: IntProvider,
    capHeight: IntProvider,
    val capXZInletOffset: IntProvider,
    val capYInletOffset: IntProvider
) : MushroomFeatureConfig(replaceable, ignores, stemBlock, stemSize, capBlock, capHeight) {

    constructor(
        capRadius: IntProvider,
        capXZInletOffset: IntProvider,
        capYInletOffset: IntProvider,
        config: MushroomFeatureConfig,
    ) : this(
            config.replaceable,
            config.ignores,
            config.stemBlock,
            config.stemSize,
            config.capBlock,
            capRadius,
            config.capHeight,
            capXZInletOffset,
            capYInletOffset
    )

    companion object {
        val CODEC: Codec<HugeNethershroomFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    IntProvider.codec(1, 8).fieldOf("cap_radius").forGetter { it.capRadius },
                    IntProvider.codec(0, 8).fieldOf("cap_xz_inlet_offset").forGetter { it.capXZInletOffset },
                    IntProvider.codec(0, 8).fieldOf("cap_y_inlet_offset").forGetter { it.capYInletOffset },
                    MushroomFeatureConfig.MAP_CODEC.forGetter { it }
                ).apply(instance, ::HugeNethershroomFeatureConfig)
            }
    }
}
