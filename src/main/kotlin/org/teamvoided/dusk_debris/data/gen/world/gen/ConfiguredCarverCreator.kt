package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.registry.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.*
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig

@Suppress("DEPRECATION")
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
                UniformHeightProvider.create(YOffset.aboveBottom(8), YOffset.fixed(0)),
                Blocks.LAVA.defaultState
            )
        )
        c.registerConfiguredCarver(
            DuskConfiguredCarvers.AMETHYST_GEODE,
            DuskCarvers.GEODE_CARVER,
            GeodeCarverConfig(
                0.015f,
                UniformHeightProvider.create(YOffset.aboveBottom(8), YOffset.fixed(180)),
                UniformFloatProvider.create(0.4f, 1f),
                YOffset.aboveBottom(8),
                block.getTagOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                UniformIntProvider.create(10, 30),
            )
        )

    }

    private fun <FC : CarverConfig, F : Carver<FC>> BootstrapContext<ConfiguredCarver<*>>.registerConfiguredCarver(
        registryKey: RegistryKey<ConfiguredCarver<*>>,
        carver: F,
        carverConfig: FC
    ): Any = this.register(registryKey, ConfiguredCarver(carver, carverConfig))

//    @Suppress("unused")
//    private fun BootstrapContext<ConfiguredCarver<*>>.registerConfiguredFeature(
//        registryKey: RegistryKey<ConfiguredCarver<*>>, carver: Carver<CarverConfig>
//    ) = this.registerConfiguredCarver(registryKey, carver, CarverConfig.DEFAULT)

}