package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.Utils.PI
import org.teamvoided.dusk_debris.util.Utils.rotate135
import org.teamvoided.dusk_debris.util.Utils.rotate315
import org.teamvoided.dusk_debris.util.Utils.rotate45
import org.teamvoided.dusk_debris.util.Utils.rotate90
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

typealias ShapePredicate = (dx: Int, dz: Int) -> Boolean

class SequoiaTreeFeatureOld(codec: Codec<NoneFeatureConfiguration>) : Feature<NoneFeatureConfiguration>(codec) {

    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
        val origin = context.origin()
        val random = context.random()
        val world = context.level()
        val config = context.config()

        val width = 6//random.nextInt(5) + 1
        val height = width * 10 + (random.nextInt(width * 4) - width * 2)

        if (origin.y + height > world.maxBuildHeight) return false

        val logPositions: MutableList<BlockPos> = mutableListOf()

        val mutable = BlockPos.MutableBlockPos()

        var cornerNW = true
        var cornerNE = true
        var cornerSW = true
        var cornerSE = true

        val logBlock = SimpleStateProvider.simple(DuskBlocks.SEQUOIA_LOG.defaultBlockState())

        val cone = 10 * width //10 * width * width

        val isEven = width % 2 == 0
        val funWidth1 = width / 2 + 1
        val funWidth2 = -width / 2 + if (isEven) 1 else 0
        println("lala")
        println(funWidth1)
        println(funWidth2)

