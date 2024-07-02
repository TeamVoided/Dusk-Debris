package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import java.util.List

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {
    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE)

        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockFeatureConfig(BlockStateProvider.of(DuskBlocks.BLUE_NETHERSHROOM))
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
                UniformIntProvider.create(1, 3),
                UniformIntProvider.create(1, 4)
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                64, PlacedFeatureUtil.placedInline(
                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM)
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                10, PlacedFeatureUtil.placedInline<RandomFeatureConfig, Feature<RandomFeatureConfig>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                        listOf(
                            WeightedPlacedFeature(
                                PlacedFeatureUtil.placedInline(
                                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM),
                                    BlockPredicateFilterPlacementModifier.create(
                                        BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_PLACEABLE_ON)
                                    )
                                ), 0.01f
                            )
                        ),
                        PlacedFeatureUtil.placedInline(configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM))
                    )
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockFeatureConfig(BlockStateProvider.of(DuskBlocks.PURPLE_NETHERSHROOM))
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
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                64, PlacedFeatureUtil.placedInline(
                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM)
                )
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            ConfiguredFeatureUtil.createRandomPatchFeatureConfig(
                10, PlacedFeatureUtil.placedInline<RandomFeatureConfig, Feature<RandomFeatureConfig>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                        listOf(
                            WeightedPlacedFeature(
                                PlacedFeatureUtil.placedInline(
                                    configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM),
                                    BlockPredicateFilterPlacementModifier.create(
                                        BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_PLACEABLE_ON)
                                    )
                                ), 0.01f
                            )
                        ),
                        PlacedFeatureUtil.placedInline(configuredFeatures.getHolderOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM))
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