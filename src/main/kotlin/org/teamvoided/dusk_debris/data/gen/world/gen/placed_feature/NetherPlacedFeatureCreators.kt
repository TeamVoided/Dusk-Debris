package org.teamvoided.dusk_debris.data.gen.world.gen.placed_feature

import net.minecraft.core.Direction
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.*
import org.teamvoided.dusk_debris.data.gen.world.gen.PlacedFeatureCreator.register
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures

object NetherPlacedFeatureCreators {
    fun BootstrapContext<PlacedFeature>.netherPlacedFeatureCreators() {
        val configuredFeatureProvider = this.lookup(Registries.CONFIGURED_FEATURE)
        this.nethershrooms(configuredFeatureProvider)
        this.register(
            DuskPlacedFeatures.BLACKSTONE_STRIPS,
            configuredFeatureProvider.getOrThrow(DuskConfiguredFeatures.BLACKSTONE_STRIPS),
            CountPlacement.of(40),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
            EnvironmentScanPlacement.scanningFor(
                Direction.DOWN,
                BlockPredicate.solid(),
                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                12
            ),
            BiomeFilter.biome()
        )
    }

    private fun BootstrapContext<PlacedFeature>.nethershrooms(configuredFeatureProvider: HolderGetter<ConfiguredFeature<*, *>>) {
        this.register(
            DuskPlacedFeatures.HUGE_BLUE_NETHERSHROOM,
            configuredFeatureProvider.getOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM),
            BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesTag(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )
        this.register(
            DuskPlacedFeatures.HUGE_PURPLE_NETHERSHROOM,
            configuredFeatureProvider.getOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM),
            BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesTag(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )

        this.registerNethershroomPlacement(
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH,
            RarityFilter.onAverageOnceEvery(7),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH,
            CountPlacement.of(1),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH,
            RarityFilter.onAverageOnceEvery(7),
        )
        this.registerNethershroomPlacement(
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH,
            CountPlacement.of(1),
        )
    }

    private fun BootstrapContext<PlacedFeature>.registerNethershroomPlacement(
        registryKey: ResourceKey<PlacedFeature>,
        configuredFeature: ResourceKey<ConfiguredFeature<*, *>>,
        count: PlacementModifier
    ) {
        this.register(
            registryKey,
            this.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature),
            listOf(
                count,
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.belowTop(128)),
                EnvironmentScanPlacement.scanningFor(
                    Direction.DOWN,
                    BlockPredicate.solid(),
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    12
                ),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome()
            )
        )
    }
}