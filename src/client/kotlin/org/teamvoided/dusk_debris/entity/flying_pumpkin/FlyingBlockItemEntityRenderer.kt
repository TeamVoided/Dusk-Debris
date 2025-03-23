package org.teamvoided.dusk_debris.entity.flying_pumpkin

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.FlyingBlockItemEntity


class FlyingBlockItemEntityRenderer<T>(
    ctx: EntityRendererFactory.Context, private val scale: Float, private val lit: Boolean
) : EntityRenderer<T>(ctx) where T : Entity, T : FlyingBlockItemEntity {
    private val blockRenderer = ctx.blockRenderManager

    constructor(context: EntityRendererFactory.Context) : this(context, 1.0f, false)

    override fun getBlockLight(entity: T, pos: BlockPos): Int {
        return if (this.lit) 15 else super.getBlockLight(entity, pos)
    }

    override fun render(
        entity: T,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if (entity.age >= 2 ||
            !(dispatcher.camera.focusedEntity.squaredDistanceTo(entity) < distance)) {
            matrices.push()
            matrices.scale(this.scale, this.scale, this.scale)
            matrices.translate(0f, 0.25f, 0f)
            matrices.rotate(
                Axis.Y_POSITIVE.rotationDegrees(
                    MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw) + 90f
                )
            )
            matrices.rotate(
                Axis.Z_POSITIVE.rotationDegrees(
                    -MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)
                )
            )
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(90f))

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
            matrices.rotate(
                Axis.Z_POSITIVE.rotation(
                    MathHelper.lerp(tickDelta, (entity.age - 1) * 0.3f, entity.age * 0.3f)
                )
            )
            //                 //


            matrices.translate(-OFFSET, -0.25f, -OFFSET)
            blockRenderer.renderBlockAsEntity(
                entity.getState(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV
            )
            matrices.pop()
            sendMessageIngame(entity.yaw.toString())
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        }
    }

    override fun getTexture(entity: T): Identifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE

    companion object {
        private val xRotatorMult = 0.3f
        private val yRotatorMult = 0.3f
        private val zRotatorMult = 0.1f
        private val distance = MathHelper.square(3.5f).toDouble()
        const val OFFSET = 0.5f
    }
}
