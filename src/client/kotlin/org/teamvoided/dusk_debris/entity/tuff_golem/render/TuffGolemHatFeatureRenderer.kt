package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemHatFeatureRenderer(
    context: FeatureRendererContext<TuffGolemEntity, TuffGolemEntityModel>,
    private val itemRenderer: ItemRenderer
) :
    FeatureRenderer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val hatStack = tuffGolemEntity.getEquippedStack(EquipmentSlot.HEAD)
        if (!hatStack.isEmpty &&
            (!tuffGolemEntity.isInvisible ||
                    (MinecraftClient.getInstance().hasOutline(tuffGolemEntity) && tuffGolemEntity.isInvisible))
        ) {
            matrices.push()
            (this.contextModel as TuffGolemEntityModel).body.rotate(matrices)
            val scale = 0.625f
            matrices.translate(0.0f, -0.34375f, 0.0f)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
            matrices.scale(scale, -scale, -scale)
            itemRenderer.renderItem(
                tuffGolemEntity,
                ItemStack(hatStack.item),
                ModelTransformationMode.HEAD,
                false,
                matrices,
                vertexConsumers,
                tuffGolemEntity.world,
                i,
                LivingEntityRenderer.getOverlay(tuffGolemEntity, 0.0f),
                tuffGolemEntity.id
            )

            matrices.pop()
        }
    }
}