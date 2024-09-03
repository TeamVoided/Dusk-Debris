package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

@Environment(EnvType.CLIENT)
open class TuffGolemEyesFeatureRenderer(featureRendererContext: FeatureRendererContext<TuffGolemEntity, TuffGolemEntityModel>) :
    EyesFeatureRenderer<TuffGolemEntity, TuffGolemEntityModel>(featureRendererContext) {

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (tuffGolemEntity.state?.isStatueMode() == false) {
            val vertexConsumer = vertexConsumers.getBuffer(this.getEyesLayer(tuffGolemEntity))
            this.contextModel.method_60879(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV)
        }
    }

    override fun getEyesLayer(): RenderLayer? {
        return null
    }

    open fun getEyesLayer(tuffGolemEntity: TuffGolemEntity): RenderLayer {
        return RenderLayer.getEyes(
            id("textures/entity/tuff_golem/eyes/" + tuffGolemEntity.getEyeBlock() + ".png")
        )
    }
}