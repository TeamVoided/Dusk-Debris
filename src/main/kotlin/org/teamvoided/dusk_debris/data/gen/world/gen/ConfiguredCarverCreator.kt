package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.carver.CarverConfiguration
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig

object ConfiguredCarverCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredWorldCarver<*>>) {
        val block: HolderGetter<Block> = c.lookup<Block>(Registries.BLOCK)

        c.registerConfiguredCarver(
            DuskConfiguredCarvers.LAKE,
            DuskCarvers.LAKE_CARVER,
            LakeCarverConfig.defaultWithFluid(
                block,
                UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(180)),
                Blocks.WATER.defaultBlockState()
            )
        )
        c.registerConfiguredCarver(
            DuskConfiguredCarvers.LAVA_LAKE,
            DuskCarvers.LAKE_CARVER,
            LakeCarverConfig.defaultWithFluid(
                block,
                UniformHeight.of(VerticalAnchor.aboveBottom(10), VerticalAnchor.aboveBottom(48)),
                Blocks.LAVA.defaultBlockState()
            )
        )

        val amethystBlock = SimpleWeightedRandomList.builder<BlockState>()
            .add(Blocks.BUDDING_AMETHYST.defaultBlockState())
            .add(Blocks.AMETHYST_BLOCK.defaultBlockState(), 29)
        val amethystCluster = SimpleWeightedRandomList.builder<BlockState>()
            .add(Blocks.SMALL_AMETHYST_BUD.defaultBlockState())
            .add(Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState())
            .add(Blocks.LARGE_AMETHYST_BUD.defaultBlockState())
            .add(Blocks.AMETHYST_CLUSTER.defaultBlockState())
        c.registerConfiguredCarver(
            DuskConfiguredCarvers.AMETHYST_GEODE,
            DuskCarvers.GEODE_CARVER,
            GeodeCarverConfig(
                0.005f,
                UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(80)),
                UniformFloat.of(0.2f, 1.2f),
                VerticalAnchor.aboveBottom(8),
                block.getOrThrow(DuskBlockTags.OVERWORLD_GEODE_CARVER_REPLACEABLES),
                UniformInt.of(10, 60),
                SimpleStateProvider.simple(Blocks.SMOOTH_BASALT),
                SimpleStateProvider.simple(Blocks.CALCITE),
                WeightedStateProvider(amethystBlock),
                WeightedStateProvider(amethystCluster),
            )
        )
    }

    private fun <FC : CarverConfiguration, F : WorldCarver<FC>> BootstrapContext<ConfiguredWorldCarver<*>>.registerConfiguredCarver(
        registryKey: ResourceKey<ConfiguredWorldCarver<*>>,
        carver: F,
        carverConfig: FC
    ): Any = this.register(registryKey, ConfiguredWorldCarver(carver, carverConfig))
}