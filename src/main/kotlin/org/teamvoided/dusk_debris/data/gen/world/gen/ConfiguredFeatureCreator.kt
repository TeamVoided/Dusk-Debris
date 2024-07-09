package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.decorator.EnvironmentScanPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.data.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {
    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE)

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
                                PlacedFeatureUtil.placedInline(
                                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM),
                                    BlockPredicateFilterPlacementModifier.create(
                                        BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
                                    )
                                ), 0.0001f
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
                                PlacedFeatureUtil.placedInline(
                                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM),
                                    EnvironmentScanPlacementModifier.create(
                                        Direction.DOWN,
                                        BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON),
                                        BlockPredicate.IS_AIR,
                                        12
                                    )
                                ), 0.0001f
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


//        c.registerConfiguredFeature(
//            TreeConfiguredFeatures.HUGE_RED_MUSHROOM, Feature.HUGE_RED_MUSHROOM, HugeMushroomFeatureConfig(
//                BlockStateProvider.of(
//                    Blocks.RED_MUSHROOM_BLOCK.defaultState.with(MushroomBlock.DOWN, false)
//                ), BlockStateProvider.of(
//                    (Blocks.MUSHROOM_STEM.defaultState.with(
//                        MushroomBlock.UP,
//                        false
//                    )).with(MushroomBlock.DOWN, false)
//                ), 2
//            )
//        )

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