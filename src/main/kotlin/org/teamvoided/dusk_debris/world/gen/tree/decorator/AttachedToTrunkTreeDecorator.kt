package org.teamvoided.dusk_debris.world.gen.tree.decorator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class AttachedToTrunkTreeDecorator(
    val probability: Float,
    val blockProvider: BlockStateProvider,
    val directions: List<Direction>
) : TreeDecorator() {
    override fun place(placer: Context) {
        val set: MutableSet<BlockPos> = HashSet()
        val randomGenerator = placer.random()

        val var4: Iterator<BlockPos> =            Util.shuffledCopy(placer.logs(), randomGenerator) .iterator()

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
                    posOffset = pos.relative(direction)
                } while (set.contains(posOffset))
            } while (!(randomGenerator.nextFloat() < this.probability))


            placer.setBlock(posOffset, blockProvider.getState(randomGenerator, posOffset))
        }
    }

    override fun type(): TreeDecoratorType<AttachedToTrunkTreeDecorator> = DuskTreeStuff.ATTACHED_TO_TRUNK_DECORATOR

    companion object {
        val CODEC: MapCodec<AttachedToTrunkTreeDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.floatRange(0f, 1f).fieldOf("probability").forGetter { it.probability },
                BlockStateProvider.CODEC.fieldOf("block_provider").forGetter { it.blockProvider },
                ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter { it.directions },
            ).apply(instance, ::AttachedToTrunkTreeDecorator)
        }
    }
}
