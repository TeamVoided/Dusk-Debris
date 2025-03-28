package org.teamvoided.dusk_debris.entity.skeleton.gloom.render

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.ColorUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.GloomEntity

class GloomEyesFeatureRenderer<M : EntityModel<GloomEntity>>(featureRendererContext: FeatureRendererContext<GloomEntity, M>) :
    FeatureRenderer<GloomEntity, M>(featureRendererContext) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: GloomEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val time: Int = entity.age / 25 + entity.id
        val size = DyeColor.entries.size
        val prev = time % size
        val next = (time + 1) % size
        val lerp: Float = ((entity.age % 25).toFloat() + tickDelta) / 25.0f
        val prevColor = SheepEntity.getColor(DyeColor.byId(prev))
        val nextColor = SheepEntity.getColor(DyeColor.byId(next))
        val color = ColorUtil.Argb32.lerp(lerp, prevColor, nextColor)
        //val color = entity.eyeColor
        //val color = 0xFFFFFF

        val vertexConsumerGlow = vertexConsumers.getBuffer(getGlowingEyesLayer())
        val vertexConsumerRegular = vertexConsumers.getBuffer(getEyesLayer())
        //this.contextModel!!.method_2828(matrices, vertexConsumerGlow, 15728640, OverlayTexture.DEFAULT_UV, color)
        this.contextModel!!.method_2828(matrices, vertexConsumerRegular, 15728640, OverlayTexture.DEFAULT_UV, color)
    }

    fun getGlowingEyesLayer(): RenderLayer = EYES_GLOWING
    fun getEyesLayer(): RenderLayer = EYES


    companion object {
        private val EYES_TEXTURE: Identifier = id("textures/entity/skeleton/gloomed_eyes.png")
        private val EYES_GLOWING: RenderLayer = RenderLayer.getEntityCutoutNoCull(EYES_TEXTURE)
        private val EYES: RenderLayer = RenderLayer.getEyes(EYES_TEXTURE)
    }
}