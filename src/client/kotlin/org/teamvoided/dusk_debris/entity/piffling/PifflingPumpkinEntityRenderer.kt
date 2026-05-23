package org.teamvoided.dusk_debris.entity.piffling

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeadFeatureRenderer
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeldItemFeatureRenderer

class PifflingPumpkinEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<PifflingPumpkinEntity, PifflingPumpkinModel>(
        context,
        PifflingPumpkinModel(context.bakeLayer(DuskEntityModelLayers.PIFFLING_PUMPKIN)),
        0.35f
    ) {
    init {
        this.addLayer(PifflingPumpkinHeldItemFeatureRenderer(this, context.itemInHandRenderer))
        this.addLayer(PifflingPumpkinHeadFeatureRenderer(this, context.itemRenderer))
    }

    override fun getTextureLocation(tuffGolemEntity: PifflingPumpkinEntity): ResourceLocation {
        return TEXTURE
    }

    override fun isShaking(entity: PifflingPumpkinEntity): Boolean {
        return super.isShaking(entity)
    }

    companion object {
        private val TEXTURE: ResourceLocation = id("textures/entity/pumpkin/piffling_pumpkin.png")
    }
}