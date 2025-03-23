package org.teamvoided.dusk_debris.entity.dice

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DiceEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.dice.render.DiceEntityModel

class DiceEntityRenderer(context: EntityRendererFactory.Context) :
    EntityRenderer<DiceEntity>(context) {
    private val model = DiceEntityModel(context.getPart(DuskEntityModelLayers.DICE))

    override fun render(
        entity: DiceEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if (entity.age >= 2 ||
            !(dispatcher.camera.focusedEntity.squaredDistanceTo(entity) < distance.toDouble())
        ) {
            matrices.push()
            val age = entity.age.toFloat() + tickDelta
            model.animateModel(entity, 0f, 0f, tickDelta)
            model.setAngles(entity, 0f, 0f, age, 0f, 0f)
            model.method_2828(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TEXTURE)),
                light,
                OverlayTexture.DEFAULT_UV,
                -1
            )
            matrices.pop()
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTexture(diceEntity: DiceEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val distance = MathHelper.square(3.5f)
        private val TEXTURE: Identifier = id("textures/entity/dice/die.png")
    }
}