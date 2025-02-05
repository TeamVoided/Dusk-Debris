package org.teamvoided.dusk_debris.world.gen.configured_carver.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.HolderSet
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.CarverConfig
import net.minecraft.world.gen.carver.CarverDebugConfig
import net.minecraft.world.gen.heightprovider.HeightProvider
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
