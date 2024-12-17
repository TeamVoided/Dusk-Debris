package org.teamvoided.dusk_debris.data.gen.world.gen

import com.google.common.collect.ImmutableList
import net.minecraft.block.*
import net.minecraft.registry.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.unmapped.C_cxbmzbuz
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.root.AboveRootPlacement
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.trunk.StraightTrunkPlacer
import org.teamvoided.dusk_debris.data.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.DuskPlacedFeatures
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.ThresholdPlacedFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import org.teamvoided.dusk_debris.world.gen.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.root.CypressRootPlacer
import org.teamvoided.dusk_debris.world.gen.root.config.CypressRootConfig
import java.util.*

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val block = c.getRegistryLookup(RegistryKeys.BLOCK)



        c.registerConfiguredFeature(
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
        c.registerConfiguredFeature(
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
        c.registerConfiguredFeature(
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
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.TREES_SWAMP, Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                listOf(
                    WeightedPlacedFeature(
                        placedFeatures.getHolderOrThrow(DuskPlacedFeatures.TALL_CYPRESS),
                        0.85f
                    )
                ), placedFeatures.getHolderOrThrow(DuskPlacedFeatures.CYPRESS)
            )
        )




        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockFeatureConfig(
                WeightedBlockStateProvider(
                    DataPool.builder<BlockState>()
                        .addWeighted(DuskBlocks.BLUE_NETHERSHROOM.defaultState, 24)
                        .add(DuskBlocks.PURPLE_NETHERSHROOM.defaultState)
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                64, PlacedFeatureUtil.placedInline(
                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM),
                    BlockPredicateFilterPlacementModifier.create(
                        BlockPredicate.matchingBlockTags(BlockTags.AIR)
                    )
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM,
            DuskFeatures.HUGE_BLUE_NETHERSHROOM,
            HugeNethershroomFeatureConfig(
                DuskBlockTags.NETHERSHROOM_REPLACEABLE,
                DuskBlockTags.NETHERSHROOM_IGNORE,
                BlockStateProvider.of(
                    DuskBlocks.NETHERSHROOM_STEM.defaultState
                        .with(MushroomBlock.UP, false)
                        .with(MushroomBlock.DOWN, false)
                ),
                UniformIntProvider.create(5, 10),
                BlockStateProvider.of(
                    DuskBlocks.BLUE_NETHERSHROOM_BLOCK.defaultState
                        .with(MushroomBlock.UP, true)
                        .with(MushroomBlock.DOWN, false)
                ),
                UniformIntProvider.create(2, 4),
                UniformIntProvider.create(2, 5),
                UniformIntProvider.create(1, 2),
                UniformIntProvider.create(1, 4)
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                96, PlacedFeatureUtil.placedInline<RandomFeatureConfig, Feature<RandomFeatureConfig>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                        listOf(
                            WeightedPlacedFeature(
                                placedFeatures.getHolderOrThrow(DuskPlacedFeatures.HUGE_BLUE_NETHERSHROOM), 0.0001f
                            )
                        ),
                        PlacedFeatureUtil.placedInline(
                            configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM),
                            BlockPredicateFilterPlacementModifier.create(
                                BlockPredicate.matchingBlockTags(BlockTags.AIR)
                            )
                        )
                    )
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockFeatureConfig(
                WeightedBlockStateProvider(
                    DataPool.builder<BlockState>()
                        .addWeighted(DuskBlocks.PURPLE_NETHERSHROOM.defaultState, 24)
                        .add(DuskBlocks.BLUE_NETHERSHROOM.defaultState)
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                64, PlacedFeatureUtil.placedInline(
                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM),
                    BlockPredicateFilterPlacementModifier.create(
                        BlockPredicate.matchingBlockTags(BlockTags.AIR)
                    )
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM,
            DuskFeatures.HUGE_PURPLE_NETHERSHROOM,
            HugeNethershroomFeatureConfig(
                DuskBlockTags.NETHERSHROOM_REPLACEABLE,
                DuskBlockTags.NETHERSHROOM_IGNORE,
                BlockStateProvider.of(
                    DuskBlocks.NETHERSHROOM_STEM.defaultState
                        .with(MushroomBlock.UP, false)
                        .with(MushroomBlock.DOWN, false)
                ),
                UniformIntProvider.create(5, 10),
                BlockStateProvider.of(
                    DuskBlocks.PURPLE_NETHERSHROOM_BLOCK.defaultState
                        .with(MushroomBlock.UP, true)
                        .with(MushroomBlock.DOWN, false)
                ),
                UniformIntProvider.create(2, 4),
                UniformIntProvider.create(2, 5),
                UniformIntProvider.create(1, 3),
                UniformIntProvider.create(1, 3)
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                96, PlacedFeatureUtil.placedInline<RandomFeatureConfig, Feature<RandomFeatureConfig>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                        listOf(
                            WeightedPlacedFeature(
                                placedFeatures.getHolderOrThrow(DuskPlacedFeatures.HUGE_PURPLE_NETHERSHROOM), 0.0001f
                            )
                        ),
                        PlacedFeatureUtil.placedInline(
                            configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM),
                            BlockPredicateFilterPlacementModifier.create(
                                BlockPredicate.matchingBlockTags(BlockTags.AIR)
                            )
                        )
                    )
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.GLASS_SPIKE,
            DuskFeatures.GLASS_SPIKE,
            GlassSpikeFeatureConfig(
                BlockStateProvider.of(
                    Blocks.TINTED_GLASS.defaultState
                ),
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(-3, 3),
                UniformIntProvider.create(0, 10),
                UniformIntProvider.create(3, 4),
                UniformIntProvider.create(1, 2),
                -16,
                16,
                0.05
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.TORUS,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                BlockStateProvider.of(
                    DuskBlocks.CRYSTAL_BLOCK.defaultState
                ),
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(4, 13),
                UniformIntProvider.create(2, 6),
                UniformIntProvider.create(2, 6),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0.5f, 1.5f)
            )
        )
        c.createOverworldTorus(
            DuskConfiguredFeatures.COBBLESTONE_TORUS,
            NoiseBlockStateProvider(
                6789L,
                NoiseParameters(0, 1.0, *DoubleArray(0)),
                0.5f,
                listOf<BlockState>(
                    Blocks.COBBLESTONE.defaultState,
                    Blocks.MOSSY_COBBLESTONE.defaultState
                )
            )
        )
        c.createOverworldTorus(
            DuskConfiguredFeatures.STONE_TORUS,
            BlockStateProvider.of(Blocks.STONE)
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.OVERWORLD_TORUS,
            Feature.RANDOM_BOOLEAN_SELECTOR,
            RandomBooleanFeatureConfig(
                c.emptyPlaceInLine(DuskConfiguredFeatures.COBBLESTONE_TORUS),
                c.emptyPlaceInLine(DuskConfiguredFeatures.STONE_TORUS),
            )
        )

        c.registerConfiguredFeature(
            DuskConfiguredFeatures.FREEZING_WOODS_VEGETATION,
            DuskFeatures.RANDOM_NOISE_SELECTOR,
            NoiseFeatureConfig(
                -6,
                listOf(1.25, 2.0, 0.0, -2.0),
                listOf(
                    ThresholdPlacedFeature(
                        placedFeatures.getHolderOrThrow(TreePlacedFeatures.DARK_OAK_CHECKED),
                        -0.25f
                    ),
                    ThresholdPlacedFeature(
                        placedFeatures.getHolderOrThrow(TreePlacedFeatures.MEGA_SPRUCE_CHECKED),
                        0.25f
                    )
                ),
                placedFeatures.getHolderOrThrow(TreePlacedFeatures.SPRUCE_CHECKED)
            )
        )
    }

    fun BootstrapContext<ConfiguredFeature<*, *>>.emptyPlaceInLine(registryKey: RegistryKey<ConfiguredFeature<*, *>>): Holder<PlacedFeature> {
        return PlacedFeatureUtil.placedInline(
            this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                .getHolderOrThrow(registryKey), *arrayOfNulls<PlacementModifier>(0)
        )
    }

    fun BootstrapContext<ConfiguredFeature<*, *>>.createOverworldTorus(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>,
        blockStateProvider: BlockStateProvider
    ) {
        this.registerConfiguredFeature(
            registryKey,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                blockStateProvider,
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(4, 13),
                UniformIntProvider.create(2, 6),
                UniformIntProvider.create(2, 6),
                UniformFloatProvider.create(0.175f, 0.325f),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0.5f, 1.5f)
            )
        )
    }

    private fun <FC : FeatureConfig, F : Feature<FC>> BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>,
        feature: F,
        featureConfig: FC
    ): Any = this.register(registryKey, ConfiguredFeature(feature, featureConfig))

    @Suppress("unused")
    private fun BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>, feature: Feature<DefaultFeatureConfig>
    ) = this.registerConfiguredFeature(registryKey, feature, FeatureConfig.DEFAULT)

}