package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

@Environment(EnvType.CLIENT)
class TuffGolemEyesFeatureRenderer(featureRendererContext: FeatureRendererContext<TuffGolemEntity, TuffGolemEntityModel>) :
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
        if (!tuffGolemEntity.isStatue())
            super.render(
                matrices,
                vertexConsumers,
                light,
                tuffGolemEntity,
                limbAngle,
                limbDistance,
                tickDelta,
                animationProgress,
                headYaw,
                headPitch
            )
    }

    override fun getEyesLayer(): RenderLayer {
        return RenderLayer.getEyes(getEyesTexture("default"))
    }

    companion object {
        fun getEyesTexture(string: String): Identifier =
            id("textures/entity/tuff_golem/eyes/$string.png")
    }
}