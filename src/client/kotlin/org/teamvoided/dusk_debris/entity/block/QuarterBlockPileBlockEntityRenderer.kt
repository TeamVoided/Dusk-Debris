package org.teamvoided.dusks_and_dungeons.entity.block

import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Axis
import org.teamvoided.dusks_and_dungeons.block.entity.QuarterBlockPileBlockEntity

class QuarterBlockPileBlockEntityRenderer(
    ctx: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<QuarterBlockPileBlockEntity> {

    private val blockRenderer: BlockRenderManager = ctx.renderManager

    override fun render(
        blockEntity: QuarterBlockPileBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val direction = blockEntity.cachedState.get(HorizontalFacingBlock.FACING)

        matrices.push()
        matrices.translate(0.5, 0.0, 0.5)
        matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(direction.asRotation()))
        matrices.translate(-0.5, 0.0, -0.5)

        blockEntity.blocks.forEachIndexed { index, block ->
            matrices.push()
            if (index == 2) {
                matrices.translate(0.0, 0.5, 0.25)
            }

            var state = block.defaultState
            if (state.contains(Properties.FACING)) {
                state = state.with(Properties.FACING, direction)
            }

            matrices.translate(0.0, 0.0, 0.25 * if (index % 2 == 0) -1 else 1)
            blockRenderer.renderBlockAsEntity(state, matrices, vertexConsumers, light, overlay)
            matrices.pop()
        }
        matrices.pop()
    }
}
