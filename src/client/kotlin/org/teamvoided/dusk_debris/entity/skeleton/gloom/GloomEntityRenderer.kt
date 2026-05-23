package org.teamvoided.dusk_debris.entity.skeleton.gloom

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.SkeletonRenderer
import net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.entity.skeleton.gloom.model.GloomEntityModel
import org.teamvoided.dusk_debris.entity.skeleton.gloom.render.GloomEyesFeatureRenderer

@Environment(EnvType.CLIENT)
class GloomEntityRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<GloomEntity>(
    context,
    DuskEntityModelLayers.GLOOM_INNER_ARMOR,
    DuskEntityModelLayers.GLOOM_OUTER_ARMOR,
    GloomEntityModel(context.bakeLayer(DuskEntityModelLayers.GLOOM))
) {
    init {
        this.addLayer(
            SkeletonClothingLayer(
                this,
                context.modelSet,
                DuskEntityModelLayers.GLOOM_OUTER,
                OVERLAY_TEXTURE
            )
        )
        this.addLayer(GloomEyesFeatureRenderer(this))
    }


    override fun getTextureLocation(gloomEntity: GloomEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: ResourceLocation = id("textures/entity/skeleton/gloomed.png")
        private val OVERLAY_TEXTURE: ResourceLocation = id("textures/entity/skeleton/gloomed_overlay.png")
    }
}