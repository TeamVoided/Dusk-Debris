package org.teamvoided.dusk_debris.entity.tuff_golem.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemHatFeatureRenderer(
    context: RenderLayerParent<TuffGolemEntity, TuffGolemEntityModel>,
    private val itemRenderer: ItemRenderer
) : RenderLayer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    val scale = 0.8f
    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val hatStack = tuffGolemEntity.getItemBySlot(EquipmentSlot.HEAD)
        if (!hatStack.isEmpty &&
            (!tuffGolemEntity.isInvisible ||
                    (Minecraft.getInstance().shouldEntityAppearGlowing(tuffGolemEntity) && tuffGolemEntity.isInvisible))
        ) {
            matrices.pushPose()
            (this.parentModel as TuffGolemEntityModel).body.translateAndRotate(matrices)
            val scale = 0.65f
            matrices.translate(0.0f, -0.6f, 0.0f)
            matrices.mulPose(Axis.YP.rotationDegrees(180.0f))
            matrices.scale(scale, -scale, -scale)
            itemRenderer.renderStatic(
                tuffGolemEntity,
                ItemStack(hatStack.item),
                ItemDisplayContext.HEAD,
                false,
                matrices,
                vertexConsumers,
                tuffGolemEntity.level(),
                i,
                LivingEntityRenderer.getOverlayCoords(tuffGolemEntity, 0.0f),
                tuffGolemEntity.id
            )

            matrices.popPose()
        }
    }
}