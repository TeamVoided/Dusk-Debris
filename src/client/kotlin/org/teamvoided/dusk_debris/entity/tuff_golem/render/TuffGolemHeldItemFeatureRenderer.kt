package org.teamvoided.dusk_debris.entity.tuff_golem.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.ItemInHandRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemHeldItemFeatureRenderer(
    context: RenderLayerParent<TuffGolemEntity, TuffGolemEntityModel>,
    private val heldItemRenderer: ItemInHandRenderer
) : RenderLayer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    val scale = 0.625f
    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val itemStack = tuffGolemEntity.getItemBySlot(EquipmentSlot.MAINHAND)
        if (!itemStack.isEmpty) {
            matrices.pushPose()
            (this.parentModel as TuffGolemEntityModel).body.translateAndRotate(matrices)
            val scale = 0.625f
            matrices.translate(0.0f, -0.50001f, -0.55f)
//            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
            matrices.scale(-scale, -scale, scale)
            heldItemRenderer.renderItem(
                tuffGolemEntity,
                ItemStack(itemStack.item),
                ItemDisplayContext.FIXED,
                false,
                matrices,
                vertexConsumers,
                light
            )
            matrices.popPose()
        }

//        val itemStack = tuffGolemEntity.getEquippedStack(EquipmentSlot.MAINHAND)
//        if (!itemStack.isEmpty) {
//            matrices.push()
//            matrices.translate(
//                (this.contextModel as TuffGolemEntityModel).body.pivotX / 16.0f,
//                (this.contextModel as TuffGolemEntityModel).body.pivotY / 16.0f,
//                (this.contextModel as TuffGolemEntityModel).body.pivotZ / 16.0f
//            )
//            matrices.translate(0f, -0.3f, -0.5f)
//            matrices.scale(scale, scale, scale)
//            matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(180.0f))
//            //ADD ITEM TAG FOR ROTATION
//            //if (itemStack.isIn(TAG)){
//            //matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90.0f))
//            //}
//            heldItemRenderer.renderItem(
//                tuffGolemEntity,
//                itemStack,
//                ModelTransformationMode.FIXED,
//                false,
//                matrices,
//                vertexConsumers,
//                light
//            )
//            matrices.pop()
//        }
    }
}