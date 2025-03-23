package org.teamvoided.dusk_debris.entity.block.stone_chest

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Direction
import org.joml.Vector2f
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity.Companion.shouldRenderLid
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.max

class StoneChestBlockEntityRenderer<T>(ctx: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<T> where T : BlockEntity {
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
        if (world != null && blockEntity is DuskChestBlockEntity) {
            val blockState = blockEntity.cachedState
            val phase = blockState.get(DuskProperties.CHEST_PHASE)
            if (blockEntity.shouldRenderLid() ) {
                val animProg = animProg(blockEntity, phase.ordinal, tickDelta)

                matrices.push()
                matrices.animateLid(blockState, animProg)
                renderLid(matrices, vertexConsumers, lightI, true, blockState, overlay)
                matrices.pop()
            }
        }
    }

    fun animProg(blockEntity: DuskChestBlockEntity, state: Int, tickDelta: Float): Float {
        val tockDelta = if (state == 2) tickDelta else -tickDelta
        var animProg = (max(blockEntity.lidOpeningTicks + tockDelta, 0f) / DuskChestBlockEntity.MAX_OPENING_TICKS)
        animProg = 1.0f - animProg
        animProg = 1.0f - animProg * animProg * animProg
        return animProg
    }

    fun MatrixStack.animateLid(state: BlockState, progress: Float) {
        val one = 0.0625f
        val fifteen = 1f - one //0.9375f
        val type = state.get(Properties.CHEST_TYPE)
        val isDouble = if (type != ChestType.SINGLE) Utils.rotate45 / 2 else Utils.rotate45
        val isRight = if (type == ChestType.LEFT) 1f else 0f
        val point = when (state.get(Properties.HORIZONTAL_FACING)) {
            Direction.NORTH -> Vector2f(one - isRight, fifteen)
            Direction.SOUTH -> Vector2f(fifteen + isRight, one)
            Direction.WEST -> Vector2f(fifteen, fifteen + isRight)
            Direction.EAST -> Vector2f(one, one - isRight)
            else -> Vector2f(0f, 0f)
        }
        this.rotateAround(Axis.Y_NEGATIVE.rotation(isDouble * progress), point.x, 0.625f, point.y)
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