package org.teamvoided.dusk_debris.util

import net.minecraft.block.BlockState
import net.minecraft.block.sign.WallSignBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.render.OverlayTexture
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

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
            graphics.matrices.translate(-scale / 2, scale * 0.484375f, 1f) // 7.75/16
        } else {
            graphics.matrices.translate(-scale / 2, scale * 0.734375f, 1f) // 11.75/16
        }
        graphics.matrices.scale(scale, -scale, 1f)
        MinecraftClient.getInstance().blockRenderManager.renderBlockAsEntity(
            state.block.defaultState.withIfExists(Properties.HORIZONTAL_FACING, Direction.SOUTH),
            graphics.matrices,
            graphics.vertexConsumers,
            15728880,
            OverlayTexture.DEFAULT_UV
        )
    }

    @JvmStatic
    fun offsetSign(graphics: GuiGraphics, state: BlockState, width: Float) {
        graphics.matrices.translate(width / 2f, 125f, 50f)
    }
}