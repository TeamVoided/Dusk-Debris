package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemHeldItemFeatureRenderer(
    context: FeatureRendererContext<TuffGolemEntity, TuffGolemEntityModel>,
    private val heldItemRenderer: HeldItemRenderer
) : FeatureRenderer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    val scale = 0.8f
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val itemStack = tuffGolemEntity.getEquippedStack(EquipmentSlot.MAINHAND)
        matrices.push()
        matrices.translate(
            (this.contextModel as TuffGolemEntityModel).body.pivotX / 16.0f,
            (this.contextModel as TuffGolemEntityModel).body.pivotY / 16.0f,
            (this.contextModel as TuffGolemEntityModel).body.pivotZ / 16.0f
        )
        matrices.translate(0f, 0.27f, -0.5f)
        matrices.scale(scale, scale, scale)
        matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(180.0f))
        //ADD ITEM TAG FOR ROTATION
        //if (itemStack.isIn(TAG)){
        //matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90.0f))
        //}
        heldItemRenderer.renderItem(
            tuffGolemEntity,
            itemStack,
            ModelTransformationMode.FIXED,
            false,
            matrices,
            vertexConsumers,
            light
        )
        matrices.pop()
    }
}