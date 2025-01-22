//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.sniffer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

@Environment(EnvType.CLIENT)
abstract class EyesFeatureRenderer<T : Entity, M : EntityModel<T>>(featureRendererContext: FeatureRendererContext<T, M>) :
    FeatureRenderer<T, M>(featureRendererContext) {
    override fun render(
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider,
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
        this.getContextModel().method_60879(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV)
    }

    abstract val eyesLayer: RenderLayer?
}
