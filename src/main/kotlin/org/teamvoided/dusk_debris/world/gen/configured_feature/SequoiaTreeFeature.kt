package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.google.common.collect.Lists
import com.mojang.serialization.Codec
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.init.DuskBlocks
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import org.teamvoided.dusk_debris.util.Utils.pi
import org.teamvoided.dusk_debris.util.Utils.rotate135
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.util.Utils.rotate45
import org.teamvoided.dusk_debris.util.Utils.rotate90

class SequoiaTreeFeature(codec: Codec<DefaultFeatureConfig>) : Feature<DefaultFeatureConfig>(codec) {
    override fun place(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val origin = context.origin
        val random = context.random
        val world = context.world
        val config = context.config

        val width = random.nextInt(5) + 1
        val height = width * 10 + (random.nextInt(width * 4) - width * 2)

        if (origin.y + height > world.topY) return false

        val logPositions: MutableList<BlockPos> = mutableListOf()

        val mutable = BlockPos.Mutable()

        var cornerNW = true
        var cornerNE = true
        var cornerSW = true
        var cornerSE = true

        val logBlock = SimpleBlockStateProvider.of(DuskBlocks.SEQUOIA_LOG.defaultState)

        val offsetXZ = -(width / 2) + 1
        for (y in 0 until height) {
            for (x in 0 until width) {
                for (z in 0 until width) {
                    if (width <= 2 ||
                        (!isTopEdge(x, y, z, width - 1, height - 1) &&
                                isCornerAndActive(x, z, width - 1, cornerNW, cornerNE, cornerSW, cornerSE))
                    ) {
                        if (width > 2 && isCorner(x, z, width - 1)) {
                            val the = y.toDouble() / height
                            if (random.nextDouble() < the * the) {
                                if (x == 0) {
                                    if (z == 0) {
                                        cornerNW = false
                                    } else if (z == width - 1) {
                                        cornerSW = false
                                    }
                                } else if (x == width - 1) {
                                    if (z == 0) {
                                        cornerNE = false
                                    } else if (z == width - 1) {
                                        cornerSE = false
                                    }
                                }
                            }
                        }

                        val pos = origin
                            .offset(Direction.UP, y)
                            .offset(Direction.EAST, x + offsetXZ)
                            .offset(Direction.SOUTH, z + offsetXZ)
                        mutable.set(pos)
                        if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) {
                            logPositions.add(pos)
//                            this.setBlockState(world, mutable, logBlock.getBlockState(random, pos))
                        } else {
                            println(mutable)
                            println(world.getBlockState(mutable))
                            return false
                        }
                    }
                }
            }
        }

        setTrunkBlocks(config, world, random, logPositions)
        branch(config, world, random, origin, width, height)

//                this.setBlockState(world, mutable, logBlock.getBlockState(random, pos))
        return true
    }

    fun branch(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        random: RandomGenerator,
        origin: BlockPos,
        width: Int,
        height: Int
    ) {
        val logBlock = SimpleBlockStateProvider.of(DuskBlocks.SEQUOIA_LOG.defaultState)
        var posY = height - width - random.nextInt(width)
        while (posY > height / 3) {
            val rotation = random.nextFloat() * rotate360
//            val axis = ((rotation - rotate45) * (180 / pi)) / 90f
            val axis = if ((rotation + rotate45) % pi > rotate90) {
                Direction.Axis.Z
            } else {
                Direction.Axis.X
            }

            var rotX = 0
            var rotY = 0
            val angleY = random.nextInt(4)
            for (offset in 0..(0.4 * (height - posY)).toInt()) {
                rotX = (1.5f + MathHelper.cos(rotation) * offset).toInt()
                rotY = (1.5f + MathHelper.sin(rotation) * offset).toInt()
                val blockPos = origin.add(rotX, posY - offset / angleY, rotY)
//                this.placeTrunkBlock(world, replacer, random, blockPos, config)
                this.setBlockState(
                    world,
                    blockPos,
                    logBlock.getBlockState(random, blockPos).withIfExists(Properties.AXIS, axis)
                )
            }
            posY -= 1 + random.nextInt(3)
        }
    }

    fun setTrunkBlocks(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        random: RandomGenerator,
        positions: MutableList<BlockPos>
    ) {
        val logBlock = SimpleBlockStateProvider.of(DuskBlocks.SEQUOIA_LOG.defaultState)
        positions.forEach {
            this.setBlockState(world, it, logBlock.getBlockState(random, it))
        }
    }

    private fun isTopEdge(x: Int, y: Int, z: Int, width: Int, height: Int): Boolean {
        return (y == height && (x == 0 || x == width || z == 0 || z == width))
    }

    private fun isCornerAndActive(
        x: Int,
        z: Int,
        width: Int,
        cornerNW: Boolean,
        cornerNE: Boolean,
        cornerSW: Boolean,
        cornerSE: Boolean
    ): Boolean {
        if (x == 0) {
            if (z == 0) {
                return cornerNW
            } else if (z == width) {
                return cornerSW
            }
        } else if (x == width) {
            if (z == 0) {
                return cornerNE
            } else if (z == width) {
                return cornerSE
            }
        }
        return true
//        return (x == 0 && z == 0 && cornerNW) ||
//                (x == width && z == 0 && cornerNE) ||
//                (x == 0 && z == width && cornerSW) ||
//                (x == width && z == width && cornerSE)
    }

    private fun isCorner(
        x: Int,
        z: Int,
        width: Int
    ): Boolean {
        return (x == 0 || x == width) && (z == 0 || z == width)
    }
}