package org.teamvoided.dusk_debris.entity.dice

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DiceEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.dice.render.DiceEntityModel

class DiceEntityRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<DiceEntity>(context) {
    private val model = DiceEntityModel(context.bakeLayer(DuskEntityModelLayers.DICE))

    override fun render(
        entity: DiceEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        if (entity.tickCount >= 2 ||
            !(entityRenderDispatcher.camera.entity.distanceToSqr(entity) < distance.toDouble())
        ) {
            matrices.pushPose()
            val age = entity.tickCount.toFloat() + tickDelta
            model.prepareMobModel(entity, 0f, 0f, tickDelta)
            model.setupAnim(entity, 0f, 0f, age, 0f, 0f)
            model.renderToBuffer(
                matrices,
                vertexConsumers.getBuffer(RenderType.entitySolid(TEXTURE)),
                light,
                OverlayTexture.NO_OVERLAY,
                -1
            )
            matrices.popPose()
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTextureLocation(diceEntity: DiceEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val distance = Mth.square(3.5f)
        private val TEXTURE: ResourceLocation = id("textures/entity/dice/die.png")
    }
}