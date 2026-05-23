package org.teamvoided.dusk_debris.entity.skeleton.wolf

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.WitherSkeletonWolfEntity
import org.teamvoided.dusk_debris.entity.skeleton.wolf.render.SkeletonWolfEntityModel

class WitherSkeletonWolfEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<WitherSkeletonWolfEntity, SkeletonWolfEntityModel<WitherSkeletonWolfEntity>>(
        context,
        SkeletonWolfEntityModel(context.bakeLayer(DuskEntityModelLayers.SKELETON_WOLF)),
        0.55f
    ) {

    override fun getBob(wolfEntity: WitherSkeletonWolfEntity, f: Float): Float {
        return wolfEntity.getTailAngle()
    }


    override fun scale(witherSkeletonEntity: WitherSkeletonWolfEntity, matrices: PoseStack, f: Float) {
        matrices.scale(1.2f, 1.2f, 1.2f)
    }
    override fun getTextureLocation(wolfEntity: WitherSkeletonWolfEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: ResourceLocation = DuskDebris.id("textures/entity/skeleton/wither_wolf.png")
    }
}