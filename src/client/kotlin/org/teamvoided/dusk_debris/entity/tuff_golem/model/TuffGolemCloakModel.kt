package org.teamvoided.dusk_debris.entity.tuff_golem.model


import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.animation.TuffGolemEntityAnimations

class TuffGolemCloakModel(private val root: ModelPart) : SinglePartEntityModel<TuffGolemEntity>() {
    val body: ModelPart = root.getChild("body")
    val cloak_item: ModelPart = body.getChild("cloak_item")
    val cloak_no_item: ModelPart = body.getChild("cloak_no_item")

    override fun setAngles(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        this.part.traverse().forEach(ModelPart::resetTransform)
        this.animateWalk(TuffGolemEntityAnimations.WALK, limbAngle, limbDistance, 16.5f, 2.5f)
        cloak_item.visible = tuffGolemEntity.isHoldingItem()
        cloak_no_item.visible = !tuffGolemEntity.isHoldingItem()
    }

    override fun getPart(): ModelPart {
        return this.root
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val body = modelPartData.addChild(
                    "body",
                    ModelPartBuilder.create(),
                    ModelTransform.pivot(0f, 20f, 0f)
                )
                body.addChild(
                    "cloak_no_item",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                            -5f, -5f, -4f,
                            10f, 8f, 8f,
                            Dilation(0.5f)
                        ),
                    ModelTransform.pivot(0f, 0f, 0f)
                )
                body.addChild(
                    "cloak_item",
                    ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(
                            -5f, -5f, -9f,
                            10f, 8f, 13f,
                            Dilation(0.5f)
                        ),
                    ModelTransform.pivot(0f, 0f, 0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}