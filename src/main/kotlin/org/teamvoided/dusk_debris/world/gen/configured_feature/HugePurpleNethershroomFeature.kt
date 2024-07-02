package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.MushroomBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

open class HugePurpleNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
    AbstractHugeNethershroomFeature(codec) {

    override fun generateCap(
        world: WorldAccess,
        random: RandomGenerator,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.Mutable,
        config: HugeNethershroomFeatureConfig
    ) {
        val radius = config.capRadius[random]
        val height = config.capHeight[random]
//        var offsetXZ = 0
        val codecOffsetXZ = config.capXZInletOffset[random]
        var offsetXZ = if (1 >= (radius - codecOffsetXZ)) {
            radius -1
        } else {
            codecOffsetXZ
        }
        val offsetY = config.capYInletOffset[random]
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
                    mutable[start, x, y + yStart + yOffset] = z
                    if (world.getBlockState(mutable).isIn(config.replaceable)) {
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
                        var blockState = config.capBlock.getBlockState(random, start)
                        if (blockState.contains(MushroomBlock.WEST) &&
                            blockState.contains(MushroomBlock.EAST) &&
                            blockState.contains(MushroomBlock.NORTH) &&
                            blockState.contains(MushroomBlock.SOUTH) &&
                            blockState.contains(MushroomBlock.UP)
                        ) {
                            blockState = blockState
                                .with(MushroomBlock.WEST, westEdge)
                                .with(MushroomBlock.EAST, eastEdge)
                                .with(MushroomBlock.NORTH, northEdge)
                                .with(MushroomBlock.SOUTH, southEdge)
                                .with(MushroomBlock.UP, upEdge)
                        }
                        this.setBlockState(world, mutable, blockState)
                    }
                }
            }
        }
    }
}