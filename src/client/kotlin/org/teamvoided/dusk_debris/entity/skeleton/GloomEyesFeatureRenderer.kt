package org.teamvoided.dusk_debris.entity.skeleton

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.SkeletonEntityModel
import net.minecraft.entity.mob.AbstractSkeletonEntity
import org.teamvoided.dusk_debris.DuskDebris.id

@Environment(EnvType.CLIENT)
class GloomEyesFeatureRenderer<T : AbstractSkeletonEntity, M : SkeletonEntityModel<T>>(featureRendererContext: FeatureRendererContext<T, M>) :
    EyesFeatureRenderer<T, M>(featureRendererContext) {
    override fun getEyesLayer(): RenderLayer {
        return SKIN
    }

    companion object {
        private val SKIN: RenderLayer = RenderLayer.getEyes(id("textures/entity/skeleton/gloomed_eyes.png"))
    }
}