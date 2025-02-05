package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.*
import net.minecraft.registry.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.unmapped.C_cxbmzbuz
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.carver.*
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig
import java.util.*

@Suppress("DEPRECATION")
object ConfiguredCarverCreator {

    fun bootstrap(c: BootstrapContext<ConfiguredCarver<*>>) {
        val block: HolderProvider<Block> = c.getRegistryLookup<Block>(RegistryKeys.BLOCK)

        c.registerConfiguredCarver(
            DuskConfiguredCarvers.LAKE,
            DuskCarvers.LAKE_CARVER,
            LakeCarverConfig(
                0.15f,
                UniformHeightProvider.create(YOffset.aboveBottom(8), YOffset.fixed(180)),
                UniformFloatProvider.create(0.3f, 1f),
                YOffset.aboveBottom(8),
                LakeCarverDebugConfig.default(true),
                block.getTagOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),

                UniformFloatProvider.create(0.7f, 1.4f),
                UniformFloatProvider.create(0.8f, 1.3f),
                Blocks.WATER.defaultState,
                UniformFloatProvider.create(-0.4f, 0.2f)
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