        for (y in 0 until height) {
            for (x in funWidth2 until funWidth1) {
                for (z in funWidth2 until funWidth1) {
                    if (
                        width < 3 ||
                        isCornerAndActive(
                            x, z,
                            funWidth1 - 1,
                            funWidth2,
                            cornerNW, cornerNE, cornerSW, cornerSE
                        )
                    ) {
                        if (width > 2 && isCorner(x, z, funWidth1 - 1, funWidth2)) {
                            val the = y / 2.0 / height
                            if (random.nextDouble() < the * the) {
                                if (x == funWidth1 - 1) {
                                    if (z == funWidth1 - 1) {
                                        cornerNW = false
                                    } else if (z == funWidth2) {
                                        cornerSW = false
                                    }
                                } else if (x == funWidth2) {
                                    if (z == funWidth1 - 1) {
                                        cornerNE = false
                                    } else if (z == funWidth2) {
                                        cornerSE = false
                                    }
                                }
                            }
                        }
                        val pos = origin
                            .relative(Direction.UP, y)
                            .relative(Direction.EAST, x)
                            .relative(Direction.SOUTH, z)
                        mutable.set(pos)
                        if (width <= 2 || (x == 0 && z == 0)) {
                            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) {
                                logPositions.add(pos)
                            } else return false
                        } else {
                            val dx = x - if (isEven) 0.5 else 0.0
                            val dz = z - if (isEven) 0.5 else 0.0
                            val the = -(sqrt((cone * (dx * dx + dz * dz))) - height)
                            if (the >= y) {
                                if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) {
                                    logPositions.add(pos)
                                } else return false
                            }
                        }
                    }
                }
            }
        }
        if (width == 2) {
            mutable.set(origin.offset(0, height, 0))
            if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE)) {
                logPositions.add(mutable)
            }
        }

        setTrunkBlocks(config, world, random, logPositions)
        //setLeavesBlocks(config, world, random, origin, height, width)
        //branch(config, world, random, origin.add(-1, 0, -1), width, height)
        //this.setBlockState(world, mutable, logBlock.getBlockState(random, pos))
        return true
    }

    fun setLeavesBlocks(
        config: NoneFeatureConfiguration,
        world: WorldGenLevel,
        random: RandomSource,
        origin: BlockPos,
        trunkHeight: Int,
        trunkWidth: Int
    ) {
        val blockPos: BlockPos = origin
        val foliageRadius = (trunkWidth * (1.5 + random.nextDouble() * 0.5)) + 2
        val foliageHeight = trunkHeight
        val isEven = trunkWidth % 2 == 0


        val chunkRandom = WorldgenRandom(LegacyRandomSource(world.seed))
        val dps = NormalNoise.create(chunkRandom, -2, *doubleArrayOf(1.0, 2.0))

        for (j in blockPos.y - foliageHeight..blockPos.y) {
            val k: Int = (blockPos.y - j)

            val heightBasedRadius = (1 + foliageRadius *
                    if (k > foliageHeight * 0.66666)
                        ((foliageHeight - k) * (1.0 / foliageHeight) * 3)
                    else
                        ((foliageHeight - k) * (1.0 / foliageHeight) * -1.5 + 1.5)
                    ).toInt()
            var foliagePos = BlockPos(blockPos.x, j + trunkHeight, blockPos.z)
            if (!isEven) foliagePos = foliagePos.offset(1, 0, 1)
            coneLeaves(
                world,
                random,
                foliagePos,
                isEven,
                trunkWidth + 2,
                heightBasedRadius,
                dps
            )
        }
    }


    fun coneLeaves(
        world: WorldGenLevel,
        random: RandomSource,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        dps: NormalNoise
    ) {
        val i = if (isEven) 1 else 0
        val mutable = BlockPos.MutableBlockPos()
        val leafBlock = SimpleStateProvider.simple(Blocks.GREEN_STAINED_GLASS.defaultBlockState())
        val leafBlock2 = SimpleStateProvider.simple(Blocks.RED_STAINED_GLASS.defaultBlockState())
        val radiu = Math.min(radius, 16)
        for (x in -radiu..radiu + i) {
            for (z in -radiu..radiu + i) {
                val dx = if (isEven) min(abs(x), abs(x - 1)) else abs(x)
                val dz = if (isEven) min(abs(z), abs(z - 1)) else abs(z)
                val tre = (dx * dx + dz * dz)
                if (tre < radius * radius) {
                    mutable.setWithOffset(centerPos, x, y, z)
                    if (world.getBlockState(mutable).`is`(BlockTags.REPLACEABLE))
                        this.setBlock(
                            world, mutable,
                            leafBlock.getState(random, mutable)
                        )
                }
            }
        }
    }

    fun branch(
        config: NoneFeatureConfiguration,
        world: WorldGenLevel,
        random: RandomSource,
        origin: BlockPos,
        trunkWidth: Int,
        trunkHeight: Int
    ) {
        val logBlock = SimpleStateProvider.simple(DuskBlocks.SEQUOIA_LOG.defaultBlockState())
        val logBlock2 = SimpleStateProvider.simple(Blocks.RED_CONCRETE.defaultBlockState())
        val isEven = trunkWidth % 2 == 0
        var posY = trunkHeight
        val height3 = trunkHeight / 3
        while (posY > height3) {
            var rotation = random.nextFloat() * rotate90
//            val axis = ((rotation - rotate45) * (180 / pi)) / 90f

            var rotX = 0
            var rotZ = 0
            var posY2 = 0
            val angleY = 2 + random.nextInt(3)
            for (side in 0..3) {
                val axis = if ((rotation + rotate45) % PI > rotate90) {
                    Direction.Axis.Z
                } else {
                    Direction.Axis.X
                }
                val start = if (isEven || (rotation >= rotate135 && rotation < rotate315)) 2 else 1
                if (posY + trunkWidth < trunkHeight) {
                    for (offset in start..start + (trunkWidth / 2) + (0.3 * (trunkHeight - posY)).toInt()) {
                        rotX = (1.5f + Mth.cos(rotation) * offset).toInt()
                        rotZ = (1.5f + Mth.sin(rotation) * offset).toInt()

                        val blockPos = origin.offset(rotX, posY - (offset / angleY) + posY2, rotZ)
                        if (origin.x - blockPos.x > 15 ||
                            origin.z - blockPos.z > 15 ||
                            origin.x - blockPos.x < -15 ||
                            origin.z - blockPos.z < -15
                        ) {
                            this.setBlock(
                                world, blockPos,
                                logBlock2.getState(random, blockPos)
                            )
                        } else
                            this.setBlock(
                                world, blockPos,
                                logBlock.getState(random, blockPos)
                                    .trySetValue(BlockStateProperties.AXIS, axis)
                            )
//                this.placeTrunkBlock(world, replacer, random, blockPos, config)
                    }
                }
                rotation += rotate90
                posY2 = random.nextInt(4) - 2
            }
            posY -= 1 + random.nextInt(2)
        }
//        println(" ")
    }


    fun setTrunkBlocks(
        config: NoneFeatureConfiguration,
        world: WorldGenLevel,
        random: RandomSource,
        positions: MutableList<BlockPos>
    ) {
        val logBlock = SimpleStateProvider.simple(DuskBlocks.SEQUOIA_LOG.defaultBlockState())
        positions.forEach {
            this.setBlock(world, it, logBlock.getState(random, it))
        }
    }

    private fun isTopEdge(x: Int, y: Int, z: Int, width: Int, height: Int): Boolean {
        return (y == height && (x == 0 || x == width || z == 0 || z == width))
    }

    private fun isCornerAndActive(
        x: Int,
        z: Int,
        min: Int,
        max: Int,
        cornerNW: Boolean,
        cornerNE: Boolean,
        cornerSW: Boolean,
        cornerSE: Boolean
    ): Boolean {
        if (x == min) {
            if (z == min) {
                return cornerNW
            } else if (z == max) {
                return cornerSW
            }
        } else if (x == max) {
            if (z == min) {
                return cornerNE
            } else if (z == max) {
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
        min: Int,
        max: Int,
    ): Boolean {
        return (x == min || x == max) && (z == min || z == max)
    }
}