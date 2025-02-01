package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.render.VolaphyraMembraneFeatureRenderer

class VolaphyraEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<AbstractVolaphyraEntity, VolaphyraCoreModel>(
        context,
        VolaphyraCoreModel(context.getPart(DuskEntityModelLayers.VOLAPHYRA), 2f),
        0.45f
    ) {

    init {
        this.addFeature(VolaphyraMembraneFeatureRenderer(this, context.modelLoader))
    }

    override fun render(
        entity: AbstractVolaphyraEntity,
        f: Float,
        g: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int
    ) {
        if (entity.isAlive)
            super.render(entity, f, g, matrices, vertexConsumers, i)
    }

    override fun getTexture(entity: AbstractVolaphyraEntity): Identifier = TEXTURE_CORE

    companion object {
        val TEXTURE_CORE: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra_core.png")
        val TEXTURE_MESOGLEA: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra_mesoglea.png")
    }
}