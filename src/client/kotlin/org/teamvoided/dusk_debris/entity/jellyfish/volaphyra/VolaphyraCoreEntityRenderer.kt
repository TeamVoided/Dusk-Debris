package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel

class VolaphyraCoreEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<AbstractVolaphyraEntity, VolaphyraCoreModel>(
        context,
        VolaphyraCoreModel(context.getPart(DuskEntityModelLayers.VOLAPHYRA_CORE)),
        0.25f
    ) {

    override fun getTexture(entity: AbstractVolaphyraEntity): Identifier = VolaphyraEntityRenderer.TEXTURE_CORE
}