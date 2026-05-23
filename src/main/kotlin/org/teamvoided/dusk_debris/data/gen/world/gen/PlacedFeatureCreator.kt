package org.teamvoided.dusk_debris.data.gen.world.gen


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.*
import org.teamvoided.dusk_debris.data.gen.world.gen.placed_feature.NetherPlacedFeatureCreators.netherPlacedFeatureCreators
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures

@Suppress("MagicNumber")
object PlacedFeatureCreator {
    fun bootstrap(c: BootstrapContext<PlacedFeature>) {

        val configuredFeatureProvider = c.lookup(Registries.CONFIGURED_FEATURE)
        c.netherPlacedFeatureCreators()

        c.register(
            DuskPlacedFeatures.OAK_CHECHED,
            DuskConfiguredFeatures.OAK,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )

        c.register(
            DuskPlacedFeatures.CYPRESS,
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TALL_CYPRESS,
            DuskConfiguredFeatures.TALL_SWAMP_CYPRESS,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TREES_SWAMP,
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            PlacementUtils.countExtra(2, 0.1f, 1),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(2),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome(),
            BlockPredicateFilter.forPredicate(
                BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)
            )
        )
        c.register(
            DuskPlacedFeatures.TREES_SWAMP_EXTRA,
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            NoiseBasedCountPlacement.of(20, 40.0, 0.0),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(2),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
        )



        c.register(
            DuskPlacedFeatures.TORUS,
            DuskConfiguredFeatures.TORUS,
            RarityFilter.onAverageOnceEvery(5),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(106)),
            EnvironmentScanPlacement.scanningFor(
                Direction.DOWN,
                BlockPredicate.solid(),
                BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.LAVA),
                12
            ),
            BiomeFilter.biome()
        )
        c.register(
            DuskPlacedFeatures.OVERWORLD_TORUS,
            DuskConfiguredFeatures.OVERWORLD_TORUS,
            RarityFilter.onAverageOnceEvery(100),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
        )

        c.register(
            DuskPlacedFeatures.LARGE_ROCK_SPIRE,
            DuskConfiguredFeatures.LARGE_ROCK_SPIRE,
            RarityFilter.onAverageOnceEvery(100),
            RandomOffsetPlacement.horizontal(ConstantInt.of(8)),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
        )

        c.register(
            DuskPlacedFeatures.BOREAL_VALLEY_VEGETATION, DuskConfiguredFeatures.BOREAL_VALLEY_VEGETATION,
            CountPlacement.of(16),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome()
        )
    }

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: ResourceKey<PlacedFeature>,
        configuredFeature: ResourceKey<ConfiguredFeature<*, *>>,
        vararg placementModifiers: PlacementModifier
    ): Any = this.register(
        registryKey,
        PlacedFeature(
            this.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature),
            placementModifiers.toList()
        )
    )

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: ResourceKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        vararg placementModifiers: PlacementModifier
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers.toList()))

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: ResourceKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        placementModifiers: List<PlacementModifier>
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers))

}
