package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.LeavesBlock
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.structure.Structure
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.BitSetVoxelSet
import net.minecraft.util.shape.VoxelSet
import net.minecraft.world.ModifiableWorld
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.TestableWorld
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.treedecorator.TreeDecorator
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

class TreeFeature(codec: Codec<TreeFeatureConfig>) : Feature<TreeFeatureConfig>(codec) {
    private fun generate(
        world: StructureWorldAccess,
        random: RandomGenerator,
        pos: BlockPos,
        trunkReplacer: BiConsumer<BlockPos, BlockState>,
        foliageReplacer: BiConsumer<BlockPos, BlockState>,
        placer: FoliagePlacer.Placer,
        config: TreeFeatureConfig
    ): Boolean {
        val trunkHeight = config.trunkPlacer.getHeight(random)
        val foliageHeight = config.foliagePlacer.getRandomHeight(random, trunkHeight, config)
        val baseOfFoliage = trunkHeight - foliageHeight
        val foliageRadius = config.foliagePlacer.getRandomRadius(random, baseOfFoliage)
        val trunkBase = config.rootPlacer.map { it.getTrunkOrigin(pos, random) }.orElse(pos)
        val min = min(pos.y, trunkBase.y)
        val max = (max(pos.y, trunkBase.y) + trunkHeight + 1)
        if (min >= world.bottomY + 1 && max <= world.topY) {
            val optionalInt = config.minimumSize.minClippedHeight
            val topPos = this.getTopPosition(world, trunkHeight, trunkBase, config)
            return if (topPos < trunkHeight && (optionalInt.isEmpty || topPos < optionalInt.asInt)) {
                false
            } else if (config.rootPlacer.isPresent &&
                !config.rootPlacer.get().generate(world, trunkReplacer, random, pos, trunkBase, config)
            ) {
                false
            } else {
                val list = config.trunkPlacer.generate(world, foliageReplacer, random, topPos, trunkBase, config)
                list.forEach { node: FoliagePlacer.TreeNode ->
                    config.foliagePlacer.createFoliage(
                        world,
                        placer,
                        random,
                        config,
                        topPos,
                        node,
                        foliageHeight,
                        foliageRadius
                    )
                }
                true
            }
        } else {
            return false
        }
    }

    private fun getTopPosition(world: TestableWorld, height: Int, pos: BlockPos, config: TreeFeatureConfig): Int {
        val mutable = BlockPos.Mutable()

        for (i in 0..height + 1) {
            val minRadius = config.minimumSize.getRadius(height, i)

            for (k in -minRadius..minRadius) {
                for (l in -minRadius..minRadius) {
                    mutable.set(pos, k, i, l)
                    if (!config.trunkPlacer.isReplaceableOrLog(world, mutable) ||
                        !config.ignoreVines && isVine(world, mutable)
                    ) {
                        return i - 2
                    }
                }
            }
        }

        return height
    }

    override fun setBlockState(world: ModifiableWorld, pos: BlockPos, state: BlockState) {
        setBlockStateWithoutUpdatingNeighbors(world, pos, state)
    }

