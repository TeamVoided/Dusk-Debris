package org.teamvoided.dusk_debris.data.gen.world.gen.placed_feature_creators

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.*
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.PlacementModifier
import org.teamvoided.dusk_debris.data.gen.world.gen.PlacedFeatureCreator.register
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures

object NetherPlacedFeatureCreators {
    fun BootstrapContext<PlacedFeature>.netherPlacedFeatureCreators() {
        val configuredFeatureProvider = this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        this.nethershrooms(configuredFeatureProvider)
        this.register(
            DuskPlacedFeatures.BLACKSTONE_STRIPS,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.BLACKSTONE_STRIPS),
            CountPlacementModifier.create(30),
            InSquarePlacementModifier.getInstance(),
            HeightRangePlacementModifier.createUniform(YOffset.getBottom(), YOffset.getTop()),
            EnvironmentScanPlacementModifier.create(
                Direction.DOWN,
                BlockPredicate.solid(),
                BlockPredicate.IS_AIR,
                12
            ),
            BiomePlacementModifier.getInstance()
        )
    }

    private fun BootstrapContext<PlacedFeature>.nethershrooms(configuredFeatureProvider: HolderProvider<ConfiguredFeature<*, *>>) {
        this.register(
            DuskPlacedFeatures.HUGE_BLUE_NETHERSHROOM,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM),
            BlockPredicateFilterPlacementModifier.create(
                BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )
        this.register(
            DuskPlacedFeatures.HUGE_PURPLE_NETHERSHROOM,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM),
            BlockPredicateFilterPlacementModifier.create(
                BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )

        this.registerNethershroomPlacement(
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH,
            RarityFilterPlacementModifier.create(7),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH,
            CountPlacementModifier.create(1),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH,
            RarityFilterPlacementModifier.create(7),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH,
            CountPlacementModifier.create(1),
        )
    }

    private fun BootstrapContext<PlacedFeature>.registerNethershroomPlacement(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: RegistryKey<ConfiguredFeature<*, *>>,
        count: PlacementModifier
    ) {
        this.register(
            registryKey,
            this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getHolderOrThrow(configuredFeature),
            listOf(
                count,
                InSquarePlacementModifier.getInstance(),
                HeightRangePlacementModifier.createUniform(YOffset.getBottom(), YOffset.belowTop(128)),
                EnvironmentScanPlacementModifier.create(
                    Direction.DOWN,
                    BlockPredicate.solid(),
                    BlockPredicate.IS_AIR,
                    12
                ),
                RandomOffsetPlacementModifier.vertical(ConstantIntProvider.create(1)),
                BiomePlacementModifier.getInstance()
            )
        )
    }
}