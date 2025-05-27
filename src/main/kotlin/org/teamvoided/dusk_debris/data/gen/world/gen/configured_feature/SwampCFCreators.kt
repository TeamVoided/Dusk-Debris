package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import com.google.common.collect.ImmutableList
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.MushroomBlock
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.unmapped.C_cxbmzbuz
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.root.AboveRootPlacement
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.trunk.StraightTrunkPlacer
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.registerConfiguredFeature
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig
import org.teamvoided.dusk_debris.world.gen.tree.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.CypressRootPlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.config.CypressRootConfig
import java.util.*

object SwampCFCreators {
    fun BootstrapContext<ConfiguredFeature<*, *>>.swampConfiguredFeatureCreators() {
        val configuredFeatures = this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val block = this.getRegistryLookup(RegistryKeys.BLOCK)

        this.registerConfiguredFeature(
            DuskConfiguredFeatures.DISK_MUD, Feature.DISK, DiskFeatureConfig(
                C_cxbmzbuz.method_43312(Blocks.CLAY), BlockPredicate.matchingBlocks(listOf(Blocks.DIRT, Blocks.MUD)),
                UniformIntProvider.create(2, 6),
                2
            )
        )

        val cypressLog = BlockStateProvider.of(DuskBlocks.CYPRESS_LOG)
        val cypressRoots = CypressRootPlacer(
            UniformIntProvider.create(1, 3),
            BlockStateProvider.of(Blocks.MANGROVE_ROOTS),
            Optional.of<AboveRootPlacement>(
                AboveRootPlacement(
                    BlockStateProvider.of(Blocks.MOSS_CARPET),
                    0.5f
                )
            ),
            CypressRootConfig(
                block.getTagOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                HolderSet.createDirect(
                    { obj: Block -> obj.builtInRegistryHolder },
                    *arrayOf<Block>(Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS)
                ),
                BlockStateProvider.of(Blocks.MUDDY_MANGROVE_ROOTS),
                8,
                15,
                0.2f
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            Feature.TREE,
            TreeFeatureConfig.Builder(
                cypressLog,
                StraightTrunkPlacer(5, 3, 3),
                BlockStateProvider.of(DuskBlocks.CYPRESS_LEAVES),
                CypressFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0)),
                Optional.of(
                    cypressRoots
                ),
                TwoLayersFeatureSize(1, 0, 1)
            ).dirtProvider(cypressLog).ignoreVines().decorators(
                ImmutableList.of<TreeDecorator>(LeavesVineTreeDecorator(0.25f))
            ).build()
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TALL_SWAMP_CYPRESS,
            Feature.TREE,
            TreeFeatureConfig.Builder(
                cypressLog,
                StraightTrunkPlacer(7, 5, 4),
                BlockStateProvider.of(DuskBlocks.CYPRESS_LEAVES),
                CypressFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0)),
                Optional.of(
                    cypressRoots
                ),
                TwoLayersFeatureSize(1, 0, 1)
            ).dirtProvider(cypressLog).ignoreVines().decorators(
                ImmutableList.of<TreeDecorator>(LeavesVineTreeDecorator(0.25f))
            ).build()
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TREES_SWAMP, Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                listOf(
                    WeightedPlacedFeature(
                        placedFeatures.getHolderOrThrow(DuskPlacedFeatures.TALL_CYPRESS),
                        0.85f
                    )
                ), placedFeatures.getHolderOrThrow(DuskPlacedFeatures.CYPRESS)
            )
        )
    }
}