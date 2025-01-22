package org.teamvoided.dusk_debris.entity.sniffer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.SkeletonEntityModel
import net.minecraft.client.render.entity.model.SnifferEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.passive.SnifferEntity
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.util.variant

@Environment(EnvType.CLIENT)
class SnifferOverlayFeatureRenderer<T : SnifferEntity, M : SnifferEntityModel<T>>(featureRendererContext: FeatureRendererContext<T, M>) :
    EyesFeatureRenderer<T, M>(featureRendererContext) {
    override fun getEyesLayer(): RenderLayer {
        return SKIN
    }

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
        val color = entity.variant.value().color ?: -1
        this.contextModel.method_2828(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, color)
    }

    companion object {
        private val SKIN: RenderLayer =
            RenderLayer.getEntityCutoutNoCull(id("textures/entity/sniffer/overlay/default.png"))
    }
}