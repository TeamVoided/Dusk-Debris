package org.teamvoided.dusk_debris.entity.tuff_golem.model

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.util.Utils

class TuffGolemEntityModel(private val root: ModelPart) : SinglePartEntityModel<TuffGolemEntity>() {
    val body: ModelPart = root.getChild("body")
    val definitelySomethingElse: ModelPart = root.getChild("definitely_something_else")
    val rightArm: ModelPart = root.getChild("right_arm")
    private val leftArm: ModelPart = root.getChild("left_arm")
    private val rightLeg: ModelPart = root.getChild("right_leg")
    private val leftLeg: ModelPart = root.getChild("left_leg")

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val armAngle = MathHelper.wrap(limbAngle, 13.0f) * limbDistance
        rightLeg.pitch = -1.5f * armAngle
        leftLeg.pitch = 1.5f * armAngle
        rightLeg.yaw = 0.0f
        leftLeg.yaw = 0.0f
        rightArm.pitch = if (!tuffGolemEntity.isHoldingItem()) -1.5f * armAngle else Utils.rotate270
        leftArm.pitch = if (!tuffGolemEntity.isHoldingItem()) 1.5f * armAngle else Utils.rotate270


        definitelySomethingElse.visible =
            tuffGolemEntity.hasCustomName() && "hon hon hon" == tuffGolemEntity.name.string
    }

//    override fun animateModel(tuffGolemEntity: TuffGolemEntity, f: Float, g: Float, h: Float) {
//        val j = tuffGolemEntity.lookingAtVillagerTicks
//        if (j > 0) {
//            rightArm.pitch = -0.8f + 0.025f * MathHelper.wrap(j.toFloat(), 70.0f)
//            leftArm.pitch = 0.0f
//        } else {
//            rightArm.pitch = (-0.2f + 1.5f * MathHelper.wrap(f, 13.0f)) * g
//            leftArm.pitch = (-0.2f - 1.5f * MathHelper.wrap(f, 13.0f)) * g
//        }
//    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "body",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-5f, 0f, -4f, 10f, 13f, 8f)
                        .uv(0, 0)
                        .cuboid(-1f, 3f, -6f, 2f, 4f, 2f),
                    ModelTransform.pivot(0f, 7f, 0f)
                )
                modelPartData.addChild(
                    "definitely_something_else",
                    ModelPartBuilder.create()
                        .uv(0, 26)
                        .cuboid(-12f, 0f, -5f, 24f, 6f, 0f),
                    ModelTransform.pivot(0f, 8f, 0f)
                )
                modelPartData.addChild(
                    "right_arm",
                    ModelPartBuilder.create()
                        .uv(36, 0)
                        .cuboid(-12f, 0f, -2f, 3f, 10f, 4f),
                    ModelTransform.pivot(4f, 13f, 0f)
                )
                modelPartData.addChild(
                    "left_arm",
                    ModelPartBuilder.create()
                        .uv(36, 0)
                        .mirrored()
                        .cuboid(9f, 0f, -2f, 3f, 10f, 4f),
                    ModelTransform.pivot(-4f, 13f, 0f)
                )
                modelPartData.addChild(
                    "right_leg",
                    ModelPartBuilder.create()
                        .uv(36, 14)
                        .cuboid(0f, -1f, -2f, 4f, 5f, 4f),
                    ModelTransform.pivot(-5f, 20f, 0f)
                )
                modelPartData.addChild(
                    "left_leg",
                    ModelPartBuilder.create()
                        .uv(36, 14)
                        .mirrored()
                        .cuboid(0f, -1f, -2f, 4f, 5f, 4f),
                    ModelTransform.pivot(1f, 20f, 0f)
                )
                return TexturedModelData.of(modelData, 64, 32)
            }
    }
}