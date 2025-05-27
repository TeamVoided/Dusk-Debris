package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.foliage.BlobFoliagePlacer
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.trunk.StraightTrunkPlacer
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.NetherCFCreators.netherConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.SwampCFCreators.swampConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature.TestCFCreators.testConfiguredFeatureCreators
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.ThresholdPlacedFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig
import org.teamvoided.dusk_debris.world.gen.tree.foliage.OakFoliagePlacer

@Suppress("DEPRECATION")
object ConfiguredFeatureCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val block = c.getRegistryLookup(RegistryKeys.BLOCK)
        c.netherConfiguredFeatureCreators()
        c.swampConfiguredFeatureCreators()
        c.testConfiguredFeatureCreators()

        c.registerConfiguredFeature(
            DuskConfiguredFeatures.OAK,
            Feature.TREE,
            TreeFeatureConfig.Builder(
                BlockStateProvider.of(Blocks.OAK_LOG),
                StraightTrunkPlacer(6, 0, 0),
                BlockStateProvider.of(Blocks.OAK_LEAVES),
                OakFoliagePlacer(3, 0),
                TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build()
        )


        c.registerConfiguredFeature(
            DuskConfiguredFeatures.BOREAL_VALLEY_VEGETATION,
            DuskFeatures.RANDOM_NOISE_SELECTOR,
            NoiseFeatureConfig(
                -6,
                listOf(1.25, 2.0, 0.0, -2.0),
                listOf(
                    ThresholdPlacedFeature(
                        placedFeatures.getHolderOrThrow(TreePlacedFeatures.MEGA_SPRUCE_CHECKED),
                        0.25f
                    ),
                    ThresholdPlacedFeature(
                        placedFeatures.getHolderOrThrow(TreePlacedFeatures.DARK_OAK_CHECKED),
                        -0.25f
                    )
                ),
                placedFeatures.getHolderOrThrow(TreePlacedFeatures.SPRUCE_CHECKED)
            )
        )
        c.registerConfiguredFeature(
            DuskConfiguredFeatures.SEQUOIA_TREE,
            DuskFeatures.SEQUOIA_TREE,
            DefaultFeatureConfig()
        )
    }

    fun BootstrapContext<ConfiguredFeature<*, *>>.emptyPlaceInLine(registryKey: RegistryKey<ConfiguredFeature<*, *>>): Holder<PlacedFeature> {
        return PlacedFeatureUtil.placedInline(
            this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                .getHolderOrThrow(registryKey), *arrayOfNulls<PlacementModifier>(0)
        )
    }

    private fun tree(
        trunk: Block,
        foliage: Block,
        baseHeight: Int,
        firstRandomHeight: Int,
        secondRandomHeight: Int,
        foliageRadius: Int
    ): TreeFeatureConfig.Builder {
        return TreeFeatureConfig.Builder(
            BlockStateProvider.of(trunk),
            StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
            BlockStateProvider.of(foliage),
            BlobFoliagePlacer(
                ConstantIntProvider.create(foliageRadius), ConstantIntProvider.create(0), 3
            ),
            TwoLayersFeatureSize(1, 0, 1)
        )
    }


    fun <FC : FeatureConfig, F : Feature<FC>> BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>,
        feature: F,
        featureConfig: FC
    ): Any = this.register(registryKey, ConfiguredFeature(feature, featureConfig))

    @Suppress("unused")
    private fun BootstrapContext<ConfiguredFeature<*, *>>.registerConfiguredFeature(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>, feature: Feature<DefaultFeatureConfig>
    ) = this.registerConfiguredFeature(registryKey, feature, FeatureConfig.DEFAULT)

}