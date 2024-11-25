package org.teamvoided.dusk_debris.world.gen.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.foliage.FoliagePlacerType
import org.teamvoided.dusk_debris.init.DuskWorldgen

class CypressFoliagePlacer(intProvider: IntProvider?, intProvider2: IntProvider?) :
    FoliagePlacer(intProvider, intProvider2) {
    override fun getType(): FoliagePlacerType<*> {
        return DuskWorldgen.CYPRESS_FOLIAGE_PLACER
    }

    override fun createFoliage(
        world: TestableWorld,
        placer: Placer,
        random: RandomGenerator,
        treeFeatureConfig: TreeFeatureConfig,
        i: Int,
        treeNode: TreeNode,
        j: Int,
        k: Int,
        l: Int
    ) {
        val bl = treeNode.isGiantTrunk
        val blockPos = treeNode.center.up(l)
        this.generateSquare(
            world,
            placer,
            random,
            treeFeatureConfig,
            blockPos,
            k + treeNode.foliageRadius,
            -1 - j,
            bl
        )
        this.generateSquare(world, placer, random, treeFeatureConfig, blockPos, k - 1, -j, bl)
        this.generateSquare(
            world,
            placer,
            random,
            treeFeatureConfig,
            blockPos,
            k + treeNode.foliageRadius - 1,
            0,
            bl
        )
    }

    override fun getRandomHeight(random: RandomGenerator, trunkHeight: Int, config: TreeFeatureConfig): Int {
        return 0
    }

    override fun isInvalidForLeaves(
        random: RandomGenerator,
        dx: Int,
        y: Int,
        dz: Int,
        radius: Int,
        giantTrunk: Boolean
    ): Boolean {
        return if (y == 0) {
            (dx > 1 || dz > 1) && dx != 0 && dz != 0
        } else {
            dx == radius && dz == radius && radius > 0
        }
    }

    companion object {
        val CODEC: MapCodec<CypressFoliagePlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<CypressFoliagePlacer> ->
                fillFoliagePlacerFields(instance).apply(
                    instance
                ) { intProvider: IntProvider, intProvider2: IntProvider ->
                    CypressFoliagePlacer(
                        intProvider,
                        intProvider2
                    )
                }
            }
    }
}