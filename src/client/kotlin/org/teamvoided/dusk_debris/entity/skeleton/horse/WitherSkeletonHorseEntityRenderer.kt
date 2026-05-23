package org.teamvoided.dusk_debris.entity.skeleton.horse

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.HorseModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.WitherSkeletonHorseEntity

class WitherSkeletonHorseEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<WitherSkeletonHorseEntity, HorseModel<WitherSkeletonHorseEntity>>(
        context,
        HorseModel(context.bakeLayer(DuskEntityModelLayers.WITHER_SKELETON_HORSE)),
        1.0f
    ) {

    override fun scale(witherSkeletonEntity: WitherSkeletonHorseEntity, matrices: PoseStack, f: Float) {
        matrices.scale(1.2f, 1.2f, 1.2f)
    }

    override fun getTextureLocation(wolfEntity: WitherSkeletonHorseEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: ResourceLocation = DuskDebris.id("textures/entity/horse/horse_wither_skeleton.png")
    }
}