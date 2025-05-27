package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.google.common.collect.Lists
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import java.util.function.BiConsumer

class BlueTrunkPlacer(
    baseHeight: Int,
    firstRandomHeight: Int,
    secondRandomHeight: Int,
    private val minHeightForLeaves: Int,
    private val bendLength: IntProvider
) : TrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight) {
    override fun getType(): TrunkPlacerType<*> {
        return TrunkPlacerType.BENDING_TRUNK_PLACER
    }

    override fun generate(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        height: Int,
        startPos: BlockPos,
        config: TreeFeatureConfig
    ): List<FoliagePlacer.TreeNode> {
        val direction = Direction.Type.HORIZONTAL.random(random)
        val treeHeight = height - 1
        val mutable = startPos.mutableCopy()
        val downPos = mutable.down()
        setToDirt(world, replacer, random, downPos, config)
        val list: MutableList<FoliagePlacer.TreeNode> = Lists.newArrayList()
        var loop = 0
        while (loop <= treeHeight) {
            if (loop + 1 >= treeHeight + random.nextInt(2)) {
                mutable.move(direction)
            }

            if (TreeFeature.canReplace(world, mutable)) {
                this.placeTrunkBlock(world, replacer, random, mutable, config)
            }

            if (loop >= this.minHeightForLeaves) {
                list.add(FoliagePlacer.TreeNode(mutable.toImmutable(), 0, false))
            }

            mutable.move(Direction.UP)
            ++loop
        }

        loop = bendLength[random]

        for (k in 0..loop) {
            if (TreeFeature.canReplace(world, mutable)) {
                this.placeTrunkBlock(world, replacer, random, mutable, config)
            }

            list.add(FoliagePlacer.TreeNode(mutable.toImmutable(), 0, false))
            mutable.move(direction)
        }

        return list
    }

    companion object {
        val CODEC: MapCodec<BlueTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<BlueTrunkPlacer> ->
                fillTrunkPlacerFields(instance).and(
                    instance.group(
                        Codecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1)
                            .forGetter { placer: BlueTrunkPlacer -> placer.minHeightForLeaves },
                        IntProvider.method_35004(1, 64).fieldOf("bend_length")
                            .forGetter { placer: BlueTrunkPlacer -> placer.bendLength })
                ).apply(instance, ::BlueTrunkPlacer)
            }
    }
}