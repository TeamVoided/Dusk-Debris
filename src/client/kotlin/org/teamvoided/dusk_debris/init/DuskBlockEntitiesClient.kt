package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.block.entity.DecoratedPotBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.entity.block.CelestalBellBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.stone_chest.StoneChestBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityRenderer
import org.teamvoided.dusks_and_dungeons.block.entity.ChestOSoulsBlockEntity
import org.teamvoided.dusks_and_dungeons.entity.block.QuarterBlockPileBlockEntityRenderer

object DuskBlockEntitiesClient {
    private var decoratedPotBlockEntity = DecoratedPotBlockEntity(BlockPos.ORIGIN, DuskBlocks.POT_O_SCREAMS.defaultState)
    private var chestOSoulsBlockEntity = ChestOSoulsBlockEntity(BlockPos.ORIGIN, DuskBlocks.CHEST_O_SOULS.defaultState)

    fun init() {
//        BuiltinItemRendererRegistry.INSTANCE.register(DuskItems.STRAY_SKULL)
        BlockEntityRendererFactories.register(DuskBlockEntities.TREASURE_CHEST, ::TreasureChestBlockEntityRenderer)
        BlockEntityRendererFactories.register(DuskBlockEntities.STONE_CHEST, ::StoneChestBlockEntityRenderer)

        // DnD
        BlockEntityRendererFactories.register(DuskBlockEntities.CELESTAL_BELL, ::CelestalBellBlockEntityRenderer)
        BlockEntityRendererFactories.register(DuskBlockEntities.CHEST_O_SOULS, ::ChestBlockEntityRenderer)
        BlockEntityRendererFactories.register(DuskBlockEntities.QUARTER_BLOCK_PILE, ::QuarterBlockPileBlockEntityRenderer)

        BuiltinItemRendererRegistry.INSTANCE.register(DuskBlocks.POT_O_SCREAMS) { stack, mode, matrices, vertexConsumers, light, overlay ->
            MinecraftClient.getInstance().blockEntityRenderDispatcher.renderEntity(
                decoratedPotBlockEntity, matrices, vertexConsumers, light, overlay
            )
        }
        BuiltinItemRendererRegistry.INSTANCE.register(DuskBlocks.CHEST_O_SOULS) { stack, mode, matrices, vertextConsumers, light, overlay ->
            MinecraftClient.getInstance().blockEntityRenderDispatcher.renderEntity(
                chestOSoulsBlockEntity, matrices, vertextConsumers, light, overlay
            )
        }
    }
}