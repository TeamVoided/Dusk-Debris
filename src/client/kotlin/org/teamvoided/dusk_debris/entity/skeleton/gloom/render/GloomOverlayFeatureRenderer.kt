package org.teamvoided.dusk_debris.entity.skeleton.gloom.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer
import net.minecraft.client.render.entity.model.*
import net.minecraft.client.util.ColorUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.entity.GloomEntity

class GloomOverlayFeatureRenderer<M : EntityModel<GloomEntity>>(
    context: FeatureRendererContext<GloomEntity, M>,
    private val modelLoader: EntityModelLoader,
    private val layer: EntityModelLayer,
    private val textureGloomOverlay: Identifier
) : SkeletonOverlayFeatureRenderer<GloomEntity, M>(context, modelLoader, layer, textureGloomOverlay) {

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        entity: GloomEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        if (!entity.isLightMode() && !entity.isInvisible) {
            val vc = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureGloomOverlay, true))
                .color(ColorUtil.Argb32.of(entity.age % 256, -1))
            val model = SkeletonEntityModel<GloomEntity>(modelLoader.getModelPart(layer))
            (this.contextModel).copyStateTo(model)
            model.animateModel(entity, f, g, h)
            model.setAngles(entity, f, g, j, k, l)
            model.method_60879(matrices, vc, i, LivingEntityRenderer.getOverlay(entity, 0.0f))
        } else
            super.render(matrices, vertexConsumers, i, entity, f, g, h, j, k, l)
    }
}