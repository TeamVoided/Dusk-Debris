package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.MushroomFeatureConfig

open class AbstractHugeMushroomFeature<T: MushroomFeatureConfig>(codec: Codec<T>) :
    Feature<T>(codec) {

    open fun generateCap(
        world: LevelAccessor,
        random: RandomSource,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.MutableBlockPos,
        config: T
    ) {
        mutable.setWithOffset(start, 0, yStart, 0)
        if (world.getBlockState(mutable).`is`(config.replaceable)) {
            this.setBlock(world, mutable, config.capBlock.getState(random, start))
        }
    }

    private fun generateStem(
        world: LevelAccessor,
        random: RandomSource,
        pos: BlockPos,
        config: T,
        height: Int,
        mutableBlockPos: BlockPos.MutableBlockPos
    ) {
        for (i in 0 until height) {
            mutableBlockPos.set(pos).move(Direction.UP, i)
            if (world.getBlockState(mutableBlockPos).`is`(config.replaceable)) {
                this.setBlock(world, mutableBlockPos, config.stemBlock.getState(random, pos))
            }
        }
    }

    private fun canGenerate(
        world: LevelAccessor,
        random: RandomSource,
        pos: BlockPos,
        height: Int,
        mutableBlockPos: BlockPos.MutableBlockPos,
        config: T
    ): Boolean {
        val y = pos.y
        if (y >= world.minBuildHeight + 1 && y + height + 1 < world.maxBuildHeight) {
            for (j in 0..height) {
                val blockState2 = world.getBlockState(mutableBlockPos.setWithOffset(pos, 0, j, 0))
                if (!blockState2.`is`(config.ignores)) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    override fun place(context: FeaturePlaceContext<T>): Boolean {
        val structureWorldAccess = context.level()
        val blockPos = context.origin()
        val randomGenerator = context.random()
        val hugeNethershroomFeatureConfig = context.config()
        val i = hugeNethershroomFeatureConfig.stemSize.sample(randomGenerator)
        val mutable = BlockPos.MutableBlockPos()
        if (!this.canGenerate(
                structureWorldAccess,
                randomGenerator,
                blockPos,
                i,
                mutable,
                hugeNethershroomFeatureConfig
            )
        ) {
            return false
        } else {
            this.generateCap(structureWorldAccess, randomGenerator, blockPos, i, mutable, hugeNethershroomFeatureConfig)
            this.generateStem(
                structureWorldAccess,
                randomGenerator,
                blockPos,
                hugeNethershroomFeatureConfig,
                i,
                mutable
            )
            return true
        }
    }
}