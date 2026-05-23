package org.teamvoided.dusk_debris.entity.tuff_golem.render

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

@Environment(EnvType.CLIENT)
open class TuffGolemEyesFeatureRenderer(featureRendererContext: RenderLayerParent<TuffGolemEntity, TuffGolemEntityModel>) :
    EyesLayer<TuffGolemEntity, TuffGolemEntityModel>(featureRendererContext) {

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (tuffGolemEntity.state < 2) {
            val vertexConsumer = vertexConsumers.getBuffer(this.getEyesLayer(tuffGolemEntity))
            this.parentModel.renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY)
        }
    }

    override fun renderType(): RenderType? {
        return null
    }

    open fun getEyesLayer(tuffGolemEntity: TuffGolemEntity): RenderType {
        return RenderType.eyes(
            id("textures/entity/tuff_golem/eyes/" + tuffGolemEntity.eyeBlock + ".png")
        )
    }
}