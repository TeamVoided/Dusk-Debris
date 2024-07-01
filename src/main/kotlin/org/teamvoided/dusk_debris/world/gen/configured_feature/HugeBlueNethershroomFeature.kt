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

open class HugeBlueNethershroomFeature(codec: Codec<HugeNethershroomFeatureConfig>) :
    Feature<HugeNethershroomFeatureConfig>(codec) {

    private fun generateCap(
        world: WorldAccess,
        random: RandomGenerator,
        start: BlockPos,
        yStart: Int,
        mutable: BlockPos.Mutable,
        config: HugeNethershroomFeatureConfig
    ) {
        val radius = config.capRadius[random]
        val height = config.capHeight[random]
        val inletOffset = config.capEdgeInletOffset[random]
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
                    val isBottomInlet: Boolean =
                        if (inletOffset == 0)
                            true
                        else if (inletOffset == 1)
                            !(edgeNegY && !(edgeX || edgeZ))
                        else
                            !(edgeNegY && !((x <= -radius + inletOffset || x >= radius + inletOffset) || (z <= -radius + inletOffset || z >= radius + inletOffset)))
                    if (isBottomInlet) {
                        mutable[start, x, y + yStart] = z
                        if (world.getBlockState(mutable).isIn(config.replaceable)) {
                            val westEdge = edgeNegX || edgeZ && x == 1 - radius
                            val eastEdge = edgePosX || edgeZ && x == radius - 1
                            val northEdge = edgeNegZ || edgeX && z == 1 - radius
                            val southEdge = edgePosZ || edgeX && z == radius - 1
                            val upEdge = edgePosY || edgeY && y == heightUpper
                            var blockState = config.capBlock.getBlockState(random, start)
                            if (blockState.contains(MushroomBlock.WEST) &&
                                blockState.contains(MushroomBlock.EAST) &&
                                blockState.contains(MushroomBlock.NORTH) &&
                                blockState.contains(MushroomBlock.SOUTH)
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
                val blockState2 = world.getBlockState(mutableBlockPos.set(pos, pos.x, j, pos.z))
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