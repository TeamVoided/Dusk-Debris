package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import org.teamvoided.dusk_debris.init.DuskBlocks
import kotlin.math.sqrt

class SequoiaTreeFeature(codec: Codec<NoneFeatureConfiguration>) :
    Feature<NoneFeatureConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
        val origin = context.origin()
        val random = context.random()
        val world = context.level()
        val config = context.config()

        val width = 9//random.nextInt(9) + 1
        val height = width * 20 //+ (random.nextInt(width * 10) - width * 5)
        if (origin.y + height > world.maxBuildHeight) return false
        val logPositions: MutableList<BlockPos> = mutableListOf()
        val mutable = BlockPos.MutableBlockPos()

        val isEven = width % 2 == 0
        val funWidth1 = width / 2 + 1
        val funWidth2 = -width / 2 + if (isEven) 1 else 0
        for (y in 0 until height) {
            for (x in funWidth2 until funWidth1) {
                for (z in funWidth2 until funWidth1) {
                    val pos = origin.above(y).east(x).south(z)
                    mutable.set(pos)
                    if (width < 3 || (x == 0 && z == 0)) {
                        if (!(width == 2 && y == height - 1 && (x != 0 || z != 0)))
                            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return false
                    } else {
                        val dx = (height * (x - if (isEven) 0.5 else 0.0)) / funWidth1
                        val dz = (height * (z - if (isEven) 0.5 else 0.0)) / funWidth1
                        if (-sqrt(dx * dx + dz * dz) + height >= y) {
                            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return false
                        }
                    }
                }
            }
        }
        setTrunkBlocks(config, world, random, logPositions)
        return true
    }


    fun setTrunkBlocks(
        config: NoneFeatureConfiguration,
        world: WorldGenLevel,
        random: RandomSource,
        positions: MutableList<BlockPos>
    ) {
        val logBlock = SimpleStateProvider.simple(DuskBlocks.SEQUOIA_LOG.defaultBlockState())
        positions.forEach {
            this.setBlock(world, it, logBlock.getState(random, it))
        }
    }

    fun old(origin: BlockPos, width: Int, height: Int, world: WorldGenLevel): MutableList<BlockPos>? {
        val logPositions: MutableList<BlockPos> = mutableListOf()
        val mutable = BlockPos.MutableBlockPos()

        val isEven = width % 2 == 0
        val funWidth1 = width / 2 + 1
        val funWidth2 = -width / 2 + if (isEven) 1 else 0
        for (y in 0 until height) {
            for (x in funWidth2 until funWidth1) {
                for (z in funWidth2 until funWidth1) {
                    val pos = origin.above(y).east(x).south(z)
                    mutable.set(pos)
                    if (width < 3 || (x == 0 && z == 0)) {
                        if (!(width == 2 && y == height - 1 && (x != 0 || z != 0)))
                            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return null
                    } else {
                        val dx = (height * (x - if (isEven) 0.5 else 0.0)) / funWidth1
                        val dz = (height * (z - if (isEven) 0.5 else 0.0)) / funWidth1
                        if (-sqrt(dx * dx + dz * dz) + height >= y) {
                            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return null
                        }
                    }
                }
            }
        }
        return logPositions
    }
}