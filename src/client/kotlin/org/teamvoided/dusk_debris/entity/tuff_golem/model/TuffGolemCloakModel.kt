package org.teamvoided.dusk_debris.entity.tuff_golem.model


import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.TuffGolemEntity

class TuffGolemCloakModel(private val root: ModelPart) : SinglePartEntityModel<TuffGolemEntity>() {
    val cloak_item: ModelPart = root.getChild("cloak_item")
    val cloak_no_item: ModelPart = root.getChild("cloak_no_item")

    override fun setAngles(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (tuffGolemEntity.isHoldingItem()) {
            cloak_item.visible = true
            cloak_no_item.visible = false
        } else {
            cloak_item.visible = false
            cloak_no_item.visible = true
        }
//        super.setAngles(tuffGolemEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch)
    }

    override fun getPart(): ModelPart {
        return this.root
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "cloak_no_item",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                            -5f, 0f, -4f,
                            10f, 8f, 8f,
                            Dilation(0.5f)
                        ),
                    ModelTransform.pivot(0f, 15f, 0f)
                )
                modelPartData.addChild(
                    "cloak_item",
                    ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(
                            -5f, 0f, -9f,
                            10f, 8f, 13f,
                            Dilation(0.5f)
                        ),
                    ModelTransform.pivot(0f, 15f, 0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}