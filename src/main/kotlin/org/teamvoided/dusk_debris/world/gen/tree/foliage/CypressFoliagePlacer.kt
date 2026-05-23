package org.teamvoided.dusk_debris.world.gen.tree.foliage

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

class CypressFoliagePlacer(intProvider: IntProvider, intProvider2: IntProvider) :
    FoliagePlacer(intProvider, intProvider2) {
    override fun type(): FoliagePlacerType<*> {
        return DuskTreeStuff.CYPRESS_FOLIAGE_PLACER
    }

    override fun createFoliage(
        world: LevelSimulatedReader,
        placer: FoliageSetter,
        random: RandomSource,
        treeFeatureConfig: TreeConfiguration,
        i: Int,
        treeNode: FoliageAttachment,
        j: Int,
        k: Int,
        l: Int
    ) {
        val bl = treeNode.doubleTrunk()
        val blockPos = treeNode.pos().above(l)
        this.placeLeavesRow(
            world,
            placer,
            random,
            treeFeatureConfig,
            blockPos,
            k + treeNode.radiusOffset(),
            -1 - j,
            bl
        )
        this.placeLeavesRow(world, placer, random, treeFeatureConfig, blockPos, k - 1, -j, bl)
        this.placeLeavesRow(
            world,
            placer,
            random,
            treeFeatureConfig,
            blockPos,
            k + treeNode.radiusOffset() - 1,
            0,
            bl
        )
    }

    override fun foliageHeight(random: RandomSource, trunkHeight: Int, config: TreeConfiguration): Int {
        return 0
    }

    override fun shouldSkipLocation(
        random: RandomSource,
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
                foliagePlacerParts(instance).apply(
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