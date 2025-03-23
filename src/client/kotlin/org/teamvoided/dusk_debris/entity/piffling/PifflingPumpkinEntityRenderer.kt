package org.teamvoided.dusk_debris.entity.piffling

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeadFeatureRenderer
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeldItemFeatureRenderer

class PifflingPumpkinEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<PifflingPumpkinEntity, PifflingPumpkinModel>(
        context,
        PifflingPumpkinModel(context.getPart(DuskEntityModelLayers.PIFFLING_PUMPKIN)),
        0.35f
    ) {
    init {
        this.addFeature(PifflingPumpkinHeldItemFeatureRenderer(this, context.heldItemRenderer))
        this.addFeature(PifflingPumpkinHeadFeatureRenderer(this, context.itemRenderer))
    }

    override fun getTexture(tuffGolemEntity: PifflingPumpkinEntity): Identifier {
        return TEXTURE
    }

    override fun isShaking(entity: PifflingPumpkinEntity): Boolean {
        return super.isShaking(entity)
    }

    companion object {
        private val TEXTURE: Identifier = id("textures/entity/pumpkin/piffling_pumpkin.png")
    }
}