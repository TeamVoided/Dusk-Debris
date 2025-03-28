package org.teamvoided.dusk_debris.entity.skeleton.gloom

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.SkeletonEntityRenderer
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer
import net.minecraft.client.util.ColorUtil
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.entity.skeleton.gloom.model.GloomEntityModel
import org.teamvoided.dusk_debris.entity.skeleton.gloom.render.GloomEyesFeatureRenderer
import org.teamvoided.dusk_debris.entity.skeleton.gloom.render.GloomOverlayFeatureRenderer
import org.teamvoided.dusk_debris.util.sendMessageIngame

@Environment(EnvType.CLIENT)
class GloomEntityRenderer(context: EntityRendererFactory.Context) : SkeletonEntityRenderer<GloomEntity>(
    context,
    DuskEntityModelLayers.GLOOM_INNER_ARMOR,
    DuskEntityModelLayers.GLOOM_OUTER_ARMOR,
    GloomEntityModel(context.getPart(DuskEntityModelLayers.GLOOM))
) {
    init {
        this.addFeature(
            SkeletonOverlayFeatureRenderer(
                this,
                context.modelLoader,
                DuskEntityModelLayers.GLOOM_OUTER,
                OVERLAY_TEXTURE
            )
        )
        this.addFeature(GloomEyesFeatureRenderer(this))
    }


    override fun getTexture(gloomEntity: GloomEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = id("textures/entity/skeleton/gloomed.png")
        private val OVERLAY_TEXTURE: Identifier = id("textures/entity/skeleton/gloomed_overlay.png")

        @JvmStatic
        fun gloomTranslucency(
            gloomEntity: GloomEntity,
            vertexConsumer: VertexConsumerProvider,
            renderLayer: RenderLayer,
            tickDelta: Float
        ): VertexConsumer? {
            sendMessageIngame("it is working")
            return vertexConsumer.getBuffer(renderLayer)


//            val transitionTime: Int = gloomEntity.darkModeTransitionTime
//            if (transitionTime >= 60) {
//                return vertexConsumer.getBuffer(
//                    RenderLayer.getBeaconBeam(
//                        TEXTURE,
//                        true
//                    )
//                ).color(ColorUtil.Argb32.of(256, -1))
//            } else if (transitionTime >= 0) {
//                val tock = if (gloomEntity.isLightMode()) {
//                    tickDelta
//                } else {
//                    -tickDelta
//                }
//                val ratio = (((transitionTime + tock) / 60) * 256).toInt()
//                return vertexConsumer.getBuffer(
//                    RenderLayer.getBeaconBeam(
//                        TEXTURE,
//                        true
//                    )
//                ).color(ColorUtil.Argb32.of(ratio, -1))
//            }
//            return null
        }
    }
}