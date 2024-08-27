package org.teamvoided.dusk_debris.entity.skeleton

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.WitherSkeletonWolfEntity
import org.teamvoided.dusk_debris.entity.skeleton.render.SkeletonWolfEntityModel

class WitherSkeletonWolfEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<WitherSkeletonWolfEntity, SkeletonWolfEntityModel<WitherSkeletonWolfEntity>>(
        context,
        SkeletonWolfEntityModel(context.getPart(DuskEntityModelLayers.SKELETON_WOLF)),
        0.55f
    ) {

    override fun getAnimationProgress(wolfEntity: WitherSkeletonWolfEntity, f: Float): Float {
        return wolfEntity.getTailAngle()
    }


    override fun scale(witherSkeletonEntity: WitherSkeletonWolfEntity, matrices: MatrixStack, f: Float) {
        matrices.scale(1.2f, 1.2f, 1.2f)
    }
    override fun getTexture(wolfEntity: WitherSkeletonWolfEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = DuskDebris.id("textures/entity/skeleton/wither_wolf.png")
    }
}