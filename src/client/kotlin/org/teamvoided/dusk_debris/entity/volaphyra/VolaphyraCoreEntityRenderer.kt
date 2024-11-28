package org.teamvoided.dusk_debris.entity.volaphyra

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.VolaphyraEntity
import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraEntityModel
import org.teamvoided.dusk_debris.entity.volaphyra.render.VolaphyraCoreFeatureRenderer

class VolaphyraCoreEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<AbstractVolaphyraEntity, VolaphyraCoreModel>(
        context,
        VolaphyraCoreModel(context.getPart(DuskEntityModelLayers.VOLAPHYRA_CORE)),
        0.25f
    ) {

    override fun getTexture(entity: AbstractVolaphyraEntity): Identifier = TEXTURE

    companion object {
        val TEXTURE: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra_core.png")
    }
}