package org.teamvoided.dusk_debris.entity.chill_charge

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.ChillChargeEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.chill_charge.render.ChillChargeEntityModel

class ChillChargeEntityRenderer(context: EntityRendererFactory.Context) :
    EntityRenderer<ChillChargeEntity>(context) {
    private val model = ChillChargeEntityModel(context.getPart(DuskEntityModelLayers.CHILL_CHARGE))

    override fun render(
        chillChargeEntity: ChillChargeEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if (chillChargeEntity.age >= 2 ||
            !(dispatcher.camera.focusedEntity.squaredDistanceTo(chillChargeEntity) < distance.toDouble())
        ) {
            val age = chillChargeEntity.age.toFloat() + tickDelta
            val vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getBreezeWind(
                    TEXTURE, 0.0f, 0.0f
//                    TEXTURE, this.method_55268(age) % 1.0f, 0.0f
                )
            )
            model.setAngles(chillChargeEntity, 0.0f, 0.0f, age, 0.0f, 0.0f)
            model.method_60879(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)
            super.render(chillChargeEntity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    protected fun method_55268(f: Float): Float {
        return f * 0.03f
    }

    override fun getTexture(chillChargeEntity: ChillChargeEntity): Identifier {
        return TEXTURE
    }

    companion object {
        //distance val is for an overide to render the charge
        private val distance = MathHelper.square(3.5f)
        private val TEXTURE: Identifier = id("textures/entity/projectiles/chill_charge.png")
    }
}