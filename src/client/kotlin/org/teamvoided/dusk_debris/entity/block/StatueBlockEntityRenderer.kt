package org.teamvoided.dusk_debris.entity.block

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import kotlin.math.max

class StatueBlockEntityRenderer(
    ctx: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<StatueBlockEntity> {

    private val entityRenderer = ctx.entityRendererDispatcher

    override fun render(
        blockEntity: StatueBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world = blockEntity.world ?: return
        val entity = blockEntity.entityType.create(world) ?: return
        renderEntity(tickDelta, matrices, vertexConsumers, light, entity, entityRenderer)
    }

    fun renderEntity(
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: Entity,
        renderDispatcher: EntityRenderDispatcher
    ) {
        matrices.push()
        matrices.translate(0.5, 1.0, 0.5)
        renderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light)
        matrices.pop()
    }
}
