package org.teamvoided.dusk_debris.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig

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
                    IntProvider.method_35004(1, 8).fieldOf("cap_radius").forGetter { it.capRadius },
                    IntProvider.method_35004(0, 8).fieldOf("cap_xz_inlet_offset").forGetter { it.capXZInletOffset },
                    IntProvider.method_35004(0, 8).fieldOf("cap_y_inlet_offset").forGetter { it.capYInletOffset },
                    MushroomFeatureConfig.MAP_CODEC.forGetter { it }
                ).apply(instance, ::HugeNethershroomFeatureConfig)
            }
    }
}
