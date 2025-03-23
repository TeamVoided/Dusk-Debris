package org.teamvoided.dusk_debris.entity.piffling.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Arm
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.render.PifflingPumpkinHeadFeatureRenderer.Companion.moveRelativeTo
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel

class PifflingPumpkinHeldItemFeatureRenderer(
    context: FeatureRendererContext<PifflingPumpkinEntity, PifflingPumpkinModel>,
    private val heldItemRenderer: HeldItemRenderer
) : FeatureRenderer<PifflingPumpkinEntity, PifflingPumpkinModel>(context) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: PifflingPumpkinEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val bl = entity.mainArm == Arm.RIGHT
        val mainhand: ItemStack = if (bl) entity.mainHandStack else entity.offHandStack
        val offhand: ItemStack = if (bl) entity.offHandStack else entity.mainHandStack
        if (!mainhand.isEmpty || !offhand.isEmpty) {
            val model = (this.contextModel as PifflingPumpkinModel)
            this.renderItem(
                entity,
                model,
                model.rightArm,
                mainhand,
                ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
                Arm.RIGHT,
                matrices,
                vertexConsumers,
                light
            )
            this.renderItem(
                entity,
                model,
                model.leftArm,
                offhand,
                ModelTransformationMode.THIRD_PERSON_LEFT_HAND,
                Arm.LEFT,
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
        transformationMode: ModelTransformationMode,
        arm: Arm,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
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


            matrices.push()
            armPart.moveRelativeTo(matrices, model)
            matrices.translate(0f, 0.3f, -0.1f)
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(-90.0f))
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
            val bl = arm == Arm.LEFT
            heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light)
            matrices.pop()
        }
    }
}