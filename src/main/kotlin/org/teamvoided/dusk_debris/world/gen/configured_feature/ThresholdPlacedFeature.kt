package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Holder
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.PlacedFeature

class ThresholdPlacedFeature(val feature: Holder<PlacedFeature>, val threshold: Float) {
    fun generate(
        world: StructureWorldAccess,
        chunkGenerator: ChunkGenerator,
        random: RandomGenerator,
        pos: BlockPos
    ): Boolean {
        return (feature.value() as PlacedFeature).place(world, chunkGenerator, random, pos)
    }

    companion object {
        val CODEC: Codec<ThresholdPlacedFeature> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<ThresholdPlacedFeature> ->
                instance.group(
                    PlacedFeature.REGISTRY_CODEC.fieldOf("feature").forGetter { it.feature },
                    Codec.floatRange(-2f, 2f).fieldOf("threshold").forGetter { it.threshold })
                    .apply(instance, ::ThresholdPlacedFeature)
            }
    }
}
