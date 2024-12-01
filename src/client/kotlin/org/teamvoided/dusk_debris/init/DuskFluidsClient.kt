package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.minecraft.client.render.RenderLayer
import org.teamvoided.dusk_debris.DuskDebris

object DuskFluidsClient {
    fun init() {
        FluidRenderHandlerRegistry.INSTANCE.register(
            DuskFluids.ACID, DuskFluids.FLOWING_ACID,
            SimpleFluidRenderHandler(
                DuskDebris.id("liquid/acid"),
                DuskDebris.id("liquid/acid")
            )
        )
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), DuskFluids.ACID, DuskFluids.FLOWING_ACID)
    }
}