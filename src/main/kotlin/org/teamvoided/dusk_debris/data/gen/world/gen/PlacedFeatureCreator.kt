package org.teamvoided.dusk_debris.data.gen.world.gen


import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.*
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.data.gen.world.gen.PlacedFeatureCreator.register
import org.teamvoided.dusk_debris.init.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskPlacedFeatures

@Suppress("MagicNumber")
object PlacedFeatureCreator {
    fun bootstrap(c: BootstrapContext<PlacedFeature>) {

        val configuredFeatureProvider = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)

        c.registerNethershroomPlacement(
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.BLUE_NETHERSHROOM_PATCH),
            CountPlacementModifier.create(25),
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.LARGE_BLUE_NETHERSHROOM_PATCH),
            CountPlacementModifier.create(50),
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.PURPLE_NETHERSHROOM_PATCH),
            CountPlacementModifier.create(25),
        )
        c.registerNethershroomPlacement(
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            configuredFeatureProvider.getHolderOrThrow(DuskConfiguredFeatures.LARGE_PURPLE_NETHERSHROOM_PATCH),
            CountPlacementModifier.create(50),
        )
    }

    private fun BootstrapContext<PlacedFeature>.registerNethershroomPlacement(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        placementModifiers: PlacementModifier
    ) {
        this.register(
            registryKey,
            configuredFeature,
            listOf(
                placementModifiers,
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

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: RegistryKey<PlacedFeature>,
        configuredFeature: Holder<ConfiguredFeature<*, *>>,
        vararg placementModifiers: PlacementModifier
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers.toList()))

    fun BootstrapContext<PlacedFeature>.register(
        registryKey: RegistryKey<PlacedFeature>, configuredFeature: Holder<ConfiguredFeature<*, *>>,
        placementModifiers: List<PlacementModifier>
    ): Any = this.register(registryKey, PlacedFeature(configuredFeature, placementModifiers))

}
