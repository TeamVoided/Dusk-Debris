package org.teamvoided.dusk_debris.world.gen.tree.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class BirchFoliagePlacer(radius: IntProvider, offset: IntProvider) : FoliageHelper(radius, offset) {
    constructor(radius: Int, offset: Int) : this(ConstantInt.of(radius), ConstantInt.of(offset))

    override fun type(): FoliagePlacerType<BirchFoliagePlacer> = DuskTreeStuff.BIRCH_FOLIAGE_PLACER

    override fun createFoliage(
        world: LevelSimulatedReader,
        placer: FoliageSetter,
        random: RandomSource,
        config: TreeConfiguration,
        trunkHeight: Int,
        node: FoliageAttachment,
        foliageHeight: Int,
        radius: Int,
        offset: Int
    ) {
        val blockPos = node.pos().above(offset)
        val isBig = node.doubleTrunk()
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
        world: LevelSimulatedReader,
        place: FoliageSetter,
        random: RandomSource,
        config: TreeConfiguration,
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
        world: LevelSimulatedReader,
        place: FoliageSetter,
        random: RandomSource,
        config: TreeConfiguration,
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
        world: LevelSimulatedReader,
        place: FoliageSetter,
        random: RandomSource,
        config: TreeConfiguration,
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

    override fun foliageHeight(random: RandomSource, trunkHeight: Int, config: TreeConfiguration): Int = 0

    companion object {
        val CODEC: MapCodec<BirchFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance ->
                foliagePlacerParts(instance)
                    .apply(instance, ::BirchFoliagePlacer)
            }
    }
}