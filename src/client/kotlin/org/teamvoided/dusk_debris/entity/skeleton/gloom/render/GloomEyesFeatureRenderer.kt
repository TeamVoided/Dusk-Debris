package org.teamvoided.dusk_debris.entity.skeleton.gloom.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FastColor
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.item.DyeColor
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.GloomEntity

class GloomEyesFeatureRenderer<M : EntityModel<GloomEntity>>(featureRendererContext: RenderLayerParent<GloomEntity, M>) :
    RenderLayer<GloomEntity, M>(featureRendererContext) {
    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        entity: GloomEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val color = getColor(entity, tickDelta)
        //val color = 0xFFFFFF

        val vertexConsumer = vertexConsumers.getBuffer(EYES)
        this.parentModel!!.renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, color)
    }

    fun getColor(entity: GloomEntity, tickDelta: Float): Int {
        if (entity.hasCustomName() && "jeb_" == entity.name.string) {
            val time: Int = entity.tickCount / 25 + entity.id
            val size = DyeColor.entries.size
            val prev = time % size
            val next = (time + 1) % size
            val lerp: Float = ((entity.tickCount % 25).toFloat() + tickDelta) / 25.0f
            val prevColor = Sheep.getColor(DyeColor.byId(prev))
            val nextColor = Sheep.getColor(DyeColor.byId(next))
            return FastColor.ARGB32.lerp(lerp, prevColor, nextColor)
        }
        return entity.eyeColor
    }


    companion object {
        private val EYES_TEXTURE: ResourceLocation = id("textures/entity/skeleton/gloomed_eyes.png")
        private val EYES: RenderType = RenderType.EYES.apply(EYES_TEXTURE, RenderStateShard.ADDITIVE_TRANSPARENCY)
    }
}