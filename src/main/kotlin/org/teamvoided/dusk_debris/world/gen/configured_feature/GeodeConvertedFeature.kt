package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Lists
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BuddingAmethystBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration
import net.minecraft.world.level.levelgen.synth.NormalNoise
import kotlin.math.sqrt

class GeodeConvertedFeature(codec: Codec<GeodeConfiguration>) :
    Feature<GeodeConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<GeodeConfiguration>): Boolean {
        val geodeFeatureConfig = context.config() as GeodeConfiguration
        val randomGenerator = context.random()
        val blockPos = context.origin()
        val structureWorldAccess = context.level()
        val i = geodeFeatureConfig.minGenOffset
        val j = geodeFeatureConfig.maxGenOffset
        val list: MutableList<Pair<BlockPos, Int>> = Lists.newLinkedList()
        val k = geodeFeatureConfig.distributionPoints.sample(randomGenerator)
        val chunkRandom = WorldgenRandom(LegacyRandomSource(structureWorldAccess.seed))
        val doublePerlinNoiseSampler = NormalNoise.create(chunkRandom, -4, *doubleArrayOf(1.0))
        val list2: MutableList<BlockPos> = Lists.newLinkedList()
        val d = k.toDouble() / geodeFeatureConfig.outerWallDistance.maxValue.toDouble()
        val geodeLayerThicknessConfig = geodeFeatureConfig.geodeLayerSettings
        val geodeLayerConfig = geodeFeatureConfig.geodeBlockSettings
        val geodeCrackConfig = geodeFeatureConfig.geodeCrackSettings
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
            o = geodeFeatureConfig.outerWallDistance.sample(randomGenerator)
            val p = geodeFeatureConfig.outerWallDistance.sample(randomGenerator)
            val q = geodeFeatureConfig.outerWallDistance.sample(randomGenerator)
            blockPos2 = blockPos.offset(o, p, q)
            blockState = structureWorldAccess.getBlockState(blockPos2)
            if (blockState.isAir || blockState.`is`(BlockTags.GEODE_INVALID_BLOCKS)) {
                ++m
                if (m > geodeFeatureConfig.invalidBlocksThreshold) {
                    return false
                }
            }

            list.add(
                Pair.of(
                    blockPos2,
                    geodeFeatureConfig.pointOffset.sample(randomGenerator)
                )
            )
            ++n
        }

        if (bl) {
            n = randomGenerator.nextInt(4)
            o = k * 2 + 1
            if (n == 0) {
                list2.add(blockPos.offset(o, 7, 0))
                list2.add(blockPos.offset(o, 5, 0))
                list2.add(blockPos.offset(o, 1, 0))
            } else if (n == 1) {
                list2.add(blockPos.offset(0, 7, o))
                list2.add(blockPos.offset(0, 5, o))
                list2.add(blockPos.offset(0, 1, o))
            } else if (n == 2) {
                list2.add(blockPos.offset(o, 7, o))
                list2.add(blockPos.offset(o, 5, o))
                list2.add(blockPos.offset(o, 1, o))
            } else {
                list2.add(blockPos.offset(0, 7, 0))
                list2.add(blockPos.offset(0, 5, 0))
                list2.add(blockPos.offset(0, 1, 0))
            }
        }

        val list3: MutableList<BlockPos> = Lists.newArrayList()
        val predicate = isReplaceable(geodeFeatureConfig.geodeBlockSettings.cannotReplace)
        val var48: Iterator<*> = BlockPos.betweenClosed(blockPos.offset(i, i, i), blockPos.offset(j, j, j)).iterator()

        while (true) {
            while (true) {
                var s: Double
                var t: Double
                var blockPos3: BlockPos
                do {
                    if (!var48.hasNext()) {
                        val list4 = geodeLayerConfig.innerPlacements
                        val var51: Iterator<*> = list3.iterator()

                        while (true) {
                            while (var51.hasNext()) {
                                blockPos2 = var51.next() as BlockPos
                                blockState = Util.getRandom(list4, randomGenerator)
                                val var53: Array<Direction> = DIRECTIONS
                                val var37 = var53.size

                                for (var54 in 0 until var37) {
                                    val direction2 = var53[var54]
                                    if (blockState.hasProperty(BlockStateProperties.FACING)) {
                                        blockState = blockState.setValue(BlockStateProperties.FACING, direction2)
                                    }

                                    val blockPos6 = blockPos2.relative(direction2)
                                    val blockState2 = structureWorldAccess.getBlockState(blockPos6)
                                    if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                                        blockState = blockState.setValue(
                                            BlockStateProperties.WATERLOGGED,
                                            blockState2.fluidState.isSource
                                        ) as BlockState
                                    }

                                    if (BuddingAmethystBlock.canClusterGrowAtState(blockState2)) {
                                        this.safeSetBlock(structureWorldAccess, blockPos6, blockState, predicate)
                                        break
                                    }
                                }
                            }

                            return true
                        }
                    }

                    blockPos3 = var48.next() as BlockPos
                    val r = doublePerlinNoiseSampler.getValue(
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
                        s += Mth.invSqrt(blockPos3.distSqr(pair.first as Vec3i) + (pair.second as Int).toDouble()) + r
                    }

                    var blockPos4: BlockPos
                    val iterator2: MutableIterator<BlockPos> = list2.iterator()
                    while (iterator2.hasNext()) {
                        blockPos4 = iterator2.next()
                        t += Mth.invSqrt(blockPos3.distSqr(blockPos4) + geodeCrackConfig.crackPointOffset.toDouble()) + r
                    }
                } while (s < h)

                if (bl && t >= l && s < e) {
                    this.safeSetBlock(structureWorldAccess, blockPos3, Blocks.AIR.defaultBlockState(), predicate)
                    val var56: Array<Direction> = DIRECTIONS
                    val var59 = var56.size

                    for (var42 in 0 until var59) {
                        val direction = var56[var42]
                        val blockPos5 = blockPos3.relative(direction)
                        val fluidState = structureWorldAccess.getFluidState(blockPos5)
                        if (!fluidState.isEmpty) {
                            structureWorldAccess.scheduleTick(blockPos5, fluidState.type, 0)
                        }
                    }
                } else if (s >= e) {
                    this.safeSetBlock(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.fillingProvider.getState(randomGenerator, blockPos3),
                        predicate
                    )
                } else if (s >= f) {
                    val bl2 = randomGenerator.nextFloat().toDouble() < geodeFeatureConfig.useAlternateLayer0Chance
                    if (bl2) {
                        this.safeSetBlock(
                            structureWorldAccess,
                            blockPos3,
                            geodeLayerConfig.alternateInnerLayerProvider.getState(randomGenerator, blockPos3),
                            predicate
                        )
                    } else {
                        this.safeSetBlock(
                            structureWorldAccess,
                            blockPos3,
                            geodeLayerConfig.innerLayerProvider.getState(randomGenerator, blockPos3),
                            predicate
                        )
                    }

                    if ((!geodeFeatureConfig.placementsRequireLayer0Alternate || bl2) && randomGenerator.nextFloat()
                            .toDouble() < geodeFeatureConfig.usePotentialPlacementsChance
                    ) {
                        list3.add(blockPos3.immutable())
                    }
                } else if (s >= g) {
                    this.safeSetBlock(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.middleLayerProvider.getState(randomGenerator, blockPos3),
                        predicate
                    )
                } else if (s >= h) {
                    this.safeSetBlock(
                        structureWorldAccess,
                        blockPos3,
                        geodeLayerConfig.outerLayerProvider.getState(randomGenerator, blockPos3),
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