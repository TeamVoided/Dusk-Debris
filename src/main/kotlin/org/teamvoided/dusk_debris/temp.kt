//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.world.gen.feature

import com.mojang.serialization.Codec
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.feature.util.FeatureContext

abstract class HugeMushroomFeature(codec: Codec<HugeMushroomFeatureConfig?>?) :
    Feature<HugeMushroomFeatureConfig>(codec) {
    protected fun generateStem(
        world: WorldAccess,
        random: RandomGenerator?,
        pos: BlockPos?,
        config: HugeMushroomFeatureConfig,
        height: Int,
        mutableBlockPos: BlockPos.Mutable
    ) {
        for (i in 0 until height) {
            mutableBlockPos.set(pos).move(Direction.UP, i)
            if (!world.getBlockState(mutableBlockPos).isOpaqueFullCube(world, mutableBlockPos)) {
                this.setBlockState(world, mutableBlockPos, config.stemProvider.getBlockState(random, pos))
            }
        }
    }

    protected fun getHeight(random: RandomGenerator): Int {
        var i = random.nextInt(3) + 4
        if (random.nextInt(12) == 0) {
            i *= 2
        }

        return i
    }

    protected fun canGenerate(
        world: WorldAccess,
        pos: BlockPos,
        height: Int,
        mutableBlockPos: BlockPos.Mutable,
        config: HugeMushroomFeatureConfig
    ): Boolean {
        val i = pos.y
        if (i >= world.bottomY + 1 && i + height + 1 < world.topY) {
            val blockState = world.getBlockState(pos.down())
            if (!isSoil(blockState) && !blockState.isIn(BlockTags.MUSHROOM_GROW_BLOCK)) {
                return false
            } else {
                for (j in 0..height) {
                    val k = this.getCapSize(-1, -1, config.foliageRadius, j)

                    for (l in -k..k) {
                        for (m in -k..k) {
                            val blockState2 = world.getBlockState(mutableBlockPos.set(pos, l, j, m))
                            if (!blockState2.isAir && !blockState2.isIn(BlockTags.LEAVES)) {
                                return false
                            }
                        }
                    }
                }

                return true
            }
        } else {
            return false
        }
    }

    override fun place(context: FeatureContext<HugeMushroomFeatureConfig>): Boolean {
        val structureWorldAccess = context.world
        val blockPos = context.origin
        val randomGenerator = context.random
        val hugeMushroomFeatureConfig = context.config as HugeMushroomFeatureConfig
        val i = this.getHeight(randomGenerator)
        val mutable = BlockPos.Mutable()
        if (!this.canGenerate(structureWorldAccess, blockPos, i, mutable, hugeMushroomFeatureConfig)) {
            return false
        } else {
            this.generateCap(structureWorldAccess, randomGenerator, blockPos, i, mutable, hugeMushroomFeatureConfig)
            this.generateStem(structureWorldAccess, randomGenerator, blockPos, hugeMushroomFeatureConfig, i, mutable)
            return true
        }
    }

    protected abstract fun getCapSize(i: Int, j: Int, capSize: Int, y: Int): Int

    protected abstract fun generateCap(
        world: WorldAccess?,
        random: RandomGenerator?,
        start: BlockPos?,
        y: Int,
        mutable: BlockPos.Mutable?,
        config: HugeMushroomFeatureConfig?
    )
}