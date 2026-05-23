package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer

class OvergrowthTrunkPlacer(
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
        val saplingFacing: Direction
        if (world.isStateAtPosition(startPos) { it.hasProperty(BlockStateProperties.FACING) }) {
            var f = Direction.DOWN
            for (i in 0..Direction.entries.size) {
                val dir = Direction.from3DDataValue(i)
                if (world.isStateAtPosition(startPos) { it.getValue(BlockStateProperties.FACING) == dir }) {
                    f = dir
                    break
                }
            }
            saplingFacing = f
        } else {
            saplingFacing = Direction.DOWN
        }

        val dirtPos = mutable.move(saplingFacing)
        setDirtAt(world, replacer, random, dirtPos, config)

        val list: MutableList<FoliagePlacer.FoliageAttachment> = ArrayList()




        return list
    }

    companion object {
        val CODEC: MapCodec<OvergrowthTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<OvergrowthTrunkPlacer> ->
                trunkPlacerParts(instance).and(
                    instance.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1)
                            .forGetter { placer: OvergrowthTrunkPlacer -> placer.minHeightForLeaves },
                        IntProvider.codec(1, 64).fieldOf("bend_length")
                            .forGetter { placer: OvergrowthTrunkPlacer -> placer.bendLength })
                ).apply(instance, ::OvergrowthTrunkPlacer)
            }
    }
}