package org.teamvoided.dusk_debris.entity.piffling.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel

class PifflingPumpkinHeadFeatureRenderer(
    context: RenderLayerParent<PifflingPumpkinEntity, PifflingPumpkinModel>,
    private val itemRenderer: ItemRenderer
) : RenderLayer<PifflingPumpkinEntity, PifflingPumpkinModel>(context) {
    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int,
        entity: PifflingPumpkinEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        var headStack = entity.getItemBySlot(EquipmentSlot.HEAD)
        if (headStack.isEmpty) {
            headStack = Items.HEAVY_CORE.defaultInstance //DnDBlocks.SMALL_CARVED_PUMPKIN.asItem().defaultStack
        }
        if ((!entity.isInvisible || (Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible))) {
            matrices.pushPose()
            val model = (this.parentModel as PifflingPumpkinModel)
            model.head.moveRelativeTo(matrices, model)
//            moveRelativeToHead(matrices, model)
            val scale = 1f
            matrices.translate(0.0f, -0.5f, 0.0f)
            matrices.mulPose(Axis.YP.rotationDegrees(180.0f))
            matrices.scale(scale, -scale, -scale)
            itemRenderer.renderStatic(
                entity,
                ItemStack(headStack.item),
                ItemDisplayContext.NONE,
                false,
                matrices,
                vertexConsumers,
                entity.level(),
                i,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0f),
                entity.id
            )
            matrices.popPose()
        }
    }
    companion object{
        fun ModelPart.moveRelativeTo(matrix: PoseStack, model: PifflingPumpkinModel) {
            val bone = model.bone
            val body = model.body

            matrix.translate(bone.x / 16.0f, bone.y / 16.0f, bone.z / 16.0f)
            matrix.mulPose(Quaternionf().rotationZYX(bone.zRot, bone.yRot, bone.xRot))

            matrix.translate(body.x / 16.0f, body.y / 16.0f, body.z / 16.0f)
            matrix.mulPose(Quaternionf().rotationZYX(body.zRot, body.yRot, body.xRot))

            matrix.translate(this.x / 16.0f, this.y / 16.0f, this.z / 16.0f)
            matrix.mulPose(Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot))

            if (this.xScale != 1.0f || (this.yScale != 1.0f) || (this.zScale != 1.0f)) {
                matrix.scale(this.xScale, this.yScale, this.zScale)
            }
        }
    }
}