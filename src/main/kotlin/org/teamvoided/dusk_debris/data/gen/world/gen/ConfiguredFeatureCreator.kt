package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {
    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {

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