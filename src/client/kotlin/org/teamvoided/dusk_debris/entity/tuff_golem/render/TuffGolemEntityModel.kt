package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.TuffGolemEntity

class TuffGolemEntityModel<T>(private val root: ModelPart) : SinglePartEntityModel<TuffGolemEntity>() {
    private val head: ModelPart = root.getChild("head")
    val rightArm: ModelPart = root.getChild("right_arm")
    private val leftArm: ModelPart = root.getChild("left_arm")
    private val rightLeg: ModelPart = root.getChild("right_leg")
    private val leftLeg: ModelPart = root.getChild("left_leg")

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        ironGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        head.yaw = headYaw * 0.017453292f
        head.pitch = headPitch * 0.017453292f
        rightLeg.pitch = -1.5f * MathHelper.wrap(limbAngle, 13.0f) * limbDistance
        leftLeg.pitch = 1.5f * MathHelper.wrap(limbAngle, 13.0f) * limbDistance
        rightLeg.yaw = 0.0f
        leftLeg.yaw = 0.0f
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
                    "head",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, -12.0f, -5.5f, 8.0f, 10.0f, 8.0f)
                        .uv(0, 0)
                        .cuboid(-1.0f, -5.0f, -7.5f, 2.0f, 4.0f, 2.0f),
                    ModelTransform.pivot(0.0f, -7.0f, -2.0f)
                )
                modelPartData.addChild(
                    "body",
                    ModelPartBuilder.create()
                        .uv(0, 40)
                        .cuboid(-9.0f, -2.0f, -6.0f, 18.0f, 12.0f, 11.0f)
                        .uv(0, 70)
                        .cuboid(-4.5f, 10.0f, -3.0f, 9.0f, 5.0f, 6.0f, Dilation(0.5f)),
                    ModelTransform.pivot(0.0f, -7.0f, 0.0f)
                )
                modelPartData.addChild(
                    "right_arm",
                    ModelPartBuilder.create()
                        .uv(60, 21)
                        .cuboid(-13.0f, -2.5f, -3.0f, 4.0f, 30.0f, 6.0f),
                    ModelTransform.pivot(0.0f, -7.0f, 0.0f)
                )
                modelPartData.addChild(
                    "left_arm",
                    ModelPartBuilder.create()
                        .uv(60, 58)
                        .cuboid(9.0f, -2.5f, -3.0f, 4.0f, 30.0f, 6.0f),
                    ModelTransform.pivot(0.0f, -7.0f, 0.0f)
                )
                modelPartData.addChild(
                    "right_leg",
                    ModelPartBuilder.create()
                        .uv(37, 0)
                        .cuboid(-3.5f, -3.0f, -3.0f, 6.0f, 16.0f, 5.0f),
                    ModelTransform.pivot(-4.0f, 11.0f, 0.0f)
                )
                modelPartData.addChild(
                    "left_leg",
                    ModelPartBuilder.create()
                        .uv(60, 0)
                        .mirrored()
                        .cuboid(-3.5f, -3.0f, -3.0f, 6.0f, 16.0f, 5.0f),
                    ModelTransform.pivot(5.0f, 11.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 128, 128)
            }
    }
}