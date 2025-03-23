package org.teamvoided.dusk_debris.entity.dust_bunny

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.entity.DustBunnyEntity
import org.teamvoided.dusk_debris.entity.dust_bunny.render.DustBunnyEntityModel

class DustBunnyEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<DustBunnyEntity, DustBunnyEntityModel>(
        context,
        DustBunnyEntityModel(context.getPart(DnDEntityModelLayers.DUST_BUNNY)),
        0f
    ) {
    init {
        this.addFeature(HeldItemFeatureRenderer(this, context.heldItemRenderer))
    }

    override fun getTexture(entity: DustBunnyEntity): Identifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
}