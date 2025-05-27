package org.teamvoided.dusk_debris.world.gen.tree.decorator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.Util
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.treedecorator.TreeDecoratorType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class AttachedToTrunkTreeDecorator(
    val probability: Float,
    val xzExclusionRadius: Int,
    val yExclusionRadius: Int,
    val blockProvider: BlockStateProvider,
    val requiredEmptyBlocks: Int,
    val directions: List<Direction>
) : TreeDecorator() {
    override fun generate(placer: Placer) {
        val set: MutableSet<BlockPos> = HashSet()
        val randomGenerator = placer.random

        val var4: Iterator<BlockPos> = Util.copyShuffled(placer.logPositions, randomGenerator).iterator()

        while (true) {
            var pos: BlockPos
            var direction: Direction
            var posOffset: BlockPos
            do {
                do {
                    do {
                        if (!var4.hasNext()) {
                            return
                        }

                        pos = var4.next()
                        direction = Util.getRandom(this.directions, randomGenerator)
                        posOffset = pos.offset(direction)
                    } while (set.contains(posOffset))
                } while (!(randomGenerator.nextFloat() < this.probability))
            } while (!this.hasRequiredEmptyBlocks(placer, pos, direction))

            val boxCorner1 = posOffset.add(-this.xzExclusionRadius, -this.yExclusionRadius, -this.xzExclusionRadius)
            val boxCorner2 = posOffset.add(this.xzExclusionRadius, this.yExclusionRadius, this.xzExclusionRadius)
            val var10: Iterator<BlockPos> = BlockPos.iterate(boxCorner1, boxCorner2).iterator()

            while (var10.hasNext()) {
                val blockPos5 = var10.next()
                set.add(blockPos5.toImmutable())
            }

            placer.replace(posOffset, blockProvider.getBlockState(randomGenerator, posOffset))
        }
    }

    private fun hasRequiredEmptyBlocks(placer: Placer, pos: BlockPos, direction: Direction): Boolean {
        for (i in 1..this.requiredEmptyBlocks) {
            val blockPos = pos.offset(direction, i)
            if (!placer.isAir(blockPos)) {
                return false
            }
        }

        return true
    }

    override fun getType(): TreeDecoratorType<AttachedToTrunkTreeDecorator> = DuskTreeStuff.ATTACHED_TO_TRUNK_DECORATOR

    companion object {
        val CODEC: MapCodec<AttachedToTrunkTreeDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.floatRange(0f, 1f).fieldOf("probability").forGetter { it.probability },
                Codec.intRange(0, 16).fieldOf("xz_exclusion_radius").forGetter { it.xzExclusionRadius },
                Codec.intRange(0, 16).fieldOf("y_exclusion_radius").forGetter { it.yExclusionRadius },
                BlockStateProvider.TYPE_CODEC.fieldOf("block_provider").forGetter { it.blockProvider },
                Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter { it.requiredEmptyBlocks },
                Codecs.withNonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter { it.directions },
            ).apply(instance, ::AttachedToTrunkTreeDecorator)
        }
    }
}
