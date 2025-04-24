package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SequoiaTreeFeature(codec: Codec<DefaultFeatureConfig>) :
    Feature<DefaultFeatureConfig>(codec) {
    override fun place(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val origin = context.origin
        val random = context.random
        val world = context.world
        val config = context.config

        val width = 9//random.nextInt(9) + 1
        val height = width * 20 //+ (random.nextInt(width * 10) - width * 5)
        if (origin.y + height > world.topY) return false
        val logPositions: MutableList<BlockPos> = mutableListOf()
        val mutable = BlockPos.Mutable()

        val isEven = width % 2 == 0
        val funWidth1 = width / 2 + 1
        val funWidth2 = -width / 2 + if (isEven) 1 else 0
        for (y in 0 until height) {
            for (x in funWidth2 until funWidth1) {
                for (z in funWidth2 until funWidth1) {
                    val pos = origin.up(y).east(x).south(z)
                    mutable.set(pos)
                    if (width < 3 || (x == 0 && z == 0)) {
                        if (!(width == 2 && y == height - 1 && (x != 0 || z != 0)))
                            if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return false
                    } else {
                        val dx = (height * (x - if (isEven) 0.5 else 0.0)) / funWidth1
                        val dz = (height * (z - if (isEven) 0.5 else 0.0)) / funWidth1
                        if (-sqrt(dx * dx + dz * dz) + height >= y) {
                            if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return false
                        }
                    }
                }
            }
        }
        setTrunkBlocks(config, world, random, logPositions)
        return true
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

    fun old(origin: BlockPos, width: Int, height: Int, world: StructureWorldAccess): MutableList<BlockPos>? {
        val logPositions: MutableList<BlockPos> = mutableListOf()
        val mutable = BlockPos.Mutable()

        val isEven = width % 2 == 0
        val funWidth1 = width / 2 + 1
        val funWidth2 = -width / 2 + if (isEven) 1 else 0
        for (y in 0 until height) {
            for (x in funWidth2 until funWidth1) {
                for (z in funWidth2 until funWidth1) {
                    val pos = origin.up(y).east(x).south(z)
                    mutable.set(pos)
                    if (width < 3 || (x == 0 && z == 0)) {
                        if (!(width == 2 && y == height - 1 && (x != 0 || z != 0)))
                            if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return null
                    } else {
                        val dx = (height * (x - if (isEven) 0.5 else 0.0)) / funWidth1
                        val dz = (height * (z - if (isEven) 0.5 else 0.0)) / funWidth1
                        if (-sqrt(dx * dx + dz * dz) + height >= y) {
                            if (world.getBlockState(mutable).isIn(BlockTags.REPLACEABLE)) logPositions.add(pos)
                            else return null
                        }
                    }
                }
            }
        }
        return logPositions
    }
}