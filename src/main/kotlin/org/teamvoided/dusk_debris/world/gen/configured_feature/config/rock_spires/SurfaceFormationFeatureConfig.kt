package org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider

class SurfaceFormationFeatureConfig(
    val topState: BlockStateProvider,
    val underState: BlockStateProvider,
    rockState: BlockStateProvider,
    replaceable: TagKey<Block>,
    sizeY: IntProvider,
    sizeXZ: IntProvider,
    exponent: FloatProvider = ConstantFloatProvider.create(2f)
) : RockFormationFeatureConfig(rockState, replaceable, sizeY, sizeXZ, exponent) {
    constructor(
        topState: BlockStateProvider,
        underState: BlockStateProvider,
        replaceable: TagKey<Block>,
        sizeY: IntProvider,
        sizeXZ: IntProvider,
        exponent: FloatProvider = ConstantFloatProvider.create(2f)
    ) : this(
        topState,
        underState,
        SimpleBlockStateProvider.of(Blocks.STONE.defaultState),
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
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("top_layer")
                        .orElse(SimpleBlockStateProvider.of(Blocks.GRASS_BLOCK.defaultState))
                        .forGetter { it.topState },
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("under_layer")
                        .orElse(SimpleBlockStateProvider.of(Blocks.DIRT.defaultState))
                        .forGetter { it.underState },
                    RockFormationFeatureConfig.MAP_CODEC.forGetter { it }
                ).apply(instance, ::SurfaceFormationFeatureConfig)
            }
    }
}
