package org.teamvoided.dusk_debris.entity.block

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import org.teamvoided.dusk_debris.DuskDebris.id

class TreasureChestBlockEntityModel(root: ModelPart) : Model(RenderLayer::getEntitySolid) {
    private val lid: ModelPart = root.getChild("lid")
    private val base: ModelPart = root.getChild("base")
    private val locks: ModelPart = root.getChild("locks")

    fun setupAnim(
        entity: Entity?,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
    }

    override fun method_2828(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        light: Int,
        overlay: Int,
        color: Int
    ) {
        lid.method_22699(matrices, vertexConsumer, light, overlay, color)
        base.method_22699(matrices, vertexConsumer, light, overlay, color)
        locks.method_22699(matrices, vertexConsumer, light, overlay, color)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION = ModelIdentifier(id("forgotten_chest_converted"), "main")

        fun createBodyLayer(): TexturedModelData {
            val meshdefinition = ModelData()
            val partdefinition = meshdefinition.root

            val lid = partdefinition.addChild(
                "lid",
                ModelPartBuilder.create().uv(0, 24)
                    .cuboid(-7.0f, -6.0f, -7.0f, 14.0f, 5.0f, 14.0f)
                    .uv(0, 0).cuboid(2.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f)
                    .uv(0, 0).cuboid(-4.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f),
                ModelTransform.pivot(0.0f, 16.0f, 0.0f)
            )

            val base = partdefinition.addChild(
                "base",
                ModelPartBuilder.create().uv(0, 0)
                    .cuboid(-7.0f, -2.0f, -7.0f, 14.0f, 10.0f, 14.0f, Dilation(-0.001f)),
                ModelTransform.pivot(0.0f, 16.0f, 0.0f)
            )

            val locks = partdefinition.addChild(
                "locks",
                ModelPartBuilder.create().uv(0, 5)
                    .cuboid(-13.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f)
                    .uv(0, 5).cuboid(-7.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f),
                ModelTransform.pivot(8.0f, 24.0f, -8.0f)
            )

            return TexturedModelData.of(meshdefinition, 64, 64)
        }
    }
}