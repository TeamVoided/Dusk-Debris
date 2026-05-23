package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import com.google.common.collect.ImmutableList
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.registerConfiguredFeature
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.world.gen.tree.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.CypressRootPlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.config.CypressRootConfig
import java.util.*

object SwampCFCreators {
    fun BootstrapContext<ConfiguredFeature<*, *>>.swampConfiguredFeatureCreators() {
        val configuredFeatures = this.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = this.lookup(Registries.PLACED_FEATURE)
        val block = this.lookup(Registries.BLOCK)

        this.registerConfiguredFeature(
            DuskConfiguredFeatures.DISK_MUD, Feature.DISK, DiskConfiguration(
                RuleBasedBlockStateProvider.simple(Blocks.CLAY), BlockPredicate.matchesBlocks(listOf(Blocks.DIRT, Blocks.MUD)),
                UniformInt.of(2, 6),
                2
            )
        )

        val cypressLog = BlockStateProvider.simple(DuskBlocks.CYPRESS_LOG)
        val cypressRoots = CypressRootPlacer(
            UniformInt.of(1, 3),
            BlockStateProvider.simple(Blocks.MANGROVE_ROOTS),
            Optional.of<AboveRootPlacement>(
                AboveRootPlacement(
                    BlockStateProvider.simple(Blocks.MOSS_CARPET),
                    0.5f
                )
            ),
            CypressRootConfig(
                block.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                HolderSet.direct(
                    { obj: Block -> obj.builtInRegistryHolder() },
                    *arrayOf<Block>(Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS)
                ),
                BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS),
                8,
                15,
                0.2f
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            Feature.TREE,
            TreeConfiguration.TreeConfigurationBuilder(
                cypressLog,
                StraightTrunkPlacer(5, 3, 3),
                BlockStateProvider.simple(DuskBlocks.CYPRESS_LEAVES),
                CypressFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                Optional.of(
                    cypressRoots
                ),
                TwoLayersFeatureSize(1, 0, 1)
            ).dirt(cypressLog).ignoreVines().decorators(
                ImmutableList.of<TreeDecorator>(LeaveVineDecorator(0.25f))
            ).build()
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TALL_SWAMP_CYPRESS,
            Feature.TREE,
            TreeConfiguration.TreeConfigurationBuilder(
                cypressLog,
                StraightTrunkPlacer(7, 5, 4),
                BlockStateProvider.simple(DuskBlocks.CYPRESS_LEAVES),
                CypressFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                Optional.of(
                    cypressRoots
                ),
                TwoLayersFeatureSize(1, 0, 1)
            ).dirt(cypressLog).ignoreVines().decorators(
                ImmutableList.of<TreeDecorator>(LeaveVineDecorator(0.25f))
            ).build()
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TREES_SWAMP, Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                listOf(
                    WeightedPlacedFeature(
                        placedFeatures.getOrThrow(DuskPlacedFeatures.TALL_CYPRESS),
                        0.85f
                    )
                ), placedFeatures.getOrThrow(DuskPlacedFeatures.CYPRESS)
            )
        )
    }
}