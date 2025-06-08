package org.teamvoided.dusk_debris.entity.block

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.crash.CrashException
import net.minecraft.util.crash.CrashReport
import net.minecraft.util.crash.CrashReportSection
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.mixin.EntityRenderDispatcherAccessor

class StatueBlockEntityRenderer(
    ctx: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<StatueBlockEntity> {

    private val entitiesRenderer = ctx.entityRendererDispatcher

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
        renderEntity(0f, matrices, vertexConsumers, 255, entity, entitiesRenderer)
    }

    fun renderEntity(
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: Entity,
        renderDispatcher: EntityRenderDispatcher,
    ) {
        matrices.push()
        matrices.translate(0.5, 1.0, 0.5)
        renderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light)
        val entityRenderer = entitiesRenderer.getRenderer(entity)
        try {
            matrices.push()
            if (entityRenderer is LivingEntityRenderer<*, *>) {
                entityRenderer.render(entity, 0f, tickDelta, matrices, vertexConsumers, light)
            }
            if (entitiesRenderer.shouldRenderHitboxes() && !MinecraftClient.getInstance().hasReducedDebugInfo()) {
                EntityRenderDispatcherAccessor.dnd_renderHitbox(
                    matrices, vertexConsumers.getBuffer(RenderLayer.getLines()),
                    entity, tickDelta, 1.0f, 1.0f, 1.0f
                )
            }

            matrices.pop()
        } catch (var25: Throwable) {
            val crashReport = CrashReport.create(var25, "Rendering Statue entity")
            val crashReportSection = crashReport.addElement("Rendering entity being rendered")
            entity.populateCrashReport(crashReportSection)
            val crashReportSection2 = crashReport.addElement("Renderer details")
            crashReportSection2.add("Assigned renderer", entityRenderer)
            crashReportSection2.add("Location", CrashReportSection.createPositionString(entity.world, 0, 0, 0))
            crashReportSection2.add("Rotation", 0f)
            crashReportSection2.add("Delta", tickDelta)
            throw CrashException(crashReport)
        }

        matrices.pop()
    }
}
