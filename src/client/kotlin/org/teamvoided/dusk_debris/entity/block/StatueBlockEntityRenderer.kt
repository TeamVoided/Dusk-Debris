package org.teamvoided.dusk_debris.entity.block

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.CrashReport
import net.minecraft.CrashReportCategory
import net.minecraft.ReportedException
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.world.entity.Entity
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.mixin.EntityRenderDispatcherAccessor

class StatueBlockEntityRenderer(
    ctx: BlockEntityRendererProvider.Context,
) : BlockEntityRenderer<StatueBlockEntity> {

    private val entitiesRenderer = ctx.entityRenderer

    override fun render(
        blockEntity: StatueBlockEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int,
    ) {
        val world = blockEntity.level ?: return
        val entity = blockEntity.entityType.create(world) ?: return
        renderEntity(0f, matrices, vertexConsumers, 255, entity, entitiesRenderer)
    }

    fun renderEntity(
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        entity: Entity,
        renderDispatcher: EntityRenderDispatcher,
    ) {
        matrices.pushPose()
        matrices.translate(0.5, 1.0, 0.5)
        renderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light)
        val entityRenderer = entitiesRenderer.getRenderer(entity)
        try {
            matrices.pushPose()
            if (entityRenderer is LivingEntityRenderer<*, *>) {
                entityRenderer.render(entity, 0f, tickDelta, matrices, vertexConsumers, light)
            }
            if (entitiesRenderer.shouldRenderHitBoxes() && !Minecraft.getInstance().showOnlyReducedInfo()) {
                EntityRenderDispatcherAccessor.dnd_renderHitbox(
                    matrices, vertexConsumers.getBuffer(RenderType.lines()),
                    entity, tickDelta, 1.0f, 1.0f, 1.0f
                )
            }

            matrices.popPose()
        } catch (var25: Throwable) {
            val crashReport = CrashReport.forThrowable(var25, "Rendering Statue entity")
            val crashReportSection = crashReport.addCategory("Rendering entity being rendered")
            entity.fillCrashReportCategory(crashReportSection)
            val crashReportSection2 = crashReport.addCategory("Renderer details")
            crashReportSection2.setDetail("Assigned renderer", entityRenderer)
            crashReportSection2.setDetail("Location", CrashReportCategory.formatLocation(entity.level(), 0, 0, 0))
            crashReportSection2.setDetail("Rotation", 0f)
            crashReportSection2.setDetail("Delta", tickDelta)
            throw ReportedException(crashReport)
        }

        matrices.popPose()
    }
}
