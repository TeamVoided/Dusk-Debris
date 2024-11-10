package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext

class GlassSpikeFeatureOld(codec: Codec<DefaultFeatureConfig>) :
    Feature<DefaultFeatureConfig>(codec) {
    override fun place(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val origin = context.origin
        val randomGenerator = context.random
        val world = context.world
//        origin = origin.up(randomGenerator.nextInt(4))
        val yHeight = 20 / randomGenerator.nextInt(4) + 7
        val j = yHeight / 4 + randomGenerator.nextInt(4)
        var rangeXZ: Int
        var yLoop = 0

        val chunkRandom = ChunkRandom(LegacySimpleRandom(world.seed))
        val dps = DoublePerlinNoiseSampler.create(chunkRandom, -2, *doubleArrayOf(-1.0, 1.0))

        while (yLoop < yHeight) {
            val f = (1.0f - yLoop.toFloat() / yHeight.toFloat()) * j.toFloat()
            rangeXZ = MathHelper.ceil(f)

            for (xLoop in -rangeXZ..rangeXZ) {
                val x = MathHelper.abs(xLoop).toFloat() - 0.25f

                for (zLoop in -rangeXZ..rangeXZ) {
                    val z = MathHelper.abs(zLoop).toFloat() - 0.25f
                    if ((xLoop == 0 && zLoop == 0 || !(x * x + z * z > f * f))) {
                        var blockPos = origin.add(xLoop, yLoop, zLoop)
                        if (noised(blockPos, dps) > 0 &&
                            world.getBlockState(blockPos).isIn(BlockTags.REPLACEABLE)
                        ) {
                            this.setBlockState(
                                world,
                                blockPos,
                                Blocks.STONE.defaultState
                            )
                        }

                        if (yLoop != 0 && rangeXZ > 1) {
                            blockPos = origin.add(xLoop, -yLoop, zLoop)
                            if (noised(blockPos, dps) > 0 &&
                                world.getBlockState(blockPos).isIn(BlockTags.REPLACEABLE)
                            ) {
                                this.setBlockState(
                                    world,
                                    blockPos,
                                    Blocks.DEEPSLATE.defaultState
                                )
                            }
                        }
                    }
                }
            }
            ++yLoop
        }

        return true
    }

    fun noised(blockPos: BlockPos, dps: DoublePerlinNoiseSampler): Double {
        return dps.sample(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble()
        )
    }
}