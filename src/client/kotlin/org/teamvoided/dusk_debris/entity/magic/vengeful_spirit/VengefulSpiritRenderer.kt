package org.teamvoided.dusk_debris.entity.magic.vengeful_spirit

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.util.Utils


class VengefulSpiritRenderer(context: EntityRendererProvider.Context) : EntityRenderer<VengefulSpiritEntity>(context) {
    private val model = VengefulSpiritModel(context.bakeLayer(DuskEntityModelLayers.VENGEFUL_SPIRIT))

    init {}

    override fun render(
        entity: VengefulSpiritEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        if (entity.tickCount >= 4 || entityRenderDispatcher.camera.entity.distanceToSqr(entity) > entity.size * entity.size) {
            val vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE))
            val yawStuff =
                Mth.wrapDegrees(Mth.rotLerp(tickDelta, entity.yRotO, entity.yRot)) * Utils.DEG_TO_RAD
            val pitchStuff = Mth.wrapDegrees(
                Mth.rotLerp(
                    tickDelta,
                    entity.xRotO,
                    entity.xRot
                )
            ) * Utils.DEG_TO_RAD
            model.setupAnim(entity, 0f, 0f, entity.tickCount + tickDelta, yawStuff, pitchStuff)
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY)
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTextureLocation(entity: VengefulSpiritEntity): ResourceLocation {
        return TEXTURE
    }

    override fun getBlockLightLevel(entity: VengefulSpiritEntity, pos: BlockPos): Int = 15

    companion object {
        private val TEXTURE: ResourceLocation = DuskDebris.id("textures/entity/vengeful_spirit/vengeful_spirit.png")
    }
}