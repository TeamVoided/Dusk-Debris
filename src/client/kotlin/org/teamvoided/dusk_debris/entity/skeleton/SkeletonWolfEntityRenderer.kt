package org.teamvoided.dusk_debris.entity.skeleton

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.SkeletonWolfEntity
import org.teamvoided.dusk_debris.entity.skeleton.render.SkeletonWolfEntityModel

class SkeletonWolfEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<SkeletonWolfEntity, SkeletonWolfEntityModel<SkeletonWolfEntity>>(
        context,
        SkeletonWolfEntityModel(context.getPart(DuskEntityModelLayers.SKELETON_WOLF)),
        0.5f
    ) {

    override fun getAnimationProgress(wolfEntity: SkeletonWolfEntity, f: Float): Float {
        return wolfEntity.getTailAngle()
    }

    override fun getTexture(wolfEntity: SkeletonWolfEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = DuskDebris.id("textures/entity/skeleton/wolf.png")
    }
}