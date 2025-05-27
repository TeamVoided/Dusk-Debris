package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import com.google.common.collect.ImmutableList
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.MushroomBlock
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.unmapped.C_cxbmzbuz
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.ConstantIntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.decorator.BlockPredicateFilterPlacementModifier
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil
import net.minecraft.world.gen.feature.util.PlacedFeatureUtil
import net.minecraft.world.gen.root.AboveRootPlacement
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.emptyPlaceInLine
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.registerConfiguredFeature
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.tree.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.CypressRootPlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.config.CypressRootConfig
import java.util.*

object TestCFCreators {
    fun BootstrapContext<ConfiguredFeature<*, *>>.testConfiguredFeatureCreators() {
        val configuredFeatures = this.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
        val placedFeatures = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val block = this.getRegistryLookup(RegistryKeys.BLOCK)

        this.registerConfiguredFeature(
            DuskConfiguredFeatures.ROCK_SPIRE,
            DuskFeatures.ROCK_SPIRE,
            spire
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_ROCK_SPIRE,
            DuskFeatures.ROCK_SPIRE,
            spire_large
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.GRASS_SPIRE,
            DuskFeatures.SURFACE_SPIRE,
            SurfaceFormationFeatureConfig(
                SimpleBlockStateProvider.of(Blocks.GRASS_BLOCK),
                SimpleBlockStateProvider.of(Blocks.DIRT),
                spire
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_GRASS_SPIRE,
            DuskFeatures.SURFACE_SPIRE,
            SurfaceFormationFeatureConfig(
                SimpleBlockStateProvider.of(Blocks.GRASS_BLOCK),
                SimpleBlockStateProvider.of(Blocks.DIRT),
                spire_large
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.GLASS_SPIKE,
            DuskFeatures.GLASS_SPIKE,
            GlassSpikeFeatureConfig(
                BlockStateProvider.of(
                    Blocks.TINTED_GLASS.defaultState
                ),
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(-3, 3),
                UniformIntProvider.create(0, 10),
                UniformIntProvider.create(3, 4),
                UniformIntProvider.create(1, 2),
                -16,
                16,
                0.05
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TORUS,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                BlockStateProvider.of(
                    DuskBlocks.CRYSTAL_BLOCK.defaultState
                ),
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(4, 13),
                UniformIntProvider.create(2, 6),
                UniformIntProvider.create(2, 6),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0.5f, 1.5f)
            )
        )
        this.createOverworldTorus(
            DuskConfiguredFeatures.COBBLESTONE_TORUS,
            NoiseBlockStateProvider(
                6789L,
                DoublePerlinNoiseSampler.NoiseParameters(0, 1.0, *DoubleArray(0)),
                0.5f,
                listOf<BlockState>(
                    Blocks.COBBLESTONE.defaultState,
                    Blocks.MOSSY_COBBLESTONE.defaultState
                )
            )
        )
        this.createOverworldTorus(
            DuskConfiguredFeatures.STONE_TORUS,
            BlockStateProvider.of(Blocks.STONE)
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.OVERWORLD_TORUS,
            Feature.RANDOM_BOOLEAN_SELECTOR,
            RandomBooleanFeatureConfig(
                this.emptyPlaceInLine(DuskConfiguredFeatures.COBBLESTONE_TORUS),
                this.emptyPlaceInLine(DuskConfiguredFeatures.STONE_TORUS),
            )
        )
    }

    private val spire = RockFormationFeatureConfig(
        DuskBlockTags.GROUND_AND_REPLACEABLE,
        UniformIntProvider.create(16, 80),
        UniformIntProvider.create(7, 16),
        UniformFloatProvider.create(1f, 3f)
    )

    private val spire_large = RockFormationFeatureConfig(
        DuskBlockTags.GROUND_AND_REPLACEABLE,
        UniformIntProvider.create(48, 120),
        UniformIntProvider.create(17, 24),
        UniformFloatProvider.create(1f, 3f)
    )

    fun BootstrapContext<ConfiguredFeature<*, *>>.createOverworldTorus(
        registryKey: RegistryKey<ConfiguredFeature<*, *>>,
        blockStateProvider: BlockStateProvider
    ) {
        this.registerConfiguredFeature(
            registryKey,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                blockStateProvider,
                BlockTags.REPLACEABLE,
                UniformIntProvider.create(4, 13),
                UniformIntProvider.create(2, 6),
                UniformIntProvider.create(2, 6),
                UniformFloatProvider.create(0.175f, 0.325f),
                UniformFloatProvider.create(0f, 1f),
                UniformFloatProvider.create(0.5f, 1.5f)
            )
        )
    }
}