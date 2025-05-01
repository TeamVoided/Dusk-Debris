package org.teamvoided.dusk_debris.entity.magic.vengeful_spirit

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.util.Utils


class VengefulSpiritRenderer(context: EntityRendererFactory.Context) : EntityRenderer<VengefulSpiritEntity>(context) {
    private val model = VengefulSpiritModel(context.getPart(DuskEntityModelLayers.VENGEFUL_SPIRIT))

    init {}

    override fun render(
        entity: VengefulSpiritEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if (entity.age >= 4 || dispatcher.camera.focusedEntity.squaredDistanceTo(entity) > entity.size * entity.size) {
            val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE))
            val yawStuff =
                MathHelper.wrapDegrees(MathHelper.lerpDegrees(tickDelta, entity.prevYaw, entity.yaw)) * Utils.DEG_TO_RAD
            val pitchStuff =
                MathHelper.wrapDegrees(
                    MathHelper.lerpDegrees(
                        tickDelta,
                        entity.prevPitch,
                        entity.pitch
                    )
                ) * Utils.DEG_TO_RAD
            model.setAngles(entity, 0f, 0f, entity.age + tickDelta, yawStuff, pitchStuff)
            model.method_60879(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTexture(entity: VengefulSpiritEntity): Identifier {
        return TEXTURE
    }

    override fun getBlockLight(entity: VengefulSpiritEntity, pos: BlockPos): Int = 15

    companion object {
        private val distance = MathHelper.square(3.5)
        private val TEXTURE: Identifier = DuskDebris.id("textures/entity/vengeful_spirit/vengeful_spirit.png")
    }
}