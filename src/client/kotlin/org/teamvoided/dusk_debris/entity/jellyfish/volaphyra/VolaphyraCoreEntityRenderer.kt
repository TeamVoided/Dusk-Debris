package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel

class VolaphyraCoreEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<AbstractVolaphyraEntity, VolaphyraCoreModel>(
        context,
        VolaphyraCoreModel(context.bakeLayer(DuskEntityModelLayers.VOLAPHYRA_CORE)),
        0.25f
    ) {
    override fun getTextureLocation(entity: AbstractVolaphyraEntity): ResourceLocation = VolaphyraEntityRenderer.VOLAPHYRA_CORE
}