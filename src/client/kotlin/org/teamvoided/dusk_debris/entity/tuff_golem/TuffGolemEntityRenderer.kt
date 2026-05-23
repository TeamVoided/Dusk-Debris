package org.teamvoided.dusk_debris.entity.tuff_golem

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemCloakFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemEyesFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemHatFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemHeldItemFeatureRenderer

class TuffGolemEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<TuffGolemEntity, TuffGolemEntityModel>(
        context,
        TuffGolemEntityModel(context.bakeLayer(DuskEntityModelLayers.TUFF_GOLEM)),
        0.45f //shadowRadius
    ) {
    init {
        this.addLayer(TuffGolemEyesFeatureRenderer(this))
        this.addLayer(TuffGolemHeldItemFeatureRenderer(this, context.itemInHandRenderer))
        this.addLayer(TuffGolemCloakFeatureRenderer(this, context.modelSet))
        this.addLayer(TuffGolemHatFeatureRenderer(this, context.itemRenderer))
    }

    override fun getTextureLocation(tuffGolemEntity: TuffGolemEntity): ResourceLocation = TEXTURE

    override fun isShaking(tuffGolemEntity: TuffGolemEntity): Boolean {
        return super.isShaking(tuffGolemEntity) ||
                (tuffGolemEntity.state == tuffGolemEntity.statueState && tuffGolemEntity.statueTicks < 20)
    }

    companion object {
        private val TEXTURE: ResourceLocation = id("textures/entity/tuff_golem/tuff_golem.png")
    }
}