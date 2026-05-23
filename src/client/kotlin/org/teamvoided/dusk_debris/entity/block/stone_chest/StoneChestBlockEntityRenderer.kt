package org.teamvoided.dusk_debris.entity.block.stone_chest

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.ChestType
import org.joml.Vector2f
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity.Companion.shouldRenderLid
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.max

class StoneChestBlockEntityRenderer<T>(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<T> where T : BlockEntity {
    private val blockRenderManager: BlockRenderDispatcher = ctx.blockRenderDispatcher

    override fun render(
        blockEntity: T,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        lightI: Int,
        overlay: Int
    ) {
        val world = blockEntity.level
        if (world != null && blockEntity is DuskChestBlockEntity) {
            val blockState = blockEntity.blockState
            val phase = blockState.getValue(DuskProperties.CHEST_PHASE)
            if (blockEntity.shouldRenderLid() ) {
                val animProg = animProg(blockEntity, phase.ordinal, tickDelta)

                matrices.pushPose()
                matrices.animateLid(blockState, animProg)
                renderLid(matrices, vertexConsumers, lightI, true, blockState, overlay)
                matrices.popPose()
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

    fun PoseStack.animateLid(state: BlockState, progress: Float) {
        val one = 0.0625f
        val fifteen = 1f - one //0.9375f
        val type = state.getValue(BlockStateProperties.CHEST_TYPE)
        val isDouble = if (type != ChestType.SINGLE) Utils.rotate45 / 2 else Utils.rotate45
        val isRight = if (type == ChestType.LEFT) 1f else 0f
        val point = when (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction.NORTH -> Vector2f(one - isRight, fifteen)
            Direction.SOUTH -> Vector2f(fifteen + isRight, one)
            Direction.WEST -> Vector2f(fifteen, fifteen + isRight)
            Direction.EAST -> Vector2f(one, one - isRight)
            else -> Vector2f(0f, 0f)
        }
        this.rotateAround(Axis.YN.rotation(isDouble * progress), point.x, 0.625f, point.y)
    }


    private fun renderLid(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        renderAsModel: Boolean,
        state: BlockState,
        overlay: Int
    ) {
        if (renderAsModel) {
            val lid = this.blockRenderManager.getBlockModel(state.setValue(DuskProperties.LID, true))
            if (lid != null) {
                this.blockRenderManager.modelRenderer.renderModel(
                    matrices.last(),
                    vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)),
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
            this.blockRenderManager.renderSingleBlock(state, matrices, vertexConsumers, light, overlay)
        }
    }
}