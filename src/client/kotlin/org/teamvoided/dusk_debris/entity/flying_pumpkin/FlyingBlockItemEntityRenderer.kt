package org.teamvoided.dusk_debris.entity.flying_pumpkin

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import org.teamvoided.dusk_debris.entity.FlyingBlockItemEntity
import org.teamvoided.dusk_debris.util.sendMessageIngame


class FlyingBlockItemEntityRenderer<T>(
    ctx: EntityRendererProvider.Context, private val scale: Float, private val lit: Boolean
) : EntityRenderer<T>(ctx) where T : Entity, T : FlyingBlockItemEntity {
    private val blockRenderer = ctx.blockRenderDispatcher

    constructor(context: EntityRendererProvider.Context) : this(context, 1.0f, false)

    override fun getBlockLightLevel(entity: T, pos: BlockPos): Int {
        return if (this.lit) 15 else super.getBlockLightLevel(entity, pos)
    }

    override fun render(
        entity: T,
        yaw: Float,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        if (entity.tickCount >= 2 || !(entityRenderDispatcher.camera.entity.distanceToSqr(entity) < distance)) {
            matrices.pushPose()
            matrices.scale(this.scale, this.scale, this.scale)
            matrices.translate(0f, 0.25f, 0f)
            matrices.mulPose(
                Axis.YP.rotationDegrees(
                    Mth.lerp(tickDelta, entity.yRotO, entity.yRot) + 90f
                )
            )
            matrices.mulPose(
                Axis.ZP.rotationDegrees(
                    -Mth.lerp(tickDelta, entity.xRotO, entity.xRot)
                )
            )
            matrices.mulPose(Axis.YP.rotationDegrees(90f))

            //funny rotation//
//            matrices.rotate(
//                Axis.Y_POSITIVE.rotation(
//                    MathHelper.lerp(tickDelta, (entity.age - 1) * yRotatorMult, entity.age * yRotatorMult)
//                )
//            )
//            matrices.rotate(
//                Axis.Z_POSITIVE.rotation(
//                    MathHelper.lerp(tickDelta, (entity.age - 1) * zRotatorMult, entity.age * zRotatorMult)
//                )
//            )
            //                 //


            //funny rotation2//
            matrices.mulPose(
                Axis.ZP.rotation(
                    Mth.lerp(tickDelta, (entity.tickCount - 1) * 0.3f, entity.tickCount * 0.3f)
                )
            )
            //                 //


            matrices.translate(-OFFSET, -0.25f, -OFFSET)
            blockRenderer.renderSingleBlock(
                entity.getState(), matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY
            )
            matrices.popPose()
            sendMessageIngame(entity.yRot.toString())
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTextureLocation(entity: T): ResourceLocation = TextureAtlas.LOCATION_BLOCKS

    companion object {
        private val xRotatorMult = 0.3f
        private val yRotatorMult = 0.3f
        private val zRotatorMult = 0.1f
        private val distance = Mth.square(3.5)
        const val OFFSET = 0.5f
    }
}
