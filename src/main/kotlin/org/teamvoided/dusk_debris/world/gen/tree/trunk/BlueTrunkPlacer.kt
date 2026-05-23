package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.google.common.collect.Lists
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer

class BlueTrunkPlacer(
    baseHeight: Int,
    firstRandomHeight: Int,
    secondRandomHeight: Int,
    private val minHeightForLeaves: Int,
    private val bendLength: IntProvider
) : TrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight) {
    override fun type(): TrunkPlacerType<*> {
        return TrunkPlacerType.BENDING_TRUNK_PLACER
    }

    override fun placeTrunk(
        world: LevelSimulatedReader,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        height: Int,
        startPos: BlockPos,
        config: TreeConfiguration
    ): List<FoliagePlacer.FoliageAttachment> {
        val direction = Direction.Plane.HORIZONTAL.getRandomDirection(random)
        val treeHeight = height - 1
        val mutable = startPos.mutable()
        val downPos = mutable.below()
        setDirtAt(world, replacer, random, downPos, config)
        val list: MutableList<FoliagePlacer.FoliageAttachment> = Lists.newArrayList()
        var loop = 0
        while (loop <= treeHeight) {
            if (loop + 1 >= treeHeight + random.nextInt(2)) {
                mutable.move(direction)
            }

            if (TreeFeature.validTreePos(world, mutable)) {
                this.placeLog(world, replacer, random, mutable, config)
            }

            if (loop >= this.minHeightForLeaves) {
                list.add(FoliagePlacer.FoliageAttachment(mutable.immutable(), 0, false))
            }

            mutable.move(Direction.UP)
            ++loop
        }

        loop = bendLength.sample(random)

        for (k in 0..loop) {
            if (TreeFeature.validTreePos(world, mutable)) {
                this.placeLog(world, replacer, random, mutable, config)
            }

            list.add(FoliagePlacer.FoliageAttachment(mutable.immutable(), 0, false))
            mutable.move(direction)
        }

        return list
    }

    companion object {
        val CODEC: MapCodec<BlueTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<BlueTrunkPlacer> ->
                trunkPlacerParts(instance).and(
                    instance.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1)
                            .forGetter { placer: BlueTrunkPlacer -> placer.minHeightForLeaves },
                        IntProvider.codec(1, 64).fieldOf("bend_length")
                            .forGetter { placer: BlueTrunkPlacer -> placer.bendLength })
                ).apply(instance, ::BlueTrunkPlacer)
            }
    }
}