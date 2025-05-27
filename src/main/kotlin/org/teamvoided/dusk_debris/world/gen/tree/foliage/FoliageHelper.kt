package org.teamvoided.dusk_debris.world.gen.tree.foliage

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import kotlin.math.abs
import kotlin.math.min


// Diameter X & Z
typealias ShapePredicate = (dx: Int, dz: Int) -> Boolean

abstract class FoliageHelper(radius: IntProvider, offset: IntProvider) : FoliagePlacer(radius, offset) {
    constructor(radius: Int, offset: Int) : this(ConstantIntProvider.create(radius), ConstantIntProvider.create(offset))

    /*KEY
     * -> Always air
     % -> Possibly air
     # -> Always Leaves
    */


    //Create Diamond
    fun genSquareRounded(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        rounding: Double = 2.0
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz -> dx + dz <= radius * 2 - rounding }

    //Create Diamond, except the diamond edges (not the squares edges) are random
    fun genSquareRoundedRand(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        rounding: Double = 2.0,
        randRange: Double = 1.0,
        randChance: Int = 2,
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz ->
        val xz = dx + dz
        val rad = radius * 2 - rounding
        if (xz <= rad) {
            if (xz > rad - randRange)
                random.nextInt(randChance) == 0
            else true
        } else false
    }

    //Create Diamond, except the edge AND diamond edges (not the squares edges) are random
    fun genSquareRoundedRandEdge(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        rounding: Double = 2.0,
        randRange: Double = 1.0,
        randChance: Int = 2,
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz ->
        val xz = dx + dz
        val rad = radius * 2 - rounding
        if (xz <= rad) {
            if ((dx >= radius || dz >= radius) && xz > rad - randRange)
                random.nextInt(randChance) == 0
            else true
        } else false
    }

    fun genCircle(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz -> !(if (dx + dz >= 7) true else dx * dx + dz * dz > radius * radius) }

    fun genSquareNoCorners(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz -> !(dx == radius && dz == radius) }

    fun genSquareRandomNoCorners(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        cornerChance: Float = 0.5f
    ) = genShapeAbsInputs(world, place, random, config, centerPos, isEven, y, radius)
    { dx, dz -> !(dx == radius && dz == radius) || random.nextFloat() > cornerChance }

    fun genSquare(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
    ) = genShape(world, place, random, config, centerPos, isEven, y, radius) { _, _ -> true }

    //simple mather so it's an equilateral
    //  if (x is even) min(|x|, |x-1|) else |x|
    fun genShapeAbsInputs(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        predicate: ShapePredicate
    ) = genShape(world, place, random, config, centerPos, isEven, y, radius) { x, z ->
        val dx = if (isEven) min(abs(x), abs((x - 1))) else abs(x)
        val dz = if (isEven) min(abs(z), abs((z - 1))) else abs(z)
        predicate(dx, dz)
    }

    fun genShape(
        world: TestableWorld,
        place: Placer,
        random: RandomGenerator,
        config: TreeFeatureConfig,
        centerPos: BlockPos,
        isEven: Boolean,
        y: Int,
        radius: Int,
        predicate: ShapePredicate
    ) {
        val i = if (isEven) 1 else 0
        val mutable = BlockPos.Mutable()

        for (x in -radius..radius + i) {
            for (z in -radius..radius + i) {
                if (predicate(x, z)) {
                    mutable.set(centerPos, x, y, z)
                    placeFoliageBlock(world, place, random, config, mutable)
                }
            }
        }
    }

    override fun isInvalidForLeaves(
        random: RandomGenerator, dx: Int, y: Int, dz: Int, radius: Int, giantTrunk: Boolean
    ): Boolean = false
}