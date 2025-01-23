package org.teamvoided.dusk_debris.entity.sniffer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.SnifferEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.data.variants.DuskSnifferVariants
import org.teamvoided.dusk_debris.util.variant

@Environment(EnvType.CLIENT)
class SnifferOverlayFeatureRenderer<T : SnifferEntity, M : SnifferEntityModel<T>>(featureRendererContext: FeatureRendererContext<T, M>) :
    FeatureRenderer<T, M>(featureRendererContext) {

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int, entity: T,
        limbAngle: Float, limbDistance: Float,
        tickDelta: Float, animationProgress: Float,
        headYaw: Float, headPitch: Float
    ) {
        val variant = entity.variant.value()
        if (entity.variant.key.get() == DuskSnifferVariants.DEFAULT) return
        if (variant.overlayTextureFull == null) return

        val vertexConsumer = vertexConsumers.getBuffer(texture(variant.overlayTextureFull!!))
        val color: Int = variant.color ?: variant.biomeColor?.value()?.foliageColor ?: -1
        this.contextModel.method_2828(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, color)

    }

    companion object {
        private fun texture(texture: Identifier): RenderLayer = RenderLayer.getEntityCutoutNoCull(texture)
    }
}