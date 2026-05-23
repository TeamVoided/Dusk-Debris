package org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider

open class RockFormationFeatureConfig(
    val rockState: BlockStateProvider,
    var replaceable: TagKey<Block>,
    val sizeY: IntProvider,
    val sizeXZ: IntProvider,
    val exponent: FloatProvider = ConstantFloat.of(2f)
) : FeatureConfiguration {

    constructor(
        replaceable: TagKey<Block>,
        sizeY: IntProvider,
        sizeXZ: IntProvider,
        exponent: FloatProvider = ConstantFloat.of(2f)
    ) : this(SimpleStateProvider.simple(Blocks.STONE.defaultBlockState()), replaceable, sizeY, sizeXZ, exponent)

    companion object {
        val MAP_CODEC: MapCodec<RockFormationFeatureConfig> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BlockStateProvider.CODEC
                        .fieldOf("rock_state")
                        .orElse(SimpleStateProvider.simple(Blocks.STONE.defaultBlockState()))
                        .forGetter { it.rockState },
                    TagKey.hashedCodec(Registries.BLOCK)
                        .fieldOf("replaceable")
                        .forGetter { it.replaceable },
                    IntProvider.CODEC
                        .fieldOf("size_y")
                        .forGetter { it.sizeY },
                    IntProvider.CODEC
                        .fieldOf("size_xz")
                        .forGetter { it.sizeXZ },
                    FloatProvider.CODEC
                        .fieldOf("exponent")
                        .orElse(ConstantFloat.of(2f))
                        .forGetter { it.exponent },
                ).apply(instance, ::RockFormationFeatureConfig)
            }
        val CODEC: Codec<RockFormationFeatureConfig> = MAP_CODEC.codec()
    }
}
