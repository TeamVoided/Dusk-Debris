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
        this.genSquareRandomNoCorners(world, placer, random, config, blockPos, isBig, -4, 2)


        this.genSquareRoundedRand(world, placer, random, config, blockPos, isBig, -4, 1, 1.0)
    }

    private fun innerRandom(
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

    override fun getRandomHeight(random: RandomGenerator, trunkHeight: Int, config: TreeFeatureConfig): Int = 0

    companion object {
        val CODEC: MapCodec<BirchFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance ->
                fillFoliagePlacerFields(instance)
                    .apply(instance, ::BirchFoliagePlacer)
            }
    }
}