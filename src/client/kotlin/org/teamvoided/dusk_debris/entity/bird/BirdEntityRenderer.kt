package org.teamvoided.dusk_debris.entity.bird

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.entity.BirdEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.bird.render.BirdEntityModel

class BirdEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<BirdEntity, BirdEntityModel>(
        context,
        BirdEntityModel(context.bakeLayer(DuskEntityModelLayers.BIRD)),
        0.3f
    ) {
    override fun getTextureLocation(parrotEntity: BirdEntity): ResourceLocation {
        return TEXTURE
    }

    override fun getBob(birdEntity: BirdEntity, f: Float): Float {
        val g = Mth.lerp(f, birdEntity.prevFlapProgress, birdEntity.flapProgress)
        val h = Mth.lerp(f, birdEntity.prevMaxWingDeviation, birdEntity.maxWingDeviation)
        return (Mth.sin(g) + 1.0f) * h
    }

    companion object {
        private val TEXTURE: ResourceLocation = ResourceLocation.withDefaultNamespace("textures/block/white_concrete.png")
    }
}