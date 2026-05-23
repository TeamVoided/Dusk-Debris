package org.teamvoided.dusk_debris.entity.piffling.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.ItemInHandRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeadFeatureRenderer.Companion.moveRelativeTo

class PifflingPumpkinHeldItemFeatureRenderer(
    context: RenderLayerParent<PifflingPumpkinEntity, PifflingPumpkinModel>,
    private val heldItemRenderer: ItemInHandRenderer
) : RenderLayer<PifflingPumpkinEntity, PifflingPumpkinModel>(context) {
    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        entity: PifflingPumpkinEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val bl = entity.mainArm == HumanoidArm.RIGHT
        val mainhand: ItemStack = if (bl) entity.mainHandItem else entity.offhandItem
        val offhand: ItemStack = if (bl) entity.offhandItem else entity.mainHandItem
        if (!mainhand.isEmpty || !offhand.isEmpty) {
            val model = (this.parentModel as PifflingPumpkinModel)
            this.renderItem(
                entity,
                model,
                model.rightArm,
                mainhand,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                HumanoidArm.RIGHT,
                matrices,
                vertexConsumers,
                light
            )
            this.renderItem(
                entity,
                model,
                model.leftArm,
                offhand,
                ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                HumanoidArm.LEFT,
                matrices,
                vertexConsumers,
                light
            )
        }
    }

    fun renderItem(
        entity: LivingEntity,
        model: PifflingPumpkinModel,
        armPart: ModelPart,
        stack: ItemStack,
        transformationMode: ItemDisplayContext,
        arm: HumanoidArm,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        if (!stack.isEmpty) {
//            matrices.push()
//            armPart.moveRelativeToHand(matrices, model)
//            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(-90.0f))
//            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))

//            matrices.translate((if (bl) -1 else 1).toFloat() / 16.0f, 0.125f, -0.625f)
//            heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light)
//            matrices.pop()


            matrices.pushPose()
            armPart.moveRelativeTo(matrices, model)
            matrices.translate(0f, 0.3f, -0.1f)
            matrices.mulPose(Axis.XP.rotationDegrees(-90.0f))
            matrices.mulPose(Axis.YP.rotationDegrees(180.0f))
            val bl = arm == HumanoidArm.LEFT
            heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light)
            matrices.popPose()
        }
    }
}