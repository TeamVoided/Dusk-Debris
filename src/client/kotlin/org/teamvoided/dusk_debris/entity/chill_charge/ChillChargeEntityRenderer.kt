package org.teamvoided.dusk_debris.entity.chill_charge

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.ChillChargeEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.chill_charge.render.ChillChargeEntityModel

class ChillChargeEntityRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<ChillChargeEntity>(context) {
    private val model = ChillChargeEntityModel(context.bakeLayer(DuskEntityModelLayers.CHILL_CHARGE))

    override fun render(
        chillChargeEntity: ChillChargeEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        if (chillChargeEntity.tickCount >= 2 ||
            !(entityRenderDispatcher.camera.entity.distanceToSqr(chillChargeEntity) < distance.toDouble())
        ) {
            val age = chillChargeEntity.tickCount.toFloat() + tickDelta
            val vertexConsumer = vertexConsumers.getBuffer(
                RenderType.breezeWind(
                    TEXTURE, 0.0f, 0.0f
//                    TEXTURE, this.method_55268(age) % 1.0f, 0.0f
                )
            )
            model.setupAnim(chillChargeEntity, 0.0f, 0.0f, age, 0.0f, 0.0f)
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY)
            super.render(chillChargeEntity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    protected fun method_55268(f: Float): Float {
        return f * 0.03f
    }

    override fun getTextureLocation(chillChargeEntity: ChillChargeEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        //distance val is for an overide to render the charge
        private val distance = Mth.square(3.5f)
        private val TEXTURE: ResourceLocation = id("textures/entity/projectiles/chill_charge.png")
    }
}