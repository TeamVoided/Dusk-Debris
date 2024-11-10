package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Lists
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.util.inBlockTagPredicate
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig
import java.util.function.Predicate

class GlassSpikeFeature(codec: Codec<GlassSpikeFeatureConfig>) :
    Feature<GlassSpikeFeatureConfig>(codec) {
    override fun place(context: FeatureContext<GlassSpikeFeatureConfig>): Boolean {
        val config = context.config as GlassSpikeFeatureConfig
        val random = context.random
        val origin = context.origin
        val world = context.world
        val minOffset = config.minGenOffset
        val maxOffset = config.maxGenOffset
        val listDistPoints: MutableList<Pair<BlockPos, Int>> = Lists.newLinkedList()
        val distributionPointsAmount = config.distributionPoints[random]
        val chunkRandom = ChunkRandom(LegacySimpleRandom(world.seed))
        val dps1 = DoublePerlinNoiseSampler.create(chunkRandom, -3, *doubleArrayOf(1.0))
        val dps2 = DoublePerlinNoiseSampler.create(chunkRandom, -3, *doubleArrayOf(1.0, 2.0))
        val radius = 1.0 / 2.0


        var distPointPos: BlockPos
        while (listDistPoints.size < distributionPointsAmount) {
            val pointX = config.outerWallDistanceXZ[random]
            val pointY = config.outerWallDistanceY[random] * 2
            val pointZ = config.outerWallDistanceXZ[random]
            distPointPos = origin.add(pointX, pointY, pointZ)
//                if (listDistPoints.size == distributionPointsAmount - 1) origin.add(-pointX, -pointY, -pointZ)
//                else origin.add(pointX, pointY, pointZ)

            this.setBlockState(world, distPointPos, Blocks.DEEPSLATE.defaultState)

            listDistPoints.add(
                Pair.of(
                    distPointPos,
                    config.pointOffset[random]
                )
            )
        }

        this.setBlockState(world, origin, Blocks.GLOWSTONE.defaultState)
        listDistPoints.add(
            Pair.of(
                origin,
                0
            )
        )

        val var48: Iterator<*> = BlockPos.iterate(
            origin.add(minOffset, minOffset, minOffset),
            origin.add(maxOffset, maxOffset, maxOffset)
        ).iterator()

        while (true) {
            while (true) {
                var radiusOfSection: Double
                var blockPos3: BlockPos
                do {
                    blockPos3 = var48.next() as BlockPos
                    radiusOfSection = 0.0

                    val noiser = dps1.sample(
                        blockPos3.x.toDouble(),
                        blockPos3.y / 3.0,
                        blockPos3.z.toDouble()
                    ) * config.noiseMultiplier
                    val noiser2 = Math.min(
                        dps2.sample(
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
                        radiusOfSection += MathHelper.inverseSqrt(blockPos3.getSquaredDistance(pair.first as Vec3i) + (pair.second as Int).toDouble()) + noiser + noiser2
                    }
                } while (radiusOfSection < radius)


                if (radiusOfSection >= radius) {
                    this.setBlockStateIf(
                        world,
                        blockPos3,
                        Blocks.STONE.defaultState,//                        config.blockstate.getBlockState(random, blockPos3),
                        inBlockTagPredicate(config.replaceable)
                    )
                }
            }
        }
    }
}