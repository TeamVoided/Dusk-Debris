package org.teamvoided.dusk_debris.world.gen.tree.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacerType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class OakFoliagePlacer(radius: IntProvider, offset: IntProvider) : FoliageHelper(radius, offset) {
    constructor(radius: Int, offset: Int) : this(ConstantIntProvider.create(radius), ConstantIntProvider.create(offset))

    override fun getType(): FoliagePlacerType<OakFoliagePlacer> = DuskTreeStuff.OAK_FOLIAGE_PLACER

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
        if (!isBig) {
            val twoThird = 0.3f

            this.genSquareRoundedRand(world, placer, random, config, blockPos, false, -4, radius - 2, twoThird, 1.0)
            this.genSquareRounded(world, placer, random, config, blockPos, false, -3, radius)
            this.genSquareRoundedRand(world, placer, random, config, blockPos, false, -2, radius, twoThird, 1.0)
            this.genSquareRoundedRand(world, placer, random, config, blockPos, false, -1, radius, twoThird)
            this.genSquareRoundedRandEdge(world, placer, random, config, blockPos, false, 0, radius, 3.0)
            this.genSquareRoundedRand(world, placer, random, config, blockPos, false, 1, radius - 1, twoThird, 1.0)
            this.genSquareRoundedRand(world, placer, random, config, blockPos, false, 2, radius - 2, twoThird, 1.0)
        } else {
            val twoThird = 0.3f
            this.genSquareRoundedRand(world, placer, random, config, blockPos, true, 0, 5, twoThird)
        }
    }

    override fun getRandomHeight(random: RandomGenerator, trunkHeight: Int, config: TreeFeatureConfig): Int = 0

    companion object {
        val CODEC: MapCodec<OakFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance ->
                fillFoliagePlacerFields(instance)
                    .apply(instance, ::OakFoliagePlacer)
            }
    }
}