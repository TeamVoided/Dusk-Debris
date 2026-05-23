package org.teamvoided.dusk_debris.world.gen.configured_carver.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.carver.CarverConfiguration
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig

class LakeCarverConfig(
    probability: Float,
    y: HeightProvider,
    yScale: FloatProvider,
    lavaLevel: VerticalAnchor,
    debugConfig: CarverDebugSettings, //LakeCarverDebugConfig, this doesent work
    replaceableBlocks: HolderSet<Block>,
    val horizontalRadius: IntProvider,
    val fluidState: BlockState,
    val waterLevel: FloatProvider //section of carver filled with water, bottom is -1, top is 1
) : CarverConfiguration(probability, y, yScale, lavaLevel, debugConfig, replaceableBlocks) {
    constructor(
        probability: Float,
        y: HeightProvider,
        yScale: FloatProvider,
        lavaLevel: VerticalAnchor,
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
        config: CarverConfiguration,
        horizontalRadiusMultiplier: IntProvider,
        fluidState: BlockState,
        waterLevel: FloatProvider
    ) : this(
        config.probability,
        config.y,
        config.yScale,
        config.lavaLevel,
        config.debugSettings,
        config.replaceable,
        horizontalRadiusMultiplier,
        fluidState,
        waterLevel
    )

    companion object {
        fun defaultWithFluid(
            block: HolderGetter<Block>,
            heightProvider: HeightProvider = UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
            fluidState: BlockState = Blocks.WATER.defaultBlockState()
        ): LakeCarverConfig {
            return LakeCarverConfig(
                0.15f, //0.015f,
                heightProvider,
                UniformFloat.of(0.4f, 1f),
                VerticalAnchor.aboveBottom(8),
                LakeCarverDebugConfig.default(),
                block.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),

                UniformInt.of(10, 30),
                fluidState,
                UniformFloat.of(-0.8f, 0.2f)
            )
        }

        val CODEC: Codec<LakeCarverConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<LakeCarverConfig> ->
                instance.group(
                    CarverConfiguration.CODEC.forGetter { it },
                    IntProvider.CODEC.fieldOf("horizontal_radius")
                        .forGetter { it.horizontalRadius },
                    BlockState.CODEC.optionalFieldOf("fluid_state", Blocks.WATER.defaultBlockState())
                        .forGetter { it.fluidState },
                    FloatProvider.codec(-1.0f, 1.0f).fieldOf("water_level")
                        .forGetter { it.waterLevel }
                ).apply(instance, ::LakeCarverConfig)
            }
    }
}
