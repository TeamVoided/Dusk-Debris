package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import java.util.function.BiConsumer

class OvergrowthTrunkPlacer(
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
        val saplingFacing: Direction
        if (world.testBlockState(startPos) { it.contains(Properties.FACING) }) {
            var f = Direction.DOWN
            for (i in 0..Direction.entries.size) {
                val dir = Direction.byId(i)
                if (world.testBlockState(startPos) { it.get(Properties.FACING) == dir }) {
                    f = dir
                    break
                }
            }
            saplingFacing = f
        } else {
            saplingFacing = Direction.DOWN
        }

        val dirtPos = mutable.move(saplingFacing)
        setToDirt(world, replacer, random, dirtPos, config)

        val list: MutableList<FoliagePlacer.TreeNode> = ArrayList()




        return list
    }

    companion object {
        val CODEC: MapCodec<OvergrowthTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<OvergrowthTrunkPlacer> ->
                fillTrunkPlacerFields(instance).and(
                    instance.group(
                        Codecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1)
                            .forGetter { placer: OvergrowthTrunkPlacer -> placer.minHeightForLeaves },
                        IntProvider.method_35004(1, 64).fieldOf("bend_length")
                            .forGetter { placer: OvergrowthTrunkPlacer -> placer.bendLength })
                ).apply(instance, ::OvergrowthTrunkPlacer)
            }
    }
}