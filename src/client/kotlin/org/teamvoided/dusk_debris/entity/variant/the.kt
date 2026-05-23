//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.variant

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.Entity

@Environment(EnvType.CLIENT)
abstract class EyesFeatureRenderer<T : Entity, M : EntityModel<T>>(featureRendererContext: RenderLayerParent<T, M>) :
    RenderLayer<T, M>(featureRendererContext) {
    override fun render(
        matrices: PoseStack?,
        vertexConsumers: MultiBufferSource,
        light: Int,
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val vertexConsumer = vertexConsumers.getBuffer(this.eyesLayer)
        this.parentModel.renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY)
    }

    abstract val eyesLayer: RenderType?
}
