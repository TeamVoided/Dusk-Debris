package org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider

open class RockFormationFeatureConfig(
    val rockState: BlockStateProvider,
    val sizeY: IntProvider,
    val sizeXZ: IntProvider,
    val exponent: FloatProvider = ConstantFloatProvider.create(2f)
) : FeatureConfig {

    constructor(
        sizeY: IntProvider,
        sizeXZ: IntProvider,
        exponent: FloatProvider = ConstantFloatProvider.create(2f)
    ) : this(SimpleBlockStateProvider.of(Blocks.STONE.defaultState), sizeY, sizeXZ, exponent)

    companion object {
        val MAP_CODEC: MapCodec<RockFormationFeatureConfig> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BlockStateProvider.TYPE_CODEC
                        .fieldOf("rock_state")
                        .orElse(SimpleBlockStateProvider.of(Blocks.STONE.defaultState))
                        .forGetter { it.rockState },
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
