package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.HugeMushroomBlock
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

open class HugePurpleNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
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
//        var offsetXZ = 0
        val codecOffsetXZ = config.capXZInletOffset.sample(random)
        var offsetXZ = if (1 >= (radius - codecOffsetXZ)) {
            radius -1
        } else {
            codecOffsetXZ
        }
        val offsetY = config.capYInletOffset.sample(random)
        val heightUpper = height - 2
        val heightLower = -1
        for (x in -radius..radius) {
            for (z in -radius..radius) {
                for (y in heightLower..heightUpper) {
                    val edgeNegX = x == -radius
                    val edgePosX = x == radius
                    val edgeNegZ = z == -radius
                    val edgePosZ = z == radius
                    val edgeNegY = y == heightLower
                    val edgePosY = y == heightUpper
                    val edgeX = edgeNegX || edgePosX
                    val edgeZ = edgeNegZ || edgePosZ
                    val edgeY = edgeNegY || edgePosY
                    val isNotInlet: Boolean =
                        (x <= -radius + offsetXZ - 1 || x >= radius - offsetXZ + 1) || (z <= -radius + offsetXZ - 1 || z >= radius - offsetXZ + 1)

                    val yOffset = if (isNotInlet)
                        if (offsetY >= height) height - 1
                        else offsetY
                    else 0
                    mutable.setWithOffset(start, x, y + yStart + yOffset, z)
                    if (world.getBlockState(mutable).`is`(config.replaceable)) {
                        val lowerY = (y < heightLower + offsetY)
                        val upperY = (y > heightLower)
                        val edgeZRange = (z > -radius + offsetXZ - 1 && z < radius - offsetXZ + 1)
                        val edgeXRange = (x > -radius + offsetXZ - 1 && x < radius - offsetXZ + 1)
                        val westEdge = edgeNegX ||
                                (lowerY && x == offsetXZ - radius && !isNotInlet) ||
                                (upperY && x == radius - offsetXZ + 1 && edgeZRange)
                        val eastEdge = edgePosX ||
                                (lowerY && x == radius - offsetXZ && !isNotInlet) ||
                                (upperY && x == offsetXZ - radius - 1 && edgeZRange)
                        val northEdge = edgeNegZ ||
                                (lowerY && z == offsetXZ - radius && !isNotInlet) ||
                                (upperY && z == radius - offsetXZ + 1 && edgeXRange)
                        val southEdge = edgePosZ ||
                                (lowerY && z == radius - offsetXZ && !isNotInlet) ||
                                (upperY && z == offsetXZ - radius - 1 && edgeXRange)
                        val upEdge = edgePosY || edgeY && y == heightUpper
                        var blockState = config.capBlock.getState(random, start)
                        if (blockState.hasProperty(HugeMushroomBlock.WEST) &&
                            blockState.hasProperty(HugeMushroomBlock.EAST) &&
                            blockState.hasProperty(HugeMushroomBlock.NORTH) &&
                            blockState.hasProperty(HugeMushroomBlock.SOUTH) &&
                            blockState.hasProperty(HugeMushroomBlock.UP)
                        ) {
                            blockState = blockState
                                .setValue(HugeMushroomBlock.WEST, westEdge)
                                .setValue(HugeMushroomBlock.EAST, eastEdge)
                                .setValue(HugeMushroomBlock.NORTH, northEdge)
                                .setValue(HugeMushroomBlock.SOUTH, southEdge)
                                .setValue(HugeMushroomBlock.UP, upEdge)
                        }
                        this.setBlock(world, mutable, blockState)
                    }
                }
            }
        }
    }
}