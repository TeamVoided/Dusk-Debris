package org.teamvoided.dusk_debris.entity.skeleton

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.SkeletonEntityRenderer
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer
import org.teamvoided.dusk_debris.entity.GloomEntity
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.skeleton.render.GloomEntityModel
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers

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
    }
//    override fun render(
//        gloomEntity: GloomEntity,
//        f: Float,
//        g: Float,
//        matrices: MatrixStack,
//        vertexConsumers: VertexConsumerProvider,
//        i: Int
//    ) {
//        super.render(gloomEntity, f, g, matrices, vertexConsumers, i)
//    }

    override fun getTexture(gloomEntity: GloomEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = id("textures/entity/skeleton/gloomed.png")
        private val OVERLAY_TEXTURE: Identifier = id("textures/entity/skeleton/gloomed_overlay.png")
    }
}