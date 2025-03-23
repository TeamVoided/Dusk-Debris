package org.teamvoided.dusk_debris.entity.chill_charge.render

import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.ChillChargeEntity

class ChillChargeEntityModel(root: ModelPart) :
    SinglePartEntityModel<ChillChargeEntity>(RenderLayer::getEntityTranslucent) {
    private val bone: ModelPart = root.getChild("bone")
    private val chillCharge: ModelPart = bone.getChild("chill_charge")

    private val chill: ModelPart = bone.getChild("chill")

    override fun setAngles(
        entity: ChillChargeEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        chillCharge.yaw = animationProgress * rotationSpeed * 0.0175f
        chill.yaw = animationProgress * rotationSpeed * 0.0175f
    }

    override fun getPart(): ModelPart = this.bone


    companion object {
        private const val rotationSpeed = 16f
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData: ModelPartData = modelData.root
                val modelPartData2: ModelPartData = modelPartData.addChild(
                    "bone",
                    ModelPartBuilder.create(),
                    ModelTransform.pivot(0.0f, 0.0f, 0.0f)
                )
                modelPartData2.addChild(
                    "chill",
                    ModelPartBuilder.create()
                        .uv(0, 9).cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, Dilation(0.25f)),
//                        .uv(15, 20).cuboid(-4.0f, -1.0f, -4.0f, 8.0f, 2.0f, 8.0f, Dilation(0.0f))
//                        .uv(0, 9).cuboid(-3.0f, -2.0f, -3.0f, 6.0f, 4.0f, 6.0f, Dilation(0.0f)),
                    ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, -0.7854f, 0.0f)
                )
                modelPartData2.addChild(
                    "chill_charge",
                    ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 0.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 16, 16)
            }
    }
}