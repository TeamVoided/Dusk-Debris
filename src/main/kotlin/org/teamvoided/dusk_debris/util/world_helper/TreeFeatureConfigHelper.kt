package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.trunk.StraightTrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacer
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

    fun build(): TreeFeatureConfig {
        val config = TreeFeatureConfig.Builder(
            BlockStateProvider.of(Blocks.OAK_LOG),
            StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.of(Blocks.OAK_LEAVES),
            OakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
            TwoLayersFeatureSize(1, 0, 1)
        ).decorators(decorators)
        if (ignoreVines) config.ignoreVines()
        return config.build()
    }
}