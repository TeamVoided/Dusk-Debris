package org.teamvoided.dusk_debris.entity.skeleton.gloom.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.SkeletonModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FastColor
import org.teamvoided.dusk_debris.entity.GloomEntity

class GloomOverlayFeatureRenderer<M : EntityModel<GloomEntity>>(
    context: RenderLayerParent<GloomEntity, M>,
    private val modelLoader: EntityModelSet,
    private val layer: ModelLayerLocation,
    private val textureGloomOverlay: ResourceLocation
) : SkeletonClothingLayer<GloomEntity, M>(context, modelLoader, layer, textureGloomOverlay) {

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
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
            val vc = vertexConsumers.getBuffer(RenderType.beaconBeam(textureGloomOverlay, true))
                .setColor(FastColor.ARGB32.color(entity.tickCount % 256, -1))
            val model = SkeletonModel<GloomEntity>(modelLoader.bakeLayer(layer))
            (this.parentModel).copyPropertiesTo(model)
            model.prepareMobModel(entity, f, g, h)
            model.setupAnim(entity, f, g, j, k, l)
            model.renderToBuffer(matrices, vc, i, LivingEntityRenderer.getOverlayCoords(entity, 0.0f))
        } else
            super.render(matrices, vertexConsumers, i, entity, f, g, h, j, k, l)
    }
}