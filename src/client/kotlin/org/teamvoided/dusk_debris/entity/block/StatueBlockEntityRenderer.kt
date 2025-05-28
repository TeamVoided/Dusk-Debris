package org.teamvoided.dusk_debris.entity.block

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity

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
        val world = blockEntity.world
        matrices.push()
        matrices.translate(0.5, 1.0, 0.5)
        val entity = blockEntity.entityType.create(world)
        entity?.age = 0

        entityRenderer.render(entity, 0.0, 0.0, 0.0, 0f, 0f, matrices, vertexConsumers, 255)
        matrices.pop()
    }
}
