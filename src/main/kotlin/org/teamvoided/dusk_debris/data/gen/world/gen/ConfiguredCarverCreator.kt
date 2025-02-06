package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.registry.*
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.carver.*
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig

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