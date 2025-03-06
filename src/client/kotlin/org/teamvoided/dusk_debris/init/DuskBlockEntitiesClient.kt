package org.teamvoided.dusk_debris.init

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.stone_chest.StoneChestBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityRenderer

object DuskBlockEntitiesClient {
    fun init() {
//        BuiltinItemRendererRegistry.INSTANCE.register(DuskItems.STRAY_SKULL)
        BlockEntityRendererFactories.register(DuskBlockEntities.TREASURE_CHEST, ::TreasureChestBlockEntityRenderer)
        BlockEntityRendererFactories.register(DuskBlockEntities.STONE_CHEST, ::StoneChestBlockEntityRenderer)
    }
}