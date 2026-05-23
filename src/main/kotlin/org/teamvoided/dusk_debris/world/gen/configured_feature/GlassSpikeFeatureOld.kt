package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.synth.NormalNoise

class GlassSpikeFeatureOld(codec: Codec<NoneFeatureConfiguration>) :
    Feature<NoneFeatureConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
        val origin = context.origin()
        val randomGenerator = context.random()
        val world = context.level()
//        origin = origin.up(randomGenerator.nextInt(4))
        val yHeight = 20 / randomGenerator.nextInt(4) + 7
        val j = yHeight / 4 + randomGenerator.nextInt(4)
        var rangeXZ: Int
        var yLoop = 0

        val chunkRandom = WorldgenRandom(LegacyRandomSource(world.seed))
        val dps = NormalNoise.create(chunkRandom, -2, *doubleArrayOf(-1.0, 1.0))

        while (yLoop < yHeight) {
            val f = (1.0f - yLoop.toFloat() / yHeight.toFloat()) * j.toFloat()
            rangeXZ = Mth.ceil(f)

            for (xLoop in -rangeXZ..rangeXZ) {
                val x = Mth.abs(xLoop).toFloat() - 0.25f

                for (zLoop in -rangeXZ..rangeXZ) {
                    val z = Mth.abs(zLoop).toFloat() - 0.25f
                    if ((xLoop == 0 && zLoop == 0 || !(x * x + z * z > f * f))) {
                        var blockPos = origin.offset(xLoop, yLoop, zLoop)
                        if (noised(blockPos, dps) > 0 &&
                            world.getBlockState(blockPos).`is`(BlockTags.REPLACEABLE)
                        ) {
                            this.setBlock(
                                world,
                                blockPos,
                                Blocks.STONE.defaultBlockState()
                            )
                        }

                        if (yLoop != 0 && rangeXZ > 1) {
                            blockPos = origin.offset(xLoop, -yLoop, zLoop)
                            if (noised(blockPos, dps) > 0 &&
                                world.getBlockState(blockPos).`is`(BlockTags.REPLACEABLE)
                            ) {
                                this.setBlock(
                                    world,
                                    blockPos,
                                    Blocks.DEEPSLATE.defaultBlockState()
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

    fun noised(blockPos: BlockPos, dps: NormalNoise): Double {
        return dps.getValue(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble()
        )
    }
}