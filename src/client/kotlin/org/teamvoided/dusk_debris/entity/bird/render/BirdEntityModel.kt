package org.teamvoided.dusk_debris.entity.bird.render

import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.BirdEntity

class BirdEntityModel(root: ModelPart) :
    SinglePartEntityModel<BirdEntity>(RenderLayer::getEntityTranslucent) {
    private val bone: ModelPart = root.getChild("bone")
    private val block: ModelPart = bone.getChild("block")

    override fun setAngles(
        entity: BirdEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        block.yaw = headYaw * 0.017453292F;
        block.pitch = headPitch * 0.017453292F;
    }

    override fun getPart(): ModelPart = this.bone

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData: ModelPartData = modelData.root
                val modelPartData2: ModelPartData = modelPartData.addChild(
                    "bone",
                    ModelPartBuilder.create(),
                    ModelTransform.pivot(0f, 16f, 0f)
                )
                modelPartData2.addChild(
                    "block",
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 10.0f, 4.0f, Dilation.NONE),
                    ModelTransform.pivot(0f, 0f, 0f)
                ).addChild(
                    "beak",
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-1.0f, 0.0f, -4.0f, 2.0f, 2.0f, 2.0f, Dilation.NONE),
                    ModelTransform.pivot(0.0f, 0.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 16, 16)
            }
    }
}