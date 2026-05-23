package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HugeMushroomBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
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
                SimpleStateProvider.simple(Blocks.BLACKSTONE.defaultBlockState()),
                BlockTags.BASE_STONE_NETHER,
                0.925f
            )
        )
    }

    private fun BootstrapContext<ConfiguredFeature<*, *>>.nethershrooms() {
        val configuredFeatures = this.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = this.lookup(Registries.PLACED_FEATURE)
        val block = this.lookup(Registries.BLOCK)

        this.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockConfiguration(
                WeightedStateProvider(
                    SimpleWeightedRandomList.builder<BlockState>()
                        .add(DuskBlocks.BLUE_NETHERSHROOM.defaultBlockState(), 24)
                        .add(DuskBlocks.PURPLE_NETHERSHROOM.defaultBlockState())
                )
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
                64, PlacementUtils.inlinePlaced(
                    configuredFeatures.getOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM),
                    BlockPredicateFilter.forPredicate(
                        BlockPredicate.matchesTag(BlockTags.AIR)
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
                BlockStateProvider.simple(
                    DuskBlocks.NETHERSHROOM_STEM.defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                UniformInt.of(5, 10),
                BlockStateProvider.simple(
                    DuskBlocks.BLUE_NETHERSHROOM_BLOCK.defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, true)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                UniformInt.of(2, 4),
                UniformInt.of(2, 5),
                UniformInt.of(1, 2),
                UniformInt.of(1, 4)
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
                96, PlacementUtils.inlinePlaced<RandomFeatureConfiguration, Feature<RandomFeatureConfiguration>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                        listOf(
                            WeightedPlacedFeature(
                                placedFeatures.getOrThrow(DuskPlacedFeatures.HUGE_BLUE_NETHERSHROOM), 0.0001f
                            )
                        ),
                        PlacementUtils.inlinePlaced(
                            configuredFeatures.getOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM),
                            BlockPredicateFilter.forPredicate(
                                BlockPredicate.matchesTag(BlockTags.AIR)
                            )
                        )
                    )
                )
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM,
            Feature.SIMPLE_BLOCK,
            SimpleBlockConfiguration(
                WeightedStateProvider(
                    SimpleWeightedRandomList.builder<BlockState>()
                        .add(DuskBlocks.PURPLE_NETHERSHROOM.defaultBlockState(), 24)
                        .add(DuskBlocks.BLUE_NETHERSHROOM.defaultBlockState())
                )
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
                64, PlacementUtils.inlinePlaced(
                    configuredFeatures.getOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM),
                    BlockPredicateFilter.forPredicate(
                        BlockPredicate.matchesTag(BlockTags.AIR)
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
                BlockStateProvider.simple(
                    DuskBlocks.NETHERSHROOM_STEM.defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                UniformInt.of(5, 10),
                BlockStateProvider.simple(
                    DuskBlocks.PURPLE_NETHERSHROOM_BLOCK.defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, true)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                UniformInt.of(2, 4),
                UniformInt.of(2, 5),
                UniformInt.of(1, 3),
                UniformInt.of(1, 3)
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH,
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
                96, PlacementUtils.inlinePlaced<RandomFeatureConfiguration, Feature<RandomFeatureConfiguration>>(
                    Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                        listOf(
                            WeightedPlacedFeature(
                                placedFeatures.getOrThrow(DuskPlacedFeatures.HUGE_PURPLE_NETHERSHROOM), 0.0001f
                            )
                        ),
                        PlacementUtils.inlinePlaced(
                            configuredFeatures.getOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM),
                            BlockPredicateFilter.forPredicate(
                                BlockPredicate.matchesTag(BlockTags.AIR)
                            )
                        )
                    )
                )
            )
        )
    }
}