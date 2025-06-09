package org.teamvoided.dusk_debris.sot.block.entity

import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.BlockItem
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.block.sot.GildedChaliceBlock
import org.teamvoided.dusk_debris.block.sot.entity.StackedChaliceBlockEntity

class StackedChaliceBlockEntityRenderer(
    ctx: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<StackedChaliceBlockEntity> {

    private val blockRenderer = ctx.renderManager
    private val itemRenderer = ctx.itemRenderer

    override fun render(
        blockEntity: StackedChaliceBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val state = blockEntity.cachedState
        val direction = state.get(HorizontalFacingBlock.FACING)

        matrices.push()
        matrices.rotateAround(Axis.Y_POSITIVE.rotationDegrees(-direction.asRotation() - 180), 0.5f, 0.5f, 0.5f)

        val chalices = state.get(GildedChaliceBlock.CHALICES)
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

            var state = block.defaultState
            if (state.contains(Properties.FACING)) {
                state = state.with(Properties.FACING, direction)
            }
            matrices.push()
            val off = offsets[index]
            val x = 0.0625
            matrices.translate(off.first * x, 0.0, off.second * x)
            blockRenderer.renderBlockAsEntity(state, matrices, vertexConsumers, light, overlay)
            matrices.pop()
        }
        if (blockEntity.isEmpty()) {
            matrices.push()
            matrices.translate(0.5, 0.5, 0.5)
            itemRenderer.renderItem(
                Items.BARRIER.defaultStack, ModelTransformationMode.FIXED,
                light, overlay,
                matrices, vertexConsumers,
                blockEntity.world, 0
            )
            matrices.pop()
        }
        matrices.pop()
    }
}