    override fun place(context: FeatureContext<TreeFeatureConfig>): Boolean {
        val structureWorldAccess = context.world
        val randomGenerator = context.random
        val blockPos = context.origin
        val treeFeatureConfig = context.config
        val rootPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val logPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val leafPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val decoratorPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val trunkReplacer = BiConsumer { pos: BlockPos, state: BlockState ->
            rootPositions.add(pos.toImmutable())
            structureWorldAccess.setBlockState(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }
        val foliageReplacer = BiConsumer<BlockPos, BlockState> { pos: BlockPos, state: BlockState? ->
            logPositions.add(pos.toImmutable())
            structureWorldAccess.setBlockState(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }
        val placer: FoliagePlacer.Placer = object : FoliagePlacer.Placer {
            override fun placeBlock(pos: BlockPos, state: BlockState) {
                leafPositions.add(pos.toImmutable())
                structureWorldAccess.setBlockState(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
            }

            override fun hasPlaced(pos: BlockPos): Boolean {
                return leafPositions.contains(pos)
            }
        }
        val replacer = BiConsumer<BlockPos, BlockState> { pos: BlockPos, state: BlockState ->
            decoratorPositions.add(pos.toImmutable())
            structureWorldAccess.setBlockState(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }
        val canGenerate = this.generate(
            structureWorldAccess,
            randomGenerator,
            blockPos,
            trunkReplacer,
            foliageReplacer,
            placer,
            treeFeatureConfig
        )
        if (canGenerate && (logPositions.isNotEmpty() || leafPositions.isNotEmpty())) {
            if (treeFeatureConfig.decorators.isNotEmpty()) {
                val placer2 = TreeDecorator.Placer(
                    structureWorldAccess,
                    replacer,
                    randomGenerator,
                    logPositions,
                    leafPositions,
                    rootPositions
                )
                treeFeatureConfig.decorators.forEach(Consumer { decorator: TreeDecorator ->
                    decorator.generate(placer2)
                })
            }

            return BlockBox.encompassPositions(
                Iterables.concat(
                    rootPositions,
                    logPositions,
                    leafPositions,
                    decoratorPositions
                )
            ).map { box: BlockBox ->
                val voxelSet =
                    updateLeavesDistances(structureWorldAccess, box, logPositions, decoratorPositions, rootPositions)
                Structure.updateCorner(structureWorldAccess, 3, voxelSet, box.minX, box.minY, box.minZ)
                true
            }.orElse(false)
        } else {
            return false
        }
    }

    companion object {
        private const val FORCE_STATE_AND_NOTIFY_ALL = 19

        private fun isVine(world: TestableWorld, pos: BlockPos): Boolean {
            return world.testBlockState(pos) { state: BlockState -> state.isOf(Blocks.VINE) }
        }

        fun isAirOrLeaves(world: TestableWorld, pos: BlockPos?): Boolean {
            return world.testBlockState(pos) { state: BlockState -> state.isAir || state.isIn(BlockTags.LEAVES) }
        }

        private fun setBlockStateWithoutUpdatingNeighbors(world: ModifiableWorld, pos: BlockPos, state: BlockState) {
            world.setBlockState(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }

        fun canReplace(world: TestableWorld, pos: BlockPos?): Boolean {
            return world.testBlockState(pos) { state: BlockState -> state.isAir || state.isIn(BlockTags.REPLACEABLE_BY_TREES) }
        }

        private fun updateLeavesDistances(
            world: WorldAccess,
            box: BlockBox,
            trunkPositions: Set<BlockPos>,
            foliagePositions: Set<BlockPos>,
            rootPositions: Set<BlockPos>
        ): VoxelSet {
            val voxelSet: VoxelSet = BitSetVoxelSet(box.blockCountX, box.blockCountY, box.blockCountZ)
            val list: MutableList<MutableSet<BlockPos>> = Lists.newArrayList()

            for (j in 0..6) {
                list.add(Sets.newHashSet())
            }

            val var22: Iterator<*> = Lists.newArrayList(Sets.union(foliagePositions, rootPositions)).iterator()

            while (var22.hasNext()) {
                val blockPos = var22.next() as BlockPos
                if (box.isInside(blockPos)) {
                    voxelSet[blockPos.x - box.minX, blockPos.y - box.minY] = blockPos.z - box.minZ
                }
            }

            val mutable = BlockPos.Mutable()
            var k = 0
            (list[0]).addAll(trunkPositions)

            while (true) {
                while (k >= 7 || (list[k]).isNotEmpty()) {
                    if (k >= 7) {
                        return voxelSet
                    }

                    val iterator: MutableIterator<BlockPos> = (list[k]).iterator()
                    val blockPos2 = iterator.next()
                    iterator.remove()
                    if (box.isInside(blockPos2)) {
                        if (k != 0) {
                            val blockState = world.getBlockState(blockPos2)
                            setBlockStateWithoutUpdatingNeighbors(
                                world,
                                blockPos2,
                                blockState.with(Properties.DISTANCE_1_7, k) as BlockState
                            )
                        }

                        voxelSet[blockPos2.x - box.minX, blockPos2.y - box.minY] = blockPos2.z - box.minZ
                        val var25 = Direction.entries.toTypedArray()
                        val var13 = var25.size

                        for (var14 in 0 until var13) {
                            val direction = var25[var14]
                            mutable[blockPos2] = direction
                            if (box.isInside(mutable)) {
                                val l = mutable.x - box.minX
                                val m = mutable.y - box.minY
                                val n = mutable.z - box.minZ
                                if (!voxelSet.contains(l, m, n)) {
                                    val blockState2 = world.getBlockState(mutable)
                                    val optionalInt = LeavesBlock.getOptionalDistanceFromLog(blockState2)
                                    if (!optionalInt.isEmpty) {
                                        val o = min(optionalInt.asInt.toDouble(), (k + 1).toDouble())
                                            .toInt()
                                        if (o < 7) {
                                            (list[o]).add(mutable.toImmutable())
                                            k = min(k.toDouble(), o.toDouble()).toInt()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ++k
            }
        }
    }
}

