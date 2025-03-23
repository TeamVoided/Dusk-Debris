package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.Carver
import net.minecraft.world.gen.carver.CarverConfig
import net.minecraft.world.gen.carver.ConfiguredCarver
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig

object ConfiguredCarverCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredCarver<*>>) {
        val block: HolderProvider<Block> = c.getRegistryLookup<Block>(RegistryKeys.BLOCK)

        c.registerConfiguredCarver(
            DuskConfiguredCarvers.LAKE,
            DuskCarvers.LAKE_CARVER,
            LakeCarverConfig.defaultWithFluid(
                block,
                UniformHeightProvider.create(YOffset.aboveBottom(32), YOffset.fixed(180)),
                Blocks.WATER.defaultState
            )
        )
        c.registerConfiguredCarver(
            DuskConfiguredCarvers.LAVA_LAKE,
            DuskCarvers.LAKE_CARVER,
            LakeCarverConfig.defaultWithFluid(
                block,
                UniformHeightProvider.create(YOffset.aboveBottom(10), YOffset.aboveBottom(48)),
                Blocks.LAVA.defaultState
            )
        )

        val amethystBlock = DataPool.builder<BlockState>()
            .add(Blocks.BUDDING_AMETHYST.defaultState)
            .addWeighted(Blocks.AMETHYST_BLOCK.defaultState, 29)
        val amethystCluster = DataPool.builder<BlockState>()
            .add(Blocks.SMALL_AMETHYST_BUD.defaultState)
            .add(Blocks.MEDIUM_AMETHYST_BUD.defaultState)
            .add(Blocks.LARGE_AMETHYST_BUD.defaultState)
            .add(Blocks.AMETHYST_CLUSTER.defaultState)
        c.registerConfiguredCarver(
            DuskConfiguredCarvers.AMETHYST_GEODE,
            DuskCarvers.GEODE_CARVER,
            GeodeCarverConfig(
                0.005f,
                UniformHeightProvider.create(YOffset.aboveBottom(32), YOffset.aboveBottom(80)),
                UniformFloatProvider.create(0.2f, 1.2f),
                YOffset.aboveBottom(8),
                block.getTagOrThrow(DuskBlockTags.OVERWORLD_GEODE_CARVER_REPLACEABLES),
                UniformIntProvider.create(10, 60),
                SimpleBlockStateProvider.of(Blocks.SMOOTH_BASALT),
                SimpleBlockStateProvider.of(Blocks.CALCITE),
                WeightedBlockStateProvider(amethystBlock),
                WeightedBlockStateProvider(amethystCluster),
            )
        )
    }

    private fun <FC : CarverConfig, F : Carver<FC>> BootstrapContext<ConfiguredCarver<*>>.registerConfiguredCarver(
        registryKey: RegistryKey<ConfiguredCarver<*>>,
        carver: F,
        carverConfig: FC
    ): Any = this.register(registryKey, ConfiguredCarver(carver, carverConfig))
}