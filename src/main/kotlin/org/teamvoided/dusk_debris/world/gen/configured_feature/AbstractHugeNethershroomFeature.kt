package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.MushroomBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

open class AbstractHugeNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
    Feature<HugeNethershroomFeatureConfig>(codec) {

    open fun generateCap(
        world: WorldAccess,
        random: RandomGenerator,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.Mutable,
        config: HugeNethershroomFeatureConfig
    ) {
        mutable[start, 0, yStart] = 0
        if (world.getBlockState(mutable).isIn(config.replaceable)) {
            this.setBlockState(world, mutable, config.capBlock.getBlockState(random, start))
        }
    }

    private fun generateStem(
        world: WorldAccess,
        random: RandomGenerator,
        pos: BlockPos,
        config: HugeNethershroomFeatureConfig,
        height: Int,
        mutableBlockPos: BlockPos.Mutable
    ) {
        for (i in 0 until height) {
            mutableBlockPos.set(pos).move(Direction.UP, i)
            if (world.getBlockState(mutableBlockPos).isIn(config.replaceable)) {
                this.setBlockState(world, mutableBlockPos, config.stemBlock.getBlockState(random, pos))
            }
        }
    }

    private fun canGenerate(
        world: WorldAccess,
        random: RandomGenerator,
        pos: BlockPos,
        height: Int,
        mutableBlockPos: BlockPos.Mutable,
        config: HugeNethershroomFeatureConfig
    ): Boolean {
        val y = pos.y
        if (y >= world.bottomY + 1 && y + height + 1 < world.topY) {
            for (j in 0..height) {
                val blockState2 = world.getBlockState(mutableBlockPos.set(pos, 0, j, 0))
                if (!blockState2.isIn(config.ignores)) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    override fun place(context: FeatureContext<HugeNethershroomFeatureConfig>): Boolean {
        val structureWorldAccess = context.world
        val blockPos = context.origin
        val randomGenerator = context.random
        val hugeNethershroomFeatureConfig = context.config
        val i = hugeNethershroomFeatureConfig.stemSize[randomGenerator]
        val mutable = BlockPos.Mutable()
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