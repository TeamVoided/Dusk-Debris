package org.teamvoided.dusk_debris.world.gen.configured_carver.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.HolderSet
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.CarverConfig
import net.minecraft.world.gen.carver.CarverDebugConfig
import net.minecraft.world.gen.heightprovider.HeightProvider
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig

class GeodeCarverConfig(
    probability: Float,
    y: HeightProvider,
    yScale: FloatProvider,
    lavaLevel: YOffset,
    debugConfig: CarverDebugConfig,
    replaceableBlocks: HolderSet<Block>,
    val horizontalRadius: IntProvider,
) : CarverConfig(probability, y, yScale, lavaLevel, debugConfig, replaceableBlocks) {
    constructor(
        probability: Float,
        y: HeightProvider,
        yScale: FloatProvider,
        lavaLevel: YOffset,
        replaceableBlocks: HolderSet<Block>,
        horizontalRadiusMultiplier: IntProvider
    ) : this(
        probability,
        y,
        yScale,
        lavaLevel,
        LakeCarverDebugConfig.default(),
        replaceableBlocks,
        horizontalRadiusMultiplier
    )

    constructor(
        config: CarverConfig,
        horizontalRadiusMultiplier: IntProvider
    ) : this(
        config.probability,
        config.y,
        config.yScale,
        config.lavaLevel,
        config.debugConfig,
        config.replaceable,
        horizontalRadiusMultiplier
    )

    companion object {
        val CODEC: Codec<GeodeCarverConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<GeodeCarverConfig> ->
                instance.group(
                    CarverConfig.CODEC.forGetter { it },
                    IntProvider.VALUE_CODEC.fieldOf("horizontal_radius").forGetter { it.horizontalRadius }
                ).apply(instance, ::GeodeCarverConfig)
            }
    }
}
