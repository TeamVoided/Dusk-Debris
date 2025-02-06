package org.teamvoided.dusk_debris.world.gen.configured_carver.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.HolderSet
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.CarverConfig
import net.minecraft.world.gen.carver.CarverDebugConfig
import net.minecraft.world.gen.heightprovider.HeightProvider
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig

class LakeCarverConfig(
    probability: Float,
    y: HeightProvider,
    yScale: FloatProvider,
    lavaLevel: YOffset,
    debugConfig: CarverDebugConfig, //LakeCarverDebugConfig, this doesent work
    replaceableBlocks: HolderSet<Block>,
    val horizontalRadius: IntProvider,
    val fluidState: BlockState,
    val waterLevel: FloatProvider //section of carver filled with water, bottom is -1, top is 1
) : CarverConfig(probability, y, yScale, lavaLevel, debugConfig, replaceableBlocks) {
    constructor(
        probability: Float,
        y: HeightProvider,
        yScale: FloatProvider,
        lavaLevel: YOffset,
        replaceableBlocks: HolderSet<Block>,
        horizontalRadiusMultiplier: IntProvider,
        fluidState: BlockState,
        waterLevel: FloatProvider
    ) : this(
        probability,
        y,
        yScale,
        lavaLevel,
        LakeCarverDebugConfig.default(),
        replaceableBlocks,
        horizontalRadiusMultiplier,
        fluidState,
        waterLevel
    )

    constructor(
        config: CarverConfig,
        horizontalRadiusMultiplier: IntProvider,
        fluidState: BlockState,
        waterLevel: FloatProvider
    ) : this(
        config.probability,
        config.y,
        config.yScale,
        config.lavaLevel,
        config.debugConfig,
        config.replaceable,
        horizontalRadiusMultiplier,
        fluidState,
        waterLevel
    )

    companion object {
        fun defaultWithFluid(block: HolderProvider<Block>, heightProvider: HeightProvider, fluidState: BlockState): LakeCarverConfig {
            return LakeCarverConfig(
                0.15f, //0.015f,
                UniformHeightProvider.create(YOffset.aboveBottom(8), YOffset.fixed(180)),
                UniformFloatProvider.create(0.4f, 1f),
                YOffset.aboveBottom(8),
                LakeCarverDebugConfig.default(),
                block.getTagOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),

                UniformIntProvider.create(10, 30),
                Blocks.WATER.defaultState,
                UniformFloatProvider.create(-0.8f, 0.2f)
            )
        }
        val CODEC: Codec<LakeCarverConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<LakeCarverConfig> ->
                instance.group(
                    CarverConfig.CODEC.forGetter { config: LakeCarverConfig -> config },
                    IntProvider.VALUE_CODEC.fieldOf("horizontal_radius")
                        .forGetter { it.horizontalRadius },
                    BlockState.CODEC.optionalFieldOf("fluid_state", Blocks.WATER.defaultState)
                        .forGetter { it.fluidState },
                    FloatProvider.createValidatedCodec(-1.0f, 1.0f).fieldOf("water_level")
                        .forGetter { it.waterLevel })
                    .apply(instance, ::LakeCarverConfig)
            }
    }
}
