package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.render.VolaphyraMembraneFeatureRenderer

class VolaphyraEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<AbstractVolaphyraEntity, VolaphyraCoreModel>(
        context,
        VolaphyraCoreModel(context.bakeLayer(DuskEntityModelLayers.VOLAPHYRA), 2f),
        0.45f
    ) {

    init {
        this.addLayer(VolaphyraMembraneFeatureRenderer(this, context.modelSet))
    }

    override fun render(
        entity: AbstractVolaphyraEntity,
        f: Float,
        g: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int
    ) {
        if (entity.isAlive)
            super.render(entity, f, g, matrices, vertexConsumers, i)
    }

    override fun getTextureLocation(entity: AbstractVolaphyraEntity): ResourceLocation = VOLAPHYRA_CORE

    companion object {
        val VOLAPHYRA_CORE: ResourceLocation = DuskDebris.id("textures/entity/jellyfish/volaphyra_core.png")
        val VOLAPHYRA_MESOGLEA: ResourceLocation = DuskDebris.id("textures/entity/jellyfish/volaphyra_mesoglea.png")
    }
}