package org.teamvoided.dusk_debris.entity.jellyfish.tiny

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.render.TinyEnemyJellyfishMembraneFeatureRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.render.VolaphyraMembraneFeatureRenderer

class TinyEnemyJellyfishEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>(
        context,
        TinyEnemyJellyfishCoreModel(context.getPart(DuskEntityModelLayers.TINY_ENEMY_JELLYFISH)),
        0.125f
    ) {

    init {
        this.addFeature(TinyEnemyJellyfishMembraneFeatureRenderer(this, context.modelLoader))
    }

    override fun getTexture(entity: TinyEnemyJellyfishEntity): Identifier = TEXTURE_CORE

    companion object {
        val TEXTURE_CORE: Identifier = DuskDebris.id("textures/entity/jellyfish/tiny_enemy_jellyfish_core.png")
        val TEXTURE_MESOGLEA: Identifier = DuskDebris.id("textures/entity/jellyfish/tiny_enemy_jellyfish.png")
    }
}