package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import org.teamvoided.dusk_debris.world.gen.tree.foliage.OakFoliagePlacer

data class TreeFeatureConfigHelper(
    var logBlock: Block = Blocks.OAK_LOG,
    var leafBlock: Block = Blocks.OAK_LEAVES,
    var trunkPlacer: TrunkPlacer = StraightTrunkPlacer(4, 2, 0),
    var foliagePlacer: FoliagePlacer = OakFoliagePlacer(2, 0),
    var minimumSize: TwoLayersFeatureSize = TwoLayersFeatureSize(1, 0, 1),
    var decorators: List<TreeDecorator> = listOf(),
    var ignoreVines:Boolean = false
) {

    fun build(): TreeConfiguration {
        val config = TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.OAK_LOG),
            StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.simple(Blocks.OAK_LEAVES),
            OakFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
            TwoLayersFeatureSize(1, 0, 1)
        ).decorators(decorators)
        if (ignoreVines) config.ignoreVines()
        return config.build()
    }
}