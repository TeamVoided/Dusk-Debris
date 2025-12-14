package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.MushroomFeatureConfig

open class HugeGoldMushroomFeature(codec: Codec<MushroomFeatureConfig>) :
    AbstractHugeMushroomFeature<MushroomFeatureConfig>(codec) {

    override fun generateCap(
        world: WorldAccess,
        random: RandomGenerator,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.Mutable,
        config: MushroomFeatureConfig
    ) {
        val height = config.capHeight[random]
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
                    mutable.set(start, x, y + yStart, z)
                    if (world.getBlockState(mutable).isIn(config.replaceable)) {
                        if (!isBig || !edgePosY) {
                            val cornerAbove = edgePosY || (isBig && (edgeX && edgeZ) && y == height - 1)

                            val blockState: BlockState = config.capBlock.getBlockState(random, start)
                                .withIfExists(Properties.WEST, edgeNegX)
                                .withIfExists(Properties.EAST, edgePosX)
                                .withIfExists(Properties.NORTH, edgeNegZ)
                                .withIfExists(Properties.SOUTH, edgePosZ)
                                .withIfExists(Properties.UP, cornerAbove)
                                .withIfExists(Properties.DOWN, false)
                            this.setBlockState(world, mutable, blockState)
                        } else if (!(edgeX && edgeZ)) {
                            val corner1 = edgeNegX || edgeZ && x == 1 - radius
                            val corner2 = edgePosX || edgeZ && x == radius - 1
                            val corner3 = edgeNegZ || edgeX && z == 1 - radius
                            val corner4 = edgePosZ || edgeX && z == radius - 1

                            val blockState: BlockState = config.capBlock.getBlockState(random, start)
                                .withIfExists(Properties.WEST, corner1)
                                .withIfExists(Properties.EAST, corner2)
                                .withIfExists(Properties.NORTH, corner3)
                                .withIfExists(Properties.SOUTH, corner4)
                                .withIfExists(Properties.UP, true)
                                .withIfExists(Properties.DOWN, false)
                            this.setBlockState(world, mutable, blockState)
                        }
                    }
                }
            }
        }
    }
}