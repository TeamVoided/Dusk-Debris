package org.teamvoided.dusk_debris.world.gen.tree.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacerType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class BirchFoliagePlacer(radius: IntProvider, offset: IntProvider) : FoliageHelper(radius, offset) {
    constructor(radius: Int, offset: Int) : this(ConstantIntProvider.create(radius), ConstantIntProvider.create(offset))

    override fun getType(): FoliagePlacerType<BirchFoliagePlacer> = DuskTreeStuff.BIRCH_FOLIAGE_PLACER

    override fun createFoliage(
        world: TestableWorld,
        placer: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        trunkHeight: Int,
        node: TreeNode,
        foliageHeight: Int,
        radius: Int,
        offset: Int
    ) {
        val blockPos = node.center.up(offset)
        val isBig = node.isGiantTrunk
        this.genSquareRandomNoCorners(world, placer, random, config, blockPos, isBig, -4, 1)
        this.birch1(world, placer, random, config, blockPos, isBig, -3, 2)
        this.birch2(world, placer, random, config, blockPos, isBig, -2, 2, 1, 2)
        this.birch2(world, placer, random, config, blockPos, isBig, -1, 2, 2)
        this.birch3(world, placer, random, config, blockPos, isBig, 0, 2)
        this.genSquareNoCorners(world, placer, random, config, blockPos, isBig, 1, 1)
        if (radius > 1)
            this.genSquare(world, placer, random, config, blockPos, isBig, 2, 0)
    }

    //random inner diamond
    private fun birch1(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        chanceLeafs: Int = 1
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz ->
        val xz = dx + dz
        val rad = radius * 2 - 1
        if (xz <= rad) {
            if (xz == rad - chanceLeafs)
                random.nextInt(2) == 0
            else true
        } else false
    }

    private fun birch2(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        rounding: Int = 1,
        randLeafsRound: Int = 1
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz ->
        val xz = dx + dz
        val rad = radius * 2 - rounding
        if (xz <= rad) {
            if (xz <= rad - randLeafsRound)
                random.nextInt(2) == 0
            else true
        } else false
    }

    private fun birch3(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz ->
        if (dx + dz <= radius * 2 - 2) {
            if (dx == dz && dx != 0)
                random.nextInt(2) == 0
            else true
        } else false
    }

    override fun getRandomHeight(random: RandomGenerator, trunkHeight: Int, config: TreeFeatureConfig): Int = 0

    companion object {
        val CODEC: MapCodec<BirchFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance ->
                fillFoliagePlacerFields(instance)
                    .apply(instance, ::BirchFoliagePlacer)
            }
    }
}