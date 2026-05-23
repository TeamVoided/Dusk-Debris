package org.teamvoided.dusk_debris.world.gen.tree.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class OakFoliagePlacer(radius: IntProvider, offset: IntProvider) : FoliageHelper(radius, offset) {
    constructor(radius: Int, offset: Int) : this(ConstantInt.of(radius), ConstantInt.of(offset))

    override fun type(): FoliagePlacerType<OakFoliagePlacer> = DuskTreeStuff.OAK_FOLIAGE_PLACER

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

    override fun foliageHeight(random: RandomSource, trunkHeight: Int, config: TreeConfiguration): Int = 0

    companion object {
        val CODEC: MapCodec<OakFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance ->
                foliagePlacerParts(instance)
                    .apply(instance, ::OakFoliagePlacer)
            }
    }
}