package org.teamvoided.dusk_debris.entity.jellyfish.tiny

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.render.TinyEnemyJellyfishMembraneFeatureRenderer

class TinyEnemyJellyfishEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>(
        context,
        TinyEnemyJellyfishCoreModel(context.bakeLayer(DuskEntityModelLayers.TINY_ENEMY_JELLYFISH)),
        0.125f
    ) {

    init {
        this.addLayer(TinyEnemyJellyfishMembraneFeatureRenderer(this, context.modelSet))
    }

    override fun getTextureLocation(entity: TinyEnemyJellyfishEntity): ResourceLocation = TEXTURE_CORE

    companion object {
        val TEXTURE_CORE: ResourceLocation = DuskDebris.id("textures/entity/jellyfish/tiny_enemy_jellyfish_core.png")
        val TEXTURE_MESOGLEA: ResourceLocation = DuskDebris.id("textures/entity/jellyfish/tiny_enemy_jellyfish.png")
    }
}