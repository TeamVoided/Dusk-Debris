package org.teamvoided.dusk_debris.entity.variant

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.SnifferModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.animal.sniffer.Sniffer
import org.teamvoided.dusk_debris.data.variants.DuskSnifferVariants
import org.teamvoided.dusk_debris.util.variant

@Environment(EnvType.CLIENT)
class SnifferOverlayFeatureRenderer<T : Sniffer, M : SnifferModel<T>>(featureRendererContext: RenderLayerParent<T, M>) :
    RenderLayer<T, M>(featureRendererContext) {

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int, entity: T,
        limbAngle: Float, limbDistance: Float,
        tickDelta: Float, animationProgress: Float,
        headYaw: Float, headPitch: Float
    ) {
        val variant = entity.variant.value()
        if (entity.variant.unwrapKey().get() == DuskSnifferVariants.DEFAULT) return
        if (variant.overlayTextureFull == null) return

        val vertexConsumer = vertexConsumers.getBuffer(texture(variant.overlayTextureFull!!))
        val color: Int = variant.color ?: variant.biomeColor?.value()?.foliageColor ?: -1
        this.parentModel.renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, color)

    }

    companion object {
        private fun texture(texture: ResourceLocation): RenderType = RenderType.entityCutoutNoCull(texture)
    }
}