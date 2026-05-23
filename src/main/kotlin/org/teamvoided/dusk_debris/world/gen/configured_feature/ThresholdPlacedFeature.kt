package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.placement.PlacedFeature

class ThresholdPlacedFeature(val feature: Holder<PlacedFeature>, val threshold: Float) {
    fun generate(
        world: WorldGenLevel,
        chunkGenerator: ChunkGenerator,
        random: RandomSource,
        pos: BlockPos
    ): Boolean {
        return (feature.value() as PlacedFeature).place(world, chunkGenerator, random, pos)
    }

    companion object {
        val CODEC: Codec<ThresholdPlacedFeature> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<ThresholdPlacedFeature> ->
                instance.group(
                    PlacedFeature.CODEC.fieldOf("feature").forGetter { it.feature },
                    Codec.floatRange(-2f, 2f).fieldOf("threshold").forGetter { it.threshold })
                    .apply(instance, ::ThresholdPlacedFeature)
            }
    }
}
