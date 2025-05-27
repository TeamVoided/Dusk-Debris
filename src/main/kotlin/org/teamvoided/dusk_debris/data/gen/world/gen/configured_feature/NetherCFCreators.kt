package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.MushroomBlock
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.registerConfiguredFeature
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig

object NetherCFCreators {
    fun BootstrapContext<ConfiguredFeature<*, *>>.netherConfiguredFeatureCreators() {
        this.nethershrooms()
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.BLACKSTONE_STRIPS,
            DuskFeatures.NOISE_SURFACE,
            NoiseSurfaceFeatureConfig(
                SimpleBlockStateProvider.of(Blocks.BLACKSTONE.defaultState),
                BlockTags.BASE_STONE_NETHER,
                0.925f
            )
        )
    }

    private fun BootstrapContext<ConfiguredFeature<*, *>>.nethershrooms() {
        val configuredFeatures = this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val block = this.getRegistryLookup(RegistryKeys.BLOCK)

        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
        this.registerConfiguredFeature(
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
    }
}