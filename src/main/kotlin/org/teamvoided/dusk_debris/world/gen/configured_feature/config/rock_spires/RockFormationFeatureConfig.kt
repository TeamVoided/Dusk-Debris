package org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider

open class RockFormationFeatureConfig(
    val rockState: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val sizeY: IntProvider,
    val sizeXZ: IntProvider,
    val exponent: FloatProvider = ConstantFloatProvider.create(2f)
) : FeatureConfig {

    constructor(
        replaceable: TagKey<Block>,
        sizeY: IntProvider,
        sizeXZ: IntProvider,
        exponent: FloatProvider = ConstantFloatProvider.create(2f)
    ) : this(SimpleBlockStateProvider.of(Blocks.STONE.defaultState), replaceable, sizeY, sizeXZ, exponent)

    companion object {
        val MAP_CODEC: MapCodec<RockFormationFeatureConfig> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("rock_state")
                        .orElse(SimpleBlockStateProvider.of(Blocks.STONE.defaultState))
                        .forGetter { it.rockState },
                    TagKey.createHashedCodec(RegistryKeys.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider.VALUE_CODEC
                        .fieldOf("size_y")
                        .forGetter { it.sizeY },
                    IntProvider.VALUE_CODEC
                        .fieldOf("size_xz")
                        .forGetter { it.sizeXZ },
                    FloatProvider.VALUE_CODEC
                        .fieldOf("exponent")
                        .orElse(ConstantFloatProvider.create(2f))
                        .forGetter { it.exponent },
                ).apply(instance, ::RockFormationFeatureConfig)
            }
        val CODEC: Codec<RockFormationFeatureConfig> = MAP_CODEC.codec()
    }
}
