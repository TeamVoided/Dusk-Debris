package org.teamvoided.dusk_debris.entity.piffling.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Axis
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel

class PifflingPumpkinHeadFeatureRenderer(
    context: FeatureRendererContext<PifflingPumpkinEntity, PifflingPumpkinModel>,
    private val itemRenderer: ItemRenderer
) : FeatureRenderer<PifflingPumpkinEntity, PifflingPumpkinModel>(context) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        entity: PifflingPumpkinEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        var headStack = entity.getEquippedStack(EquipmentSlot.HEAD)
        if (headStack.isEmpty) {
            headStack = Items.HEAVY_CORE.defaultStack //DnDBlocks.SMALL_CARVED_PUMPKIN.asItem().defaultStack
        }
        if ((!entity.isInvisible || (MinecraftClient.getInstance().hasOutline(entity) && entity.isInvisible))) {
            matrices.push()
            val model = (this.contextModel as PifflingPumpkinModel)
            model.head.moveRelativeTo(matrices, model)
//            moveRelativeToHead(matrices, model)
            val scale = 1f
            matrices.translate(0.0f, -0.5f, 0.0f)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
            matrices.scale(scale, -scale, -scale)
            itemRenderer.renderItem(
                entity,
                ItemStack(headStack.item),
                ModelTransformationMode.NONE,
                false,
                matrices,
                vertexConsumers,
                entity.world,
                i,
                LivingEntityRenderer.getOverlay(entity, 0.0f),
                entity.id
            )
            matrices.pop()
        }
    }
    companion object{
        fun ModelPart.moveRelativeTo(matrix: MatrixStack, model: PifflingPumpkinModel) {
            val bone = model.bone
            val body = model.body

            matrix.translate(bone.pivotX / 16.0f, bone.pivotY / 16.0f, bone.pivotZ / 16.0f)
            matrix.rotate(Quaternionf().rotationZYX(bone.roll, bone.yaw, bone.pitch))

            matrix.translate(body.pivotX / 16.0f, body.pivotY / 16.0f, body.pivotZ / 16.0f)
            matrix.rotate(Quaternionf().rotationZYX(body.roll, body.yaw, body.pitch))

            matrix.translate(this.pivotX / 16.0f, this.pivotY / 16.0f, this.pivotZ / 16.0f)
            matrix.rotate(Quaternionf().rotationZYX(this.roll, this.yaw, this.pitch))

            if (this.scaleX != 1.0f || (this.scaleY != 1.0f) || (this.scaleZ != 1.0f)) {
                matrix.scale(this.scaleX, this.scaleY, this.scaleZ)
            }
        }
    }
}