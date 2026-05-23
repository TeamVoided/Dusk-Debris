package org.teamvoided.dusks_and_dungeons.entity.block

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.teamvoided.dusks_and_dungeons.block.entity.QuarterBlockPileBlockEntity

class QuarterBlockPileBlockEntityRenderer(
    ctx: BlockEntityRendererProvider.Context,
) : BlockEntityRenderer<QuarterBlockPileBlockEntity> {

    private val blockRenderer: BlockRenderDispatcher = ctx.blockRenderDispatcher

    override fun render(
        blockEntity: QuarterBlockPileBlockEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        val direction = blockEntity.blockState.getValue(HorizontalDirectionalBlock.FACING)

        matrices.pushPose()
        matrices.translate(0.5, 0.0, 0.5)
        matrices.mulPose(Axis.YP.rotationDegrees(direction.toYRot()))
        matrices.translate(-0.5, 0.0, -0.5)

        blockEntity.blocks.forEachIndexed { index, block ->
            matrices.pushPose()
            if (index == 2) {
                matrices.translate(0.0, 0.5, 0.25)
            }

            var state = block.defaultBlockState()
            if (state.hasProperty(BlockStateProperties.FACING)) {
                state = state.setValue(BlockStateProperties.FACING, direction)
            }

            matrices.translate(0.0, 0.0, 0.25 * if (index % 2 == 0) -1 else 1)
            blockRenderer.renderSingleBlock(state, matrices, vertexConsumers, light, overlay)
            matrices.popPose()
        }
        matrices.popPose()
    }
}
