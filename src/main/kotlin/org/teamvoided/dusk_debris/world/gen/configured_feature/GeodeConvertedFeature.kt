package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Lists
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.BuddingAmethystBlock
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.GeodeFeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import kotlin.math.sqrt

class GeodeConvertedFeature(codec: Codec<GeodeFeatureConfig>) :
    Feature<GeodeFeatureConfig>(codec) {
    override fun place(context: FeatureContext<GeodeFeatureConfig>): Boolean {
        val geodeFeatureConfig = context.config as GeodeFeatureConfig
        val randomGenerator = context.random
        val blockPos = context.origin
        val structureWorldAccess = context.world
        val i = geodeFeatureConfig.minGenOffset
        val j = geodeFeatureConfig.maxGenOffset
        val list: MutableList<Pair<BlockPos, Int>> = Lists.newLinkedList()
        val k = geodeFeatureConfig.distributionPoints[randomGenerator]
        val chunkRandom = ChunkRandom(LegacySimpleRandom(structureWorldAccess.seed))
        val doublePerlinNoiseSampler = DoublePerlinNoiseSampler.create(chunkRandom, -4, *doubleArrayOf(1.0))
        val list2: MutableList<BlockPos> = Lists.newLinkedList()
        val d = k.toDouble() / geodeFeatureConfig.outerWallDistance.max.toDouble()
        val geodeLayerThicknessConfig = geodeFeatureConfig.layerThicknessConfig
        val geodeLayerConfig = geodeFeatureConfig.layerConfig
        val geodeCrackConfig = geodeFeatureConfig.crackConfig
        val e = 1.0 / sqrt(geodeLayerThicknessConfig.filling)
        val f = 1.0 / sqrt(geodeLayerThicknessConfig.innerLayer + d)
        val g = 1.0 / sqrt(geodeLayerThicknessConfig.middleLayer + d)
        val h = 1.0 / sqrt(geodeLayerThicknessConfig.outerLayer + d)
        val l =
            1.0 / sqrt(geodeCrackConfig.baseCrackSize + randomGenerator.nextDouble() / 2.0 + (if (k > 3) d else 0.0))
        val bl = randomGenerator.nextFloat().toDouble() < geodeCrackConfig.generateCrackChance
        var m = 0

        var n: Int
        var o: Int
        var blockPos2: BlockPos
        var blockState: BlockState
        n = 0
        while (n < k) {
            o = geodeFeatureConfig.outerWallDistance[randomGenerator]
            val p = geodeFeatureConfig.outerWallDistance[randomGenerator]
            val q = geodeFeatureConfig.outerWallDistance[randomGenerator]
            blockPos2 = blockPos.add(o, p, q)
            blockState = structureWorldAccess.getBlockState(blockPos2)
            if (blockState.isAir || blockState.isIn(BlockTags.GEODE_INVALID_BLOCKS)) {
                ++m
                if (m > geodeFeatureConfig.invalidBlocksThreshold) {
                    return false
                }
            }

            list.add(
                Pair.of(
                    blockPos2,
                    geodeFeatureConfig.pointOffset[randomGenerator]
                )
            )
            ++n
        }

        if (bl) {
            n = randomGenerator.nextInt(4)
            o = k * 2 + 1
            if (n == 0) {
                list2.add(blockPos.add(o, 7, 0))
                list2.add(blockPos.add(o, 5, 0))
                list2.add(blockPos.add(o, 1, 0))
            } else if (n == 1) {
                list2.add(blockPos.add(0, 7, o))
                list2.add(blockPos.add(0, 5, o))
                list2.add(blockPos.add(0, 1, o))
            } else if (n == 2) {
                list2.add(blockPos.add(o, 7, o))
                list2.add(blockPos.add(o, 5, o))
                list2.add(blockPos.add(o, 1, o))
            } else {
                list2.add(blockPos.add(0, 7, 0))
                list2.add(blockPos.add(0, 5, 0))
                list2.add(blockPos.add(0, 1, 0))
            }
        }

        val list3: MutableList<BlockPos> = Lists.newArrayList()
        val predicate = notInBlockTagPredicate(geodeFeatureConfig.layerConfig.cannotReplace)
        val var48: Iterator<*> = BlockPos.iterate(blockPos.add(i, i, i), blockPos.add(j, j, j)).iterator()

        while (true) {
            while (true) {
                var s: Double
                var t: Double
                var blockPos3: BlockPos
                do {
                    if (!var48.hasNext()) {
                        val list4 = geodeLayerConfig.innerBlocks
                        val var51: Iterator<*> = list3.iterator()

                        while (true) {
                            while (var51.hasNext()) {
                                blockPos2 = var51.next() as BlockPos
                                blockState = Util.getRandom(list4, randomGenerator)
                                val var53: Array<Direction> = DIRECTIONS
                                val var37 = var53.size

                                for (var54 in 0 until var37) {
                                    val direction2 = var53[var54]
                                    if (blockState.contains(Properties.FACING)) {
                                        blockState = blockState.with(Properties.FACING, direction2)
                                    }

                                    val blockPos6 = blockPos2.offset(direction2)
                                    val blockState2 = structureWorldAccess.getBlockState(blockPos6)
                                    if (blockState.contains(Properties.WATERLOGGED)) {
                                        blockState = blockState.with(
                                            Properties.WATERLOGGED,
                                            blockState2.fluidState.isSource
                                        ) as BlockState
                                    }

                                    if (BuddingAmethystBlock.canGrowIn(blockState2)) {
                                        this.setBlockStateIf(structureWorldAccess, blockPos6, blockState, predicate)
                                        break
                                    }
                                }
                            }

                            return true
                        }
                    }

                    blockPos3 = var48.next() as BlockPos
                    val r = doublePerlinNoiseSampler.sample(
                        blockPos3.x.toDouble(),
                        blockPos3.y.toDouble(),
                        blockPos3.z.toDouble()
                    ) * geodeFeatureConfig.noiseMultiplier
                    s = 0.0
                    t = 0.0

                    val iterator: Iterator<Pair<BlockPos, Int>> = list.iterator()
                    var pair: Pair<BlockPos, Int>
                    while (iterator.hasNext()) {
                        pair = iterator.next()
                        s += MathHelper.inverseSqrt(blockPos3.getSquaredDistance(pair.first as Vec3i) + (pair.second as Int).toDouble()) + r
                    }

                    var blockPos4: BlockPos
                    val iterator2: MutableIterator<BlockPos> = list2.iterator()
                    while (iterator2.hasNext()) {
                        blockPos4 = iterator2.next()
                        t += MathHelper.inverseSqrt(blockPos3.getSquaredDistance(blockPos4) + geodeCrackConfig.crackPointOffset.toDouble()) + r
                    }
                } while (s < h)

                if (bl && t >= l && s < e) {
                    this.setBlockStateIf(structureWorldAccess, blockPos3, Blocks.AIR.defaultState, predicate)
                    val var56: Array<Direction> = DIRECTIONS
                    val var59 = var56.size

                    for (var42 in 0 until var59) {
                        val direction = var56[var42]
                        val blockPos5 = blockPos3.offset(direction)
                        val fluidState = structureWorldAccess.getFluidState(blockPos5)
                        if (!fluidState.isEmpty) {
                            structureWorldAccess.scheduleFluidTick(blockPos5, fluidState.fluid, 0)
                        }
                    }
                } else if (s >= e) {
                    this.setBlockStateIf(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.fillingProvider.getBlockState(randomGenerator, blockPos3),
                        predicate
                    )
                } else if (s >= f) {
                    val bl2 = randomGenerator.nextFloat().toDouble() < geodeFeatureConfig.useAlternateLayer0Chance
                    if (bl2) {
                        this.setBlockStateIf(
                            structureWorldAccess,
                            blockPos3,
                            geodeLayerConfig.alternateInnerLayerProvider.getBlockState(randomGenerator, blockPos3),
                            predicate
                        )
                    } else {
                        this.setBlockStateIf(
                            structureWorldAccess,
                            blockPos3,
                            geodeLayerConfig.innerLayerProvider.getBlockState(randomGenerator, blockPos3),
                            predicate
                        )
                    }

                    if ((!geodeFeatureConfig.placementsRequireLayer0Alternate || bl2) && randomGenerator.nextFloat()
                            .toDouble() < geodeFeatureConfig.usePotentialPlacementsChance
                    ) {
                        list3.add(blockPos3.toImmutable())
                    }
                } else if (s >= g) {
                    this.setBlockStateIf(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.middleLayerProvider.getBlockState(randomGenerator, blockPos3),
                        predicate
                    )
                } else if (s >= h) {
                    this.setBlockStateIf(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.outerLayerProvider.getBlockState(randomGenerator, blockPos3),
                        predicate
                    )
                }
            }
        }
    }

    companion object {
        private val DIRECTIONS = Direction.entries.toTypedArray()
    }
}