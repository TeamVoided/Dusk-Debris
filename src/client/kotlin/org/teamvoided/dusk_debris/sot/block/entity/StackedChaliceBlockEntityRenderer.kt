package org.teamvoided.dusk_debris.sot.block.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.teamvoided.dusk_debris.block.sot.GildedChaliceBlock
import org.teamvoided.dusk_debris.block.sot.entity.StackedChaliceBlockEntity

class StackedChaliceBlockEntityRenderer(
    ctx: BlockEntityRendererProvider.Context,
) : BlockEntityRenderer<StackedChaliceBlockEntity> {

    private val blockRenderer = ctx.blockRenderDispatcher
    private val itemRenderer = ctx.itemRenderer

    override fun render(
        blockEntity: StackedChaliceBlockEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int,
    ) {
        val state = blockEntity.blockState
        val direction = state.getValue(HorizontalDirectionalBlock.FACING)

        matrices.pushPose()
        matrices.rotateAround(Axis.YP.rotationDegrees(-direction.toYRot() - 180), 0.5f, 0.5f, 0.5f)

        val chalices = state.getValue(GildedChaliceBlock.CHALICES)
        val offsets = listOf(
            listOf(0.0 to 0.0),
            listOf(
                4.0 to 1.0,
                -4.0 to -1.0
            ),
            listOf(
                4.0 to 4.0,
                -4.0 to 2.0,
                0.0 to -4.0
            ),
            listOf(
                4.0 to 4.0,
                -4.0 to 2.0,
                3.0 to -3.0,
                -2.0 to -4.0,
            )
        )[chalices - 1]
        for ((index, stack) in blockEntity.chalices.withIndex()) {
            if (stack.isEmpty) break
            val item = stack.item
            if (item !is BlockItem) continue
            val block = item.block

            var state = block.defaultBlockState()
            if (state.hasProperty(BlockStateProperties.FACING)) {
                state = state.setValue(BlockStateProperties.FACING, direction)
            }
            matrices.pushPose()
            val off = offsets[index]
            val x = 0.0625
            matrices.translate(off.first * x, 0.0, off.second * x)
            blockRenderer.renderSingleBlock(state, matrices, vertexConsumers, light, overlay)
            matrices.popPose()
        }
        if (blockEntity.isEmpty()) {
            matrices.pushPose()
            matrices.translate(0.5, 0.5, 0.5)
            itemRenderer.renderStatic(
                Items.BARRIER.defaultInstance, ItemDisplayContext.FIXED,
                light, overlay,
                matrices, vertexConsumers,
                blockEntity.level, 0
            )
            matrices.popPose()
        }
        matrices.popPose()
    }
}
