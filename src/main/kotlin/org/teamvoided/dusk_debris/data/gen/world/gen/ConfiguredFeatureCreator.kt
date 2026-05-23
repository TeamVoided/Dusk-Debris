package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.TreePlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.BiasedToBottomInt
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.NetherCFCreators.netherConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.SwampCFCreators.swampConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.TestCFCreators.testConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.ThresholdPlacedFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.MushroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig
import org.teamvoided.dusk_debris.world.gen.tree.decorator.AttachedToTrunkTreeDecorator
import org.teamvoided.dusk_debris.world.gen.tree.foliage.BirchFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.foliage.OakFoliagePlacer

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = c.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = c.lookup(Registries.PLACED_FEATURE)
        val block = c.lookup(Registries.BLOCK)
        c.netherConfiguredFeatureCreators()
        c.swampConfiguredFeatureCreators()
        c.testConfiguredFeatureCreators()

        c.registerConfiguredFeature(
            DuskConfiguredFeatures.HUGE_GOLD_MUSHROOM,
            DuskFeatures.HUGE_GOLDEN_MUSHROOM,
            MushroomFeatureConfig(
                BlockTags.REPLACEABLE,
                BlockTags.REPLACEABLE,
                BlockStateProvider.simple(DuskBlocks.NETHERSHROOM_STEM),
                BiasedToBottomInt.of(5, 10),
                BlockStateProvider.simple(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK),
                BiasedToBottomInt.of(1, 7),
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.OAK,
            Feature.TREE,
            TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                StraightTrunkPlacer(5, 1, 2),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                OakFoliagePlacer(3, 0),
                TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().decorators(logsOnTrunk(Blocks.OAK_LOG)).build()
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BIRCH,
            Feature.TREE,
            TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.BIRCH_LOG),
                StraightTrunkPlacer(8, 0, 0),
                BlockStateProvider.simple(Blocks.BIRCH_LEAVES),
                BirchFoliagePlacer(3, 0),
                TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().decorators(logsOnTrunk(Blocks.BIRCH_LOG)).build()
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.ROCK,
            DuskFeatures.ROCK
        )


        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BOREAL_VALLEY_VEGETATION,
            DuskFeatures.RANDOM_NOISE_SELECTOR,
            NoiseFeatureConfig(
                -6,
                listOf(1.25, 2.0, 0.0, 2.0),
                listOf(
                    ThresholdPlacedFeature(
                        placedFeatures.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED),
                        0.25f
                    ),
                    ThresholdPlacedFeature(
                        placedFeatures.getOrThrow(TreePlacements.DARK_OAK_CHECKED),
                        -0.25f
                    )
                ),
                placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED)
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.SEQUOIA_TREE,
            DuskFeatures.SEQUOIA_TREE,
            NoneFeatureConfiguration()
        )
    }

    private fun logsOnTrunk(block: Block, probability: Float = 0.15f): List<TreeDecorator> {
        return listOf(
            AttachedToTrunkTreeDecorator(
                probability,
                BlockStateProvider.simple(block.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z)),
                listOf(Direction.NORTH, Direction.SOUTH)
            ),
            AttachedToTrunkTreeDecorator(
                probability,
                BlockStateProvider.simple(block.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X)),
                listOf(Direction.EAST, Direction.WEST)
            )
        )
    }

    fun BootstrapContext<ConfiguredFeature<*, *>>.emptyPlaceInLine(registryKey: ResourceKey<ConfiguredFeature<*, *>>): Holder<PlacedFeature> {
        return PlacementUtils.inlinePlaced(
            this.lookup(Registries.CONFIGURED_FEATURE)
                .getOrThrow(registryKey), *arrayOfNulls<PlacementModifier>(0)
        )
    }

    private fun tree(
        trunk: Block,
        foliage: Block,
        baseHeight: Int,
        firstRandomHeight: Int,
        secondRandomHeight: Int,
        foliageRadius: Int
    ): TreeConfiguration.TreeConfigurationBuilder {
        return TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(trunk),
            StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
            BlockStateProvider.simple(foliage),
            BlobFoliagePlacer(
                ConstantInt.of(foliageRadius), ConstantInt.of(0), 3
            ),
            TwoLayersFeatureSize(1, 0, 1)
        )
    }


    fun <FC : FeatureConfiguration, F : Feature<FC>> BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: ResourceKey<ConfiguredFeature<*, *>>,
        feature: F,
        featureConfig: FC
    ): Any = this.register(registryKey, ConfiguredFeature(feature, featureConfig))

    @Suppress("unused")
    private fun BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: ResourceKey<ConfiguredFeature<*, *>>, feature: Feature<NoneFeatureConfiguration>
    ) = this.registerConfiguredFeature(registryKey, feature, FeatureConfiguration.NONE)

}