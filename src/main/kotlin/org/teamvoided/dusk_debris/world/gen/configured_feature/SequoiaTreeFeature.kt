package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.sample
import kotlin.math.abs
import kotlin.math.min

typealias ShapePredicate = (dx: Int, dz: Int) -> Boolean

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
        setLeavesBlocks(config, world, random, origin, height, width)
//                this.setBlockState(world, mutable, logBlock.getBlockState(random, pos))
        return true
    }

    fun setLeavesBlocks(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        random: RandomGenerator,
        origin: BlockPos,
        trunkHeight: Int,
        trunkWidth: Int
    ) {
        val blockPos: BlockPos = origin
        val foliageRadius = (trunkWidth * (1.25 + random.nextDouble() * 0.5)) + 4
        val foliageHeight = trunkHeight
        val isEven = trunkWidth % 2 == 0


        val chunkRandom = ChunkRandom(LegacySimpleRandom(world.seed))
        val dps = DoublePerlinNoiseSampler.create(chunkRandom, -2, *doubleArrayOf(1.0, 2.0))

        for (j in blockPos.y - foliageHeight..blockPos.y) {
            val k: Int = (blockPos.y - j)

            val heightBasedRadius = (1 + foliageRadius *
                    if (k > foliageHeight * 0.66666)
                        ((foliageHeight - k) * (1.0 / foliageHeight) * 3)
                    else
                        ((foliageHeight - k) * (1.0 / foliageHeight) * -1.5 + 1.5)
                    ).toInt()
            var foliagePos = BlockPos(blockPos.x, j + trunkHeight, blockPos.z)
            if (!isEven) foliagePos = foliagePos.add(1, 0, 1)
            predicate1(
                world,
                random,
                foliagePos,
                isEven,
                trunkWidth,
                heightBasedRadius,
                foliageHeight,
                dps
            )
        }
    }


    fun predicate2(
        world: StructureWorldAccess,
        random: RandomGenerator,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        dps: DoublePerlinNoiseSampler,
        predicate: ShapePredicate
    ) {
        val i = if (isEven) 1 else 0
        val mutable = BlockPos.Mutable()
        val leafBlock = SimpleBlockStateProvider.of(Blocks.GREEN_CONCRETE.defaultState)
        val leafBlock2 = SimpleBlockStateProvider.of(Blocks.RED_STAINED_GLASS.defaultState)
        val radiu = Math.min(radius, 16)
        for (x in -radiu..radiu + i) {
            for (z in -radiu..radiu + i) {
                if (predicate(x, z)) {
                    mutable[centerPos, x, y] = z
                    if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) {
                        val tre = (x.toDouble() * x + z * z)
                        if (2 * tre >= radiu * radiu) {
                            val noiser = dps.sample(mutable)
                            if (noiser > abs(tre - 2 * tre)) {
                                this.setBlockState(
                                    world, mutable,
                                    leafBlock.getBlockState(random, mutable)
                                )
                            }
                            else this.setBlockState(
                                world, mutable,
                                leafBlock2.getBlockState(random, mutable)
                            )
                        } else
                            this.setBlockState(
                                world, mutable,
                                leafBlock.getBlockState(random, mutable)
                            )
                    }
                }
            }
        }
    }

    fun predicate1(
        world: StructureWorldAccess,
        random: RandomGenerator,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        height: Int,
        dps: DoublePerlinNoiseSampler
    ) {
        return predicate2(world, random, centerPos, isEven, y, radius, dps)
        { x, z ->
            val dx = if (isEven) min(abs(x), abs(x - 1)) else abs(x)
            val dz = if (isEven) min(abs(z), abs(z - 1)) else abs(z)
//            val d = (height / radius) * (2 / 3f)
            (dx * dx + dz * dz) < radius * radius
//            !(if (dx + dz >= radius) true
//            else if (y > -height * 2 / 3f)
//                (d * 0.5 * sqrt((dx * dx + dz * dz).toDouble()) - height) > radius
//            else
//                -d * sqrt((dx * dx + dz * dz).toDouble()) > radius)
        }
    }


    fun setTrunkBlocks(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        random: RandomGenerator,
        positions: MutableList<BlockPos>
    ) {
        val logBlock = SimpleBlockStateProvider.of(Blocks.BLACKSTONE.defaultState)
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