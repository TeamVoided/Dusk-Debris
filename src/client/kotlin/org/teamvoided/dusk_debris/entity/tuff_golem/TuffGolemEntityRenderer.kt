package org.teamvoided.dusk_debris.entity.tuff_golem

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemCloakFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemEyesFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemHatFeatureRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemHeldItemFeatureRenderer
import kotlin.math.abs

class TuffGolemEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<TuffGolemEntity, TuffGolemEntityModel>(
        context,
        TuffGolemEntityModel(context.getPart(DuskEntityModelLayers.TUFF_GOLEM)),
        0.45f //shadowRadius
    ) {
    init {
        this.addFeature(TuffGolemEyesFeatureRenderer(this))
        this.addFeature(TuffGolemHeldItemFeatureRenderer(this, context.heldItemRenderer))
        this.addFeature(TuffGolemCloakFeatureRenderer(this, context.modelLoader))
        this.addFeature(TuffGolemHatFeatureRenderer(this, context.itemRenderer))
    }

    override fun getTexture(tuffGolemEntity: TuffGolemEntity): Identifier = TEXTURE

    override fun isShaking(tuffGolemEntity: TuffGolemEntity): Boolean {
        return super.isShaking(tuffGolemEntity) ||
                (tuffGolemEntity.state == tuffGolemEntity.statueState && tuffGolemEntity.statueTicks < 20)
    }

    companion object {
        private val TEXTURE: Identifier = id("textures/entity/tuff_golem/tuff_golem.png")
    }
}