package org.teamvoided.dusk_debris.world.gen.configured_carver.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.carver.CarverConfiguration
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig

class GeodeCarverConfig(
    probability: Float,
    y: HeightProvider,
    yScale: FloatProvider,
    lavaLevel: VerticalAnchor,
    debugConfig: CarverDebugSettings,
    replaceableBlocks: HolderSet<Block>,
    val horizontalRadius: IntProvider,
    val outerLayerBlock: BlockStateProvider,
    val middleLayerBlock: BlockStateProvider,
    val innerLayerBlock: BlockStateProvider,
    val extraInnerBlock: BlockStateProvider,
) : CarverConfiguration(probability, y, yScale, lavaLevel, debugConfig, replaceableBlocks) {

    constructor(
        probability: Float,
        y: HeightProvider,
        yScale: FloatProvider,
        lavaLevel: VerticalAnchor,
        replaceableBlocks: HolderSet<Block>,
        horizontalRadiusMultiplier: IntProvider,
        outerLayerBlock: BlockStateProvider,
        middleLayerBlock: BlockStateProvider,
        innerLayerBlock: BlockStateProvider,
        extraInnerBlock: BlockStateProvider,
    ) : this(
        probability,
        y,
        yScale,
        lavaLevel,
        LakeCarverDebugConfig.default(),
        replaceableBlocks,
        horizontalRadiusMultiplier,
        outerLayerBlock,
        middleLayerBlock,
        innerLayerBlock,
        extraInnerBlock
    )

    constructor(
        config: CarverConfiguration,
        horizontalRadiusMultiplier: IntProvider,
        outerLayerBlock: BlockStateProvider,
        middleLayerBlock: BlockStateProvider,
        innerLayerBlock: BlockStateProvider,
        extraInnerBlock: BlockStateProvider,
    ) : this(
        config.probability,
        config.y,
        config.yScale,
        config.lavaLevel,
        config.debugSettings,
        config.replaceable,
        horizontalRadiusMultiplier,
        outerLayerBlock,
        middleLayerBlock,
        innerLayerBlock,
        extraInnerBlock
    )

    companion object {
        val CODEC: Codec<GeodeCarverConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<GeodeCarverConfig> ->
                instance.group(
                    CarverConfiguration.CODEC.forGetter { it },
                    IntProvider.CODEC.fieldOf("horizontal_radius").forGetter { it.horizontalRadius },
                    BlockStateProvider.CODEC
                        .fieldOf("outer_layer")
                        .forGetter { it.outerLayerBlock },
                    BlockStateProvider.CODEC
                        .fieldOf("middle_inner")
                        .forGetter { it.middleLayerBlock },
                    BlockStateProvider.CODEC
                        .fieldOf("inner_inner")
                        .forGetter { it.innerLayerBlock },
                    BlockStateProvider.CODEC
                        .fieldOf("extra_inner_layer")
                        .forGetter { it.extraInnerBlock },
                ).apply(instance, ::GeodeCarverConfig)
            }
    }
}
