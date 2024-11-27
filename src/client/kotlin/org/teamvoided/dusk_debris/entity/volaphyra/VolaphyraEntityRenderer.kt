package org.teamvoided.dusk_debris.entity.volaphyra

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.VolaphyraEntity
import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraEntityModel

class VolaphyraEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<VolaphyraEntity, VolaphyraEntityModel>(
        context,
        VolaphyraEntityModel(context.getPart(DuskEntityModelLayers.VOLAPHYRA)),
        0.5f //shadowRadius
    ) {
    init {

    }

    override fun getTexture(entity: VolaphyraEntity): Identifier = TEXTURE

    companion object {
        private val TEXTURE: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra.png")
    }
}