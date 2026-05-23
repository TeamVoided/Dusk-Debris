package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.MushroomFeatureConfig

open class HugeGoldMushroomFeature(codec: Codec<MushroomFeatureConfig>) :
    AbstractHugeMushroomFeature<MushroomFeatureConfig>(codec) {

    override fun generateCap(
        world: LevelAccessor,
        random: RandomSource,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.MutableBlockPos,
        config: MushroomFeatureConfig
    ) {
        val height = config.capHeight.sample(random)
        val isBig = if (height < 4) random.nextBoolean() else false
        val radius = if (isBig) 2 else 1

        for (x in -radius..radius) {
            val edgePosX = x == radius
            val edgeNegX = x == -radius
            val edgeX = edgeNegX || edgePosX
            for (z in -radius..radius) {
                val edgePosZ = z == radius
                val edgeNegZ = z == -radius
                val edgeZ = edgeNegZ || edgePosZ
                for (y in 0..height) {
                    val edgePosY = y == height
                    mutable.setWithOffset(start, x, y + yStart, z)
                    if (world.getBlockState(mutable).`is`(config.replaceable)) {
                        if (!isBig || !edgePosY) {
                            val cornerAbove = edgePosY || (isBig && (edgeX && edgeZ) && y == height - 1)

                            val blockState: BlockState = config.capBlock.getState(random, start)
                                .trySetValue(BlockStateProperties.WEST, edgeNegX)
                                .trySetValue(BlockStateProperties.EAST, edgePosX)
                                .trySetValue(BlockStateProperties.NORTH, edgeNegZ)
                                .trySetValue(BlockStateProperties.SOUTH, edgePosZ)
                                .trySetValue(BlockStateProperties.UP, cornerAbove)
                                .trySetValue(BlockStateProperties.DOWN, false)
                            this.setBlock(world, mutable, blockState)
                        } else if (!(edgeX && edgeZ)) {
                            val corner1 = edgeNegX || edgeZ && x == 1 - radius
                            val corner2 = edgePosX || edgeZ && x == radius - 1
                            val corner3 = edgeNegZ || edgeX && z == 1 - radius
                            val corner4 = edgePosZ || edgeX && z == radius - 1

                            val blockState: BlockState = config.capBlock.getState(random, start)
                                .trySetValue(BlockStateProperties.WEST, corner1)
                                .trySetValue(BlockStateProperties.EAST, corner2)
                                .trySetValue(BlockStateProperties.NORTH, corner3)
                                .trySetValue(BlockStateProperties.SOUTH, corner4)
                                .trySetValue(BlockStateProperties.UP, true)
                                .trySetValue(BlockStateProperties.DOWN, false)
                            this.setBlock(world, mutable, blockState)
                        }
                    }
                }
            }
        }
    }
}