package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.MushroomBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

open class HugeBlueNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
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
        val offsetXZ = config.capXZInletOffset[random]
        val offsetY = config.capYInletOffset[random]
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
                        mutable[start, x, y + yStart] = z
                        if (world.getBlockState(mutable).isIn(config.replaceable)) {
                            var blockState = config.capBlock.getBlockState(random, start)
                            if (blockState.contains(MushroomBlock.WEST) &&
                                blockState.contains(MushroomBlock.EAST) &&
                                blockState.contains(MushroomBlock.NORTH) &&
                                blockState.contains(MushroomBlock.SOUTH) &&
                                blockState.contains(MushroomBlock.UP)
                            ) {
                                blockState = blockState
                                    .with(MushroomBlock.WEST, edgeNegX)
                                    .with(MushroomBlock.EAST, edgePosX)
                                    .with(MushroomBlock.NORTH, edgeNegZ)
                                    .with(MushroomBlock.SOUTH, edgePosZ)
                                    .with(MushroomBlock.UP, edgePosY)
                            }
                            this.setBlockState(world, mutable, blockState)
                        }
                    }
                }
            }
        }
    }
}