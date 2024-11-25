package org.teamvoided.dusk_debris.init

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.dusk_debris.entity.block.ChestBlockEntityRenderer

object DuskBlockEntitiesClient {
    fun init() {
//        BuiltinItemRendererRegistry.INSTANCE.register(DuskItems.STRAY_SKULL)
        BlockEntityRendererFactories.register(DuskBlockEntities.TREASURE_CHEST, ::ChestBlockEntityRenderer)
    }
}