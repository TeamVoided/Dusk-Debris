package org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider

class SurfaceFormationFeatureConfig(
    val topState: BlockStateProvider,
    val underState: BlockStateProvider,
    rockState: BlockStateProvider,
    replaceable: TagKey<Block>,
    sizeY: IntProvider,
    sizeXZ: IntProvider,
    exponent: FloatProvider = ConstantFloat.of(2f)
) : RockFormationFeatureConfig(rockState, replaceable, sizeY, sizeXZ, exponent) {
    constructor(
        topState: BlockStateProvider,
        underState: BlockStateProvider,
        replaceable: TagKey<Block>,
        sizeY: IntProvider,
        sizeXZ: IntProvider,
        exponent: FloatProvider = ConstantFloat.of(2f)
    ) : this(
        topState,
        underState,
        SimpleStateProvider.simple(Blocks.STONE.defaultBlockState()),
        replaceable,
        sizeY,
        sizeXZ,
        exponent
    )

    constructor(
        topState: BlockStateProvider,
        underState: BlockStateProvider,
        config: RockFormationFeatureConfig,
    ) : this(
        topState,
        underState,
        config.rockState,
        config.replaceable,
        config.sizeY,
        config.sizeXZ,
        config.exponent
    )

    companion object {
        val CODEC: Codec<SurfaceFormationFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    BlockStateProvider.CODEC
                        .fieldOf("top_layer")
                        .orElse(SimpleStateProvider.simple(Blocks.GRASS_BLOCK.defaultBlockState()))
                        .forGetter { it.topState },
                    BlockStateProvider.CODEC
                        .fieldOf("under_layer")
                        .orElse(SimpleStateProvider.simple(Blocks.DIRT.defaultBlockState()))
                        .forGetter { it.underState },
                    RockFormationFeatureConfig.MAP_CODEC.forGetter { it }
                ).apply(instance, ::SurfaceFormationFeatureConfig)
            }
    }
}
