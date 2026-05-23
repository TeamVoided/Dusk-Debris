package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.ChestRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity
import org.teamvoided.dusk_debris.entity.block.CelestalBellBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.StatueBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.stone_chest.StoneChestBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityRenderer
import org.teamvoided.dusk_debris.sot.block.entity.StackedChaliceBlockEntityRenderer
import org.teamvoided.dusks_and_dungeons.block.entity.ChestOSoulsBlockEntity
import org.teamvoided.dusks_and_dungeons.entity.block.QuarterBlockPileBlockEntityRenderer

object DuskBlockEntitiesClient {
    private var decoratedPotBlockEntity = DecoratedPotBlockEntity(BlockPos.ZERO, DuskBlocks.POT_O_SCREAMS.defaultBlockState())
    private var chestOSoulsBlockEntity = ChestOSoulsBlockEntity(BlockPos.ZERO, DuskBlocks.CHEST_O_SOULS.defaultBlockState())

    fun init() {
//        BuiltinItemRendererRegistry.INSTANCE.register(DuskItems.STRAY_SKULL)
        BlockEntityRenderers.register(DuskBlockEntities.TREASURE_CHEST, ::TreasureChestBlockEntityRenderer)
        BlockEntityRenderers.register(DuskBlockEntities.STONE_CHEST, ::StoneChestBlockEntityRenderer)

        // DnD
        BlockEntityRenderers.register(DuskBlockEntities.CELESTAL_BELL, ::CelestalBellBlockEntityRenderer)
        BlockEntityRenderers.register(DuskBlockEntities.CHEST_O_SOULS, ::ChestRenderer)
        BlockEntityRenderers.register(DuskBlockEntities.QUARTER_BLOCK_PILE, ::QuarterBlockPileBlockEntityRenderer)

        BuiltinItemRendererRegistry.INSTANCE.register(DuskBlocks.POT_O_SCREAMS) { stack, mode, matrices, vertexConsumers, light, overlay ->
            Minecraft.getInstance().blockEntityRenderDispatcher.renderItem(
                decoratedPotBlockEntity, matrices, vertexConsumers, light, overlay
            )
        }
        BuiltinItemRendererRegistry.INSTANCE.register(DuskBlocks.CHEST_O_SOULS) { stack, mode, matrices, vertextConsumers, light, overlay ->
            Minecraft.getInstance().blockEntityRenderDispatcher.renderItem(
                chestOSoulsBlockEntity, matrices, vertextConsumers, light, overlay
            )
        }

        BlockEntityRenderers.register(DuskBlockEntities.STATUE, ::StatueBlockEntityRenderer)
        BlockEntityRenderers.register(DuskBlockEntities.STACKED_CHALICE, ::StackedChaliceBlockEntityRenderer)

    }
}