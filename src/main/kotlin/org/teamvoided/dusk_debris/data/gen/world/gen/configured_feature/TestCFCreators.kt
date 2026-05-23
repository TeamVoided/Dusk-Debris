package org.teamvoided.dusk_debris.data.gen.world.gen.configured_feature

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.emptyPlaceInLine
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator.registerConfiguredFeature
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig

object TestCFCreators {
    fun BootstrapContext<ConfiguredFeature<*, *>>.testConfiguredFeatureCreators() {
        val configuredFeatures = this.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = this.lookup(Registries.PLACED_FEATURE)
        val block = this.lookup(Registries.BLOCK)

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
                SimpleStateProvider.simple(Blocks.GRASS_BLOCK),
                SimpleStateProvider.simple(Blocks.DIRT),
                spire
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.LARGE_GRASS_SPIRE,
            DuskFeatures.SURFACE_SPIRE,
            SurfaceFormationFeatureConfig(
                SimpleStateProvider.simple(Blocks.GRASS_BLOCK),
                SimpleStateProvider.simple(Blocks.DIRT),
                spire_large
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.GLASS_SPIKE,
            DuskFeatures.GLASS_SPIKE,
            GlassSpikeFeatureConfig(
                BlockStateProvider.simple(
                    Blocks.TINTED_GLASS.defaultBlockState()
                ),
                BlockTags.REPLACEABLE,
                UniformInt.of(-3, 3),
                UniformInt.of(0, 10),
                UniformInt.of(3, 4),
                UniformInt.of(1, 2),
                -16,
                16,
                0.05
            )
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.TORUS,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                BlockStateProvider.simple(
                    DuskBlocks.CRYSTAL_BLOCK.defaultBlockState()
                ),
                BlockTags.REPLACEABLE,
                UniformInt.of(4, 13),
                UniformInt.of(2, 6),
                UniformInt.of(2, 6),
                UniformFloat.of(0f, 1f),
                UniformFloat.of(0f, 1f),
                UniformFloat.of(0.5f, 1.5f)
            )
        )
        this.createOverworldTorus(
            DuskConfiguredFeatures.COBBLESTONE_TORUS,
            NoiseProvider(
                6789L,
                NormalNoise.NoiseParameters(0, 1.0, *DoubleArray(0)),
                0.5f,
                listOf<BlockState>(
                    Blocks.COBBLESTONE.defaultBlockState(),
                    Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                )
            )
        )
        this.createOverworldTorus(
            DuskConfiguredFeatures.STONE_TORUS,
            BlockStateProvider.simple(Blocks.STONE)
        )
        this.registerConfiguredFeature(
            DuskConfiguredFeatures.OVERWORLD_TORUS,
            Feature.RANDOM_BOOLEAN_SELECTOR,
            RandomBooleanFeatureConfiguration(
                this.emptyPlaceInLine(DuskConfiguredFeatures.COBBLESTONE_TORUS),
                this.emptyPlaceInLine(DuskConfiguredFeatures.STONE_TORUS),
            )
        )
    }

    private val spire = RockFormationFeatureConfig(
        DuskBlockTags.GROUND_AND_REPLACEABLE,
        UniformInt.of(16, 80),
        UniformInt.of(7, 16),
        UniformFloat.of(1f, 3f)
    )

    private val spire_large = RockFormationFeatureConfig(
        DuskBlockTags.GROUND_AND_REPLACEABLE,
        UniformInt.of(48, 120),
        UniformInt.of(17, 24),
        UniformFloat.of(1f, 3f)
    )

    fun BootstrapContext<ConfiguredFeature<*, *>>.createOverworldTorus(
        registryKey: ResourceKey<ConfiguredFeature<*, *>>,
        blockStateProvider: BlockStateProvider
    ) {
        this.registerConfiguredFeature(
            registryKey,
            DuskFeatures.TORUS,
            TorusFeatureConfig(
                blockStateProvider,
                BlockTags.REPLACEABLE,
                UniformInt.of(4, 13),
                UniformInt.of(2, 6),
                UniformInt.of(2, 6),
                UniformFloat.of(0.175f, 0.325f),
                UniformFloat.of(0f, 1f),
                UniformFloat.of(0.5f, 1.5f)
            )
        )
    }
}