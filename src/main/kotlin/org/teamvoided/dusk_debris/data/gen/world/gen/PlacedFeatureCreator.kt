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
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.PlacementModifier
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import org.teamvoided.dusk_debris.data.gen.world.gen.placed_feature_creators.NetherPlacedFeatureCreators.netherPlacedFeatureCreators
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags

@Suppress("MagicNumber")
object PlacedFeatureCreator {
    fun bootstrap(c: BootstrapContext<PlacedFeature>) {

        val configuredFeatureProvider = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        c.netherPlacedFeatureCreators()

        c.register(
            DuskPlacedFeatures.CYPRESS,
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            PlacedFeatureUtil.createWouldSurvivePlacementModifier(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TALL_CYPRESS,
            DuskConfiguredFeatures.TALL_SWAMP_CYPRESS,
            PlacedFeatureUtil.createWouldSurvivePlacementModifier(Blocks.OAK_SAPLING)
        )
        c.register(
            DuskPlacedFeatures.TREES_SWAMP,
            DuskConfiguredFeatures.SWAMP_CYPRESS,
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
            DuskConfiguredFeatures.SWAMP_CYPRESS,
            NoiseBasedCountPlacementModifier.create(20, 40.0, 0.0),
            InSquarePlacementModifier.getInstance(),
            SurfaceWaterDepthFilterPlacementModifier.create(2),
            PlacedFeatureUtil.OCEAN_FLOOR_WG_HEIGHTMAP,
            BiomePlacementModifier.getInstance()
        )



        c.register(
            DuskPlacedFeatures.TORUS,
            DuskConfiguredFeatures.TORUS,
            RarityFilterPlacementModifier.create(5),
            InSquarePlacementModifier.getInstance(),
            HeightRangePlacementModifier.createUniform(YOffset.aboveBottom(16), YOffset.fixed(106)),
            EnvironmentScanPlacementModifier.create(
                Direction.DOWN,
                BlockPredicate.solid(),
                BlockPredicate.matchingBlocks(Blocks.AIR, Blocks.LAVA),
                12
            ),
            BiomePlacementModifier.getInstance()
        )
        c.register(
            DuskPlacedFeatures.OVERWORLD_TORUS,
            DuskConfiguredFeatures.OVERWORLD_TORUS,
            RarityFilterPlacementModifier.create(100),
            InSquarePlacementModifier.getInstance(),
            PlacedFeatureUtil.OCEAN_FLOOR_WG_HEIGHTMAP,
            BiomePlacementModifier.getInstance()
        )

        c.register(
            DuskPlacedFeatures.BOREAL_VALLEY_VEGETATION, DuskConfiguredFeatures.BOREAL_VALLEY_VEGETATION,
            CountPlacementModifier.create(16),
            InSquarePlacementModifier.getInstance(),
            SurfaceWaterDepthFilterPlacementModifier.create(0),
            PlacedFeatureUtil.OCEAN_FLOOR_HEIGHTMAP,
            BiomePlacementModifier.getInstance()
        )
    }

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: RegistryKey<ConfiguredFeature<*, *>>,
        vararg placementModifiers: PlacementModifier
    ): Any = this.register(
        registryKey,
        PlacedFeature(
            this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getHolderOrThrow(configuredFeature),
            placementModifiers.toList()
        )
    )

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
