package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.HugeMushroomBlock
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

open class HugeBlueNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
    AbstractHugeMushroomFeature<HugeNethershroomFeatureConfig>(codec) {

    override fun generateCap(
        world: LevelAccessor,
        random: RandomSource,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.MutableBlockPos,
        config: HugeNethershroomFeatureConfig
    ) {
        val radius = config.capRadius.sample(random)
        val height = config.capHeight.sample(random)
        val offsetXZ = config.capXZInletOffset.sample(random)
        val offsetY = config.capYInletOffset.sample(random)
        val heightUpper = 0
        val heightLower = -height
        for (x in -radius..radius) {
            for (z in -radius..radius) {
                for (y in heightLower..heightUpper) {
                    val edgeNegX = x == -radius
                    val edgePosX = x == radius
                    val edgeNegZ = z == -radius
                    val edgePosZ = z == radius
                    val edgeNegY = y < heightLower + offsetY
                    val edgePosY = y == heightUpper
                    val edgeX = edgeNegX || edgePosX
                    val edgeZ = edgeNegZ || edgePosZ
                    val isNotBottomInlet: Boolean =
                        if (offsetXZ == 0)
                            !(edgeNegY && !edgePosY)
                        else if (offsetXZ == 1)
                            !(edgeNegY && !edgePosY && !(edgeX || edgeZ))
                        else if (offsetXZ >= (radius - 1))
                            !(edgeNegY && !edgePosY && (x == 0 && z == 0))
                        else
                            !(edgeNegY && !edgePosY && !((x <= -radius + offsetXZ - 1 || x >= radius - offsetXZ + 1) || (z <= -radius + offsetXZ - 1 || z >= radius - offsetXZ + 1)))
                    if (isNotBottomInlet) {
                        mutable.setWithOffset(start, x, y + yStart, z)
                        if (world.getBlockState(mutable).`is`(config.replaceable)) {
                            var blockState = config.capBlock.getState(random, start)
                            if (blockState.hasProperty(HugeMushroomBlock.WEST) &&
                                blockState.hasProperty(HugeMushroomBlock.EAST) &&
                                blockState.hasProperty(HugeMushroomBlock.NORTH) &&
                                blockState.hasProperty(HugeMushroomBlock.SOUTH) &&
                                blockState.hasProperty(HugeMushroomBlock.UP)
                            ) {
                                blockState = blockState
                                    .setValue(HugeMushroomBlock.WEST, edgeNegX)
                                    .setValue(HugeMushroomBlock.EAST, edgePosX)
                                    .setValue(HugeMushroomBlock.NORTH, edgeNegZ)
                                    .setValue(HugeMushroomBlock.SOUTH, edgePosZ)
                                    .setValue(HugeMushroomBlock.UP, edgePosY)
                            }
                            this.setBlock(world, mutable, blockState)
                        }
                    }
                }
            }
        }
    }
}