package org.teamvoided.dusk_debris.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.Direction
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

object SignFunctions {
//    @JvmStatic
//    fun setTextAngles(matrices: MatrixStack, front: Boolean) {
//        val translation = Vec3d(0.0, 0.234375, 0.0626)
//        if (!front) {
//            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
//        }
//
//        val scale: Float = 0.015625f * 0.66666f
//        matrices.translate(translation.x, translation.y, translation.z)
//        matrices.scale(scale, -scale, scale)
//    }


    @JvmStatic
    fun renderSignModelBackground(graphics: GuiGraphics, state: BlockState) {
        val scale = 90f
        if (state.block is WallSignBlock) {
            graphics.pose().translate(-scale / 2, scale * 0.484375f, 1f) // 7.75/16
        } else {
            graphics.pose().translate(-scale / 2, scale * 0.734375f, 1f) // 11.75/16
        }
        graphics.pose().scale(scale, -scale, 1f)
        Minecraft.getInstance().blockRenderer.renderSingleBlock(
            state.block.defaultBlockState().trySetValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
            graphics.pose(),
            graphics.bufferSource(),
            15728880,
            OverlayTexture.NO_OVERLAY
        )
    }

    @JvmStatic
    fun offsetSign(graphics: GuiGraphics, state: BlockState, width: Float) {
        graphics.pose().translate(width / 2f, 125f, 50f)
    }
}