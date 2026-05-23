package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.LevelWriter
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape
import net.minecraft.world.phys.shapes.DiscreteVoxelShape
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

class TreeFeature(codec: Codec<TreeConfiguration>) : Feature<TreeConfiguration>(codec) {
    private fun generate(
        world: WorldGenLevel,
        random: RandomSource,
        pos: BlockPos,
        trunkReplacer: BiConsumer<BlockPos, BlockState>,
        foliageReplacer: BiConsumer<BlockPos, BlockState>,
        placer: FoliagePlacer.FoliageSetter,
        config: TreeConfiguration
    ): Boolean {
        val trunkHeight = config.trunkPlacer.getTreeHeight(random)
        val foliageHeight = config.foliagePlacer.foliageHeight(random, trunkHeight, config)
        val baseOfFoliage = trunkHeight - foliageHeight
        val foliageRadius = config.foliagePlacer.foliageRadius(random, baseOfFoliage)
        val trunkBase = config.rootPlacer.map { it.getTrunkOrigin(pos, random) }.orElse(pos)
        val min = min(pos.y, trunkBase.y)
        val max = (max(pos.y, trunkBase.y) + trunkHeight + 1)
        if (min >= world.minBuildHeight + 1 && max <= world.maxBuildHeight) {
            val optionalInt = config.minimumSize.minClippedHeight()
            val topPos = this.getTopPosition(world, trunkHeight, trunkBase, config)
            return if (topPos < trunkHeight && (optionalInt.isEmpty || topPos < optionalInt.asInt)) {
                false
            } else if (config.rootPlacer.isPresent &&
                !config.rootPlacer.get().placeRoots(world, trunkReplacer, random, pos, trunkBase, config)
            ) {
                false
            } else {
                val list = config.trunkPlacer.placeTrunk(world, foliageReplacer, random, topPos, trunkBase, config)
                list.forEach { node: FoliagePlacer.FoliageAttachment ->
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

    private fun getTopPosition(world: LevelSimulatedReader, height: Int, pos: BlockPos, config: TreeConfiguration): Int {
        val mutable = BlockPos.MutableBlockPos()

        for (i in 0..height + 1) {
            val minRadius = config.minimumSize.getSizeAtHeight(height, i)

            for (k in -minRadius..minRadius) {
                for (l in -minRadius..minRadius) {
                    mutable.setWithOffset(pos, k, i, l)
                    if (!config.trunkPlacer.isFree(world, mutable) ||
                        !config.ignoreVines && isVine(world, mutable)
                    ) {
                        return i - 2
                    }
                }
            }
        }

        return height
    }

    override fun setBlock(world: LevelWriter, pos: BlockPos, state: BlockState) {
        setBlockStateWithoutUpdatingNeighbors(world, pos, state)
    }

    override fun place(context: FeaturePlaceContext<TreeConfiguration>): Boolean {
        val structureWorldAccess = context.level()
        val randomGenerator = context.random()
        val blockPos = context.origin()
        val treeFeatureConfig = context.config()
        val rootPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val logPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val leafPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val decoratorPositions: MutableSet<BlockPos> = Sets.newHashSet()
        val trunkReplacer = BiConsumer { pos: BlockPos, state: BlockState ->
            rootPositions.add(pos.immutable())
            structureWorldAccess.setBlock(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }
        val foliageReplacer = BiConsumer<BlockPos, BlockState> { pos: BlockPos, state: BlockState? ->
            logPositions.add(pos.immutable())
            structureWorldAccess.setBlock(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }
        val placer: FoliagePlacer.FoliageSetter = object : FoliagePlacer.FoliageSetter {
            override fun set(pos: BlockPos, state: BlockState) {
                leafPositions.add(pos.immutable())
                structureWorldAccess.setBlock(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
            }

            override fun isSet(pos: BlockPos): Boolean {
                return leafPositions.contains(pos)
            }
        }
        val replacer = BiConsumer<BlockPos, BlockState> { pos: BlockPos, state: BlockState ->
            decoratorPositions.add(pos.immutable())
            structureWorldAccess.setBlock(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
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
                val placer2 = TreeDecorator.Context(
                    structureWorldAccess,
                    replacer,
                    randomGenerator,
                    logPositions,
                    leafPositions,
                    rootPositions
                )
                treeFeatureConfig.decorators.forEach(Consumer { decorator: TreeDecorator ->
                    decorator.place(placer2)
                })
            }

            return BoundingBox.encapsulatingPositions(
                Iterables.concat(
                    rootPositions,
                    logPositions,
                    leafPositions,
                    decoratorPositions
                )
            ).map { box: BoundingBox ->
                val voxelSet =
                    updateLeavesDistances(structureWorldAccess, box, logPositions, decoratorPositions, rootPositions)
                StructureTemplate.updateShapeAtEdge(structureWorldAccess, 3, voxelSet, box.minX(), box.minY(), box.minZ())
                true
            }.orElse(false)
        } else {
            return false
        }
    }

    companion object {
        private const val FORCE_STATE_AND_NOTIFY_ALL = 19

        private fun isVine(world: LevelSimulatedReader, pos: BlockPos): Boolean {
            return world.isStateAtPosition(pos) { state: BlockState -> state.`is`(Blocks.VINE) }
        }

        fun isAirOrLeaves(world: LevelSimulatedReader, pos: BlockPos?): Boolean {
            return world.isStateAtPosition(pos) { state: BlockState -> state.isAir || state.`is`(BlockTags.LEAVES) }
        }

        private fun setBlockStateWithoutUpdatingNeighbors(world: LevelWriter, pos: BlockPos, state: BlockState) {
            world.setBlock(pos, state, FORCE_STATE_AND_NOTIFY_ALL)
        }

        fun canReplace(world: LevelSimulatedReader, pos: BlockPos?): Boolean {
            return world.isStateAtPosition(pos) { state: BlockState -> state.isAir || state.`is`(BlockTags.REPLACEABLE_BY_TREES) }
        }

        private fun updateLeavesDistances(
            world: LevelAccessor,
            box: BoundingBox,
            trunkPositions: Set<BlockPos>,
            foliagePositions: Set<BlockPos>,
            rootPositions: Set<BlockPos>
        ): DiscreteVoxelShape {
            val voxelSet: DiscreteVoxelShape = BitSetDiscreteVoxelShape(box.xSpan, box.ySpan, box.zSpan)
            val list: MutableList<MutableSet<BlockPos>> = Lists.newArrayList()

            for (j in 0..6) {
                list.add(Sets.newHashSet())
            }

            val var22: Iterator<*> = Lists.newArrayList(Sets.union(foliagePositions, rootPositions)).iterator()

            while (var22.hasNext()) {
                val blockPos = var22.next() as BlockPos
                if (box.isInside(blockPos)) {
                    voxelSet.fill(blockPos.x - box.minX(), blockPos.y - box.minY(), blockPos.z - box.minZ())
                }
            }

            val mutable = BlockPos.MutableBlockPos()
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
                                blockState.setValue(BlockStateProperties.DISTANCE, k) as BlockState
                            )
                        }

                        voxelSet.fill(blockPos2.x - box.minX(), blockPos2.y - box.minY(), blockPos2.z - box.minZ())
                        val var25 = Direction.entries.toTypedArray()
                        val var13 = var25.size

                        for (var14 in 0 until var13) {
                            val direction = var25[var14]
                            mutable.setWithOffset(blockPos2, direction)
                            if (box.isInside(mutable)) {
                                val l = mutable.x - box.minX()
                                val m = mutable.y - box.minY()
                                val n = mutable.z - box.minZ()
                                if (!voxelSet.isFull(l, m, n)) {
                                    val blockState2 = world.getBlockState(mutable)
                                    val optionalInt = LeavesBlock.getOptionalDistanceAt(blockState2)
                                    if (!optionalInt.isEmpty) {
                                        val o = min(optionalInt.asInt.toDouble(), (k + 1).toDouble())
                                            .toInt()
                                        if (o < 7) {
                                            (list[o]).add(mutable.immutable())
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

