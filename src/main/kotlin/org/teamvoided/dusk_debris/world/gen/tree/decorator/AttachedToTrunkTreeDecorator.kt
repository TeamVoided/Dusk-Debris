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
    val blockProvider: BlockStateProvider,
    val directions: List<Direction>
) : TreeDecorator() {
    override fun generate(placer: Placer) {
        val set: MutableSet<BlockPos> = HashSet()
        val randomGenerator = placer.random

        val var4: Iterator<BlockPos> =            Util.copyShuffled(placer.logPositions, randomGenerator) .iterator()

        while (true) {
            var pos: BlockPos
            var direction: Direction
            var posOffset: BlockPos
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


            placer.replace(posOffset, blockProvider.getBlockState(randomGenerator, posOffset))
        }
    }

    override fun getType(): TreeDecoratorType<AttachedToTrunkTreeDecorator> = DuskTreeStuff.ATTACHED_TO_TRUNK_DECORATOR

    companion object {
        val CODEC: MapCodec<AttachedToTrunkTreeDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.floatRange(0f, 1f).fieldOf("probability").forGetter { it.probability },
                BlockStateProvider.TYPE_CODEC.fieldOf("block_provider").forGetter { it.blockProvider },
                Codecs.withNonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter { it.directions },
            ).apply(instance, ::AttachedToTrunkTreeDecorator)
        }
    }
}
