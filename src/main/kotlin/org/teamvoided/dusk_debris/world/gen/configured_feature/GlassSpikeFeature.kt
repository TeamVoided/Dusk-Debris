package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Lists
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.util.inBlockTagPredicate
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig

class GlassSpikeFeature(codec: Codec<GlassSpikeFeatureConfig>) :
    Feature<GlassSpikeFeatureConfig>(codec) {
    override fun place(context: FeaturePlaceContext<GlassSpikeFeatureConfig>): Boolean {
        val config = context.config() as GlassSpikeFeatureConfig
        val random = context.random()
        val origin = context.origin()
        val world = context.level()
        val minOffset = config.minGenOffset
        val maxOffset = config.maxGenOffset
        val listDistPoints: MutableList<Pair<BlockPos, Int>> = Lists.newLinkedList()
        val distributionPointsAmount = config.distributionPoints.sample(random)
        val chunkRandom = WorldgenRandom(LegacyRandomSource(world.seed))
        val dps1 = NormalNoise.create(chunkRandom, -3, *doubleArrayOf(1.0))
        val dps2 = NormalNoise.create(chunkRandom, -3, *doubleArrayOf(1.0, 2.0))
        val radius = 1.0 / 2.0


        var distPointPos: BlockPos
        while (listDistPoints.size < distributionPointsAmount) {
            val pointX = config.outerWallDistanceXZ.sample(random)
            val pointY = config.outerWallDistanceY.sample(random) * 2
            val pointZ = config.outerWallDistanceXZ.sample(random)
            distPointPos = origin.offset(pointX, pointY, pointZ)
//                if (listDistPoints.size == distributionPointsAmount - 1) origin.add(-pointX, -pointY, -pointZ)
//                else origin.add(pointX, pointY, pointZ)

            this.setBlock(world, distPointPos, Blocks.DEEPSLATE.defaultBlockState())

            listDistPoints.add(
                Pair.of(
                    distPointPos,
                    config.pointOffset.sample(random)
                )
            )
        }

        this.setBlock(world, origin, Blocks.GLOWSTONE.defaultBlockState())
        listDistPoints.add(
            Pair.of(
                origin,
                0
            )
        )

        val var48: Iterator<*> = BlockPos.betweenClosed(
            origin.offset(minOffset, minOffset, minOffset),
            origin.offset(maxOffset, maxOffset, maxOffset)
        ).iterator()

        while (true) {
            while (true) {
                var radiusOfSection: Double
                var blockPos3: BlockPos
                do {
                    blockPos3 = var48.next() as BlockPos
                    radiusOfSection = 0.0

                    val noiser = dps1.getValue(
                        blockPos3.x.toDouble(),
                        blockPos3.y / 3.0,
                        blockPos3.z.toDouble()
                    ) * config.noiseMultiplier
                    val noiser2 = Math.min(
                        dps2.getValue(
                            blockPos3.x.toDouble(),
                            blockPos3.y.toDouble(),
                            blockPos3.z.toDouble()
                        ) * 0.1,
                        0.0
                    )

                    val distPoints: Iterator<Pair<BlockPos, Int>> = listDistPoints.iterator()
                    var pair: Pair<BlockPos, Int>
                    while (distPoints.hasNext()) {
                        pair = distPoints.next()
                        radiusOfSection += Mth.invSqrt(blockPos3.distSqr(pair.first as Vec3i) + (pair.second as Int).toDouble()) + noiser + noiser2
                    }
                } while (radiusOfSection < radius)


                if (radiusOfSection >= radius) {
                    this.safeSetBlock(
                        world,
                        blockPos3,
                        Blocks.STONE.defaultBlockState(),//                        config.blockstate.getBlockState(random, blockPos3),
                        inBlockTagPredicate(config.replaceable)
                    )
                }
            }
        }
    }
}