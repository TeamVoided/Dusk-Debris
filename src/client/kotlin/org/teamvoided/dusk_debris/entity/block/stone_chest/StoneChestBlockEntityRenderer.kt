package org.teamvoided.dusk_debris.entity.block.stone_chest

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.block.ChestAnimationProgress
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Axis
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.StoneChestBlock
import org.teamvoided.dusk_debris.block.entity.StoneChestBlockEntity
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.util.Utils

class StoneChestBlockEntityRenderer<T>(ctx: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<T> where T : BlockEntity, T : ChestAnimationProgress {

    private val blockRenderManager: BlockRenderManager = ctx.renderManager

    override fun render(
        blockEntity: T,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        lightI: Int,
        overlay: Int
    ) {
        val world = blockEntity.world
        if (world != null && blockEntity is StoneChestBlockEntity) {
            val blockState = blockEntity.cachedState
            val phase = blockState.get(DuskProperties.CHEST_PHASE)
            if (phase.ordinal != 0) {
                val animProg = animProg(blockEntity, phase.ordinal, tickDelta)

                matrices.push()
                matrices.rotateAround(Axis.X_NEGATIVE.rotation(Utils.rotate45 * animProg), 0f, 0.625f, 0f)
                renderLid(matrices, vertexConsumers, lightI, true, blockState, overlay)
                matrices.pop()
            }
        }
    }

    fun animProg(blockEntity: StoneChestBlockEntity, state: Int, tickDelta: Float): Float {
        val tockDelta = if (state == 2) tickDelta else -tickDelta
        var animProg = ((blockEntity.lidOpeningTicks + tockDelta) / StoneChestBlockEntity.MAX_OPENING_TICKS)
        animProg = 1.0f - animProg
        animProg = 1.0f - animProg * animProg * animProg
        return animProg
    }

    private fun renderLid(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        renderAsModel: Boolean,
        state: BlockState,
        overlay: Int
    ) {
        if (renderAsModel) {
            val lid = this.blockRenderManager.getModel(state.with(DuskProperties.LID, true))
            if (lid != null) {
                this.blockRenderManager.modelRenderer.render(
                    matrices.peek(),
                    vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)),
                    state,
                    lid,
                    0f,
                    0f,
                    0f,
                    light,
                    overlay
                )
            } else {
                val block = state.block.name
                log.info("lid is null in $block")
                log.info("$state")
                log.info("$this")
            }
        } else {
            this.blockRenderManager.renderBlockAsEntity(state, matrices, vertexConsumers, light, overlay)
        }
    }
}