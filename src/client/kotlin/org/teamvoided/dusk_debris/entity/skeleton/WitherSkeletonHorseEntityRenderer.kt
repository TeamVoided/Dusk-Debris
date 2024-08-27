package org.teamvoided.dusk_debris.entity.skeleton

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.HorseEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.WitherSkeletonHorseEntity

class WitherSkeletonHorseEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<WitherSkeletonHorseEntity, HorseEntityModel<WitherSkeletonHorseEntity>>(
        context,
        HorseEntityModel(context.getPart(DuskEntityModelLayers.WITHER_SKELETON_HORSE)),
        1.0f
    ) {

    override fun scale(witherSkeletonEntity: WitherSkeletonHorseEntity, matrices: MatrixStack, f: Float) {
        matrices.scale(1.2f, 1.2f, 1.2f)
    }

    override fun getTexture(wolfEntity: WitherSkeletonHorseEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = DuskDebris.id("textures/entity/horse/horse_wither_skeleton.png")
    }
}