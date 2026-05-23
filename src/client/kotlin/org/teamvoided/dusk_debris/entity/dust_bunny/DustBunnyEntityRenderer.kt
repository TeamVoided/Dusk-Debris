package org.teamvoided.dusk_debris.entity.dust_bunny

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.DustBunnyEntity
import org.teamvoided.dusk_debris.entity.dust_bunny.render.DustBunnyEntityModel

class DustBunnyEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<DustBunnyEntity, DustBunnyEntityModel>(
        context,
        DustBunnyEntityModel(context.bakeLayer(DuskEntityModelLayers.DUST_BUNNY)),
        0f
    ) {
    init {
        this.addLayer(ItemInHandLayer(this, context.itemInHandRenderer))
    }

    override fun getTextureLocation(entity: DustBunnyEntity): ResourceLocation = TextureAtlas.LOCATION_BLOCKS
}