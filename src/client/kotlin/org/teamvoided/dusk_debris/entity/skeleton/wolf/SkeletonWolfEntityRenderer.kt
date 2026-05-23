package org.teamvoided.dusk_debris.entity.skeleton.wolf

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.SkeletonWolfEntity
import org.teamvoided.dusk_debris.entity.skeleton.wolf.render.SkeletonWolfEntityModel

class SkeletonWolfEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<SkeletonWolfEntity, SkeletonWolfEntityModel<SkeletonWolfEntity>>(
        context,
        SkeletonWolfEntityModel(context.bakeLayer(DuskEntityModelLayers.SKELETON_WOLF)),
        0.5f
    ) {

    override fun getBob(wolfEntity: SkeletonWolfEntity, f: Float): Float {
        return wolfEntity.getTailAngle()
    }

    override fun getTextureLocation(wolfEntity: SkeletonWolfEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: ResourceLocation = DuskDebris.id("textures/entity/skeleton/wolf.png")
    }
}