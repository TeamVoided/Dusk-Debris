package org.teamvoided.dusk_debris.data.gen.world.gen


import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.*
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import org.teamvoided.dusk_debris.data.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.DuskPlacedFeatures
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags

@Suppress("MagicNumber")
object PlacedFeatureCreator {
    fun bootstrap(c: BootstrapContext<PlacedFeature>) {

        val configuredFeatureProvider = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)


        c.register(
            DuskPlacedFeatures.CYPRESS,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.SWAMP_CYPRESS),
            PlacedFeatureUtil.createWouldSurvivePlacementModifier(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TALL_CYPRESS,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.TALL_SWAMP_CYPRESS),
            PlacedFeatureUtil.createWouldSurvivePlacementModifier(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TREES_SWAMP,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.SWAMP_CYPRESS),
            PlacedFeatureUtil.createCountExtraModifier(2, 0.1f, 1),
            InSquarePlacementModifier.getInstance(),
            SurfaceWaterDepthFilterPlacementModifier.create(2),
            PlacedFeatureUtil.OCEAN_FLOOR_HEIGHTMAP,
            BiomePlacementModifier.getInstance(),
            BlockPredicateFilterPlacementModifier.create(
                BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultState, BlockPos.ORIGIN)
            )
        )
        c.register(
            DuskPlacedFeatures.TREES_SWAMP_EXTRA,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.SWAMP_CYPRESS),
            NoiseBasedCountPlacementModifier.create(20, 40.0, 0.0),
            InSquarePlacementModifier.getInstance(),
            SurfaceWaterDepthFilterPlacementModifier.create(2),
            PlacedFeatureUtil.OCEAN_FLOOR_WG_HEIGHTMAP,
            BiomePlacementModifier.getInstance()
        )

        c.register(
            DuskPlacedFeatures.HUGE_BLUE_NETHERSHROOM,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM),
            BlockPredicateFilterPlacementModifier.create(
                BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )
        c.register(
            DuskPlacedFeatures.HUGE_PURPLE_NETHERSHROOM,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM),
            BlockPredicateFilterPlacementModifier.create(
                BlockPredicate.matchingBlockTags(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            )
        )

        c.registerNethershroomPlacement(
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH),
            10,
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH),
            5,
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH),
            10,
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH),
            5,
        )
    }

    private fun BootstrapContext<PlacedFeature>.registerNethershroomPlacement(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        count: Int
    ) {
        this.register(
            registryKey,
            configuredFeature,
            listOf(
                CountPlacementModifier.create(count),
                InSquarePlacementModifier.getInstance(),
                HeightRangePlacementModifier.createUniform(YOffset.getBottom(), YOffset.fixed(128)),
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

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        vararg placementModifiers: PlacementModifier
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers.toList()))

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        placementModifiers: List<PlacementModifier>
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers))

}
