package org.teamvoided.dusk_debris.entity.tuff_golem.model

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.animation.ArmadilloEntityAnimations
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.animation.TuffGolemEntityAnimations
import org.teamvoided.dusk_debris.util.Utils

class TuffGolemEntityModel(private val root: ModelPart) : SinglePartEntityModel<TuffGolemEntity>() {
    val body: ModelPart = root.getChild("body")
    val definitelySomethingElse: ModelPart = body.getChild("definitely_something_else")
    val rightArm: ModelPart = body.getChild("right_arm")
    val leftArm: ModelPart = body.getChild("left_arm")
    val rightLeg: ModelPart = root.getChild("right_leg")
    val leftLeg: ModelPart = root.getChild("left_leg")

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.part.traverse().forEach(ModelPart::resetTransform)
        this.animateWalk(TuffGolemEntityAnimations.WALK, limbAngle, limbDistance, 16.5f, 2.5f)
        this.animate(tuffGolemEntity.risingState, TuffGolemEntityAnimations.ARISE, animationProgress, 1.0f)
        this.animate(tuffGolemEntity.statueState, TuffGolemEntityAnimations.STATUE, animationProgress, 1.0f)

        if (!tuffGolemEntity.isHoldingItem()) {
            rightArm.setPivot(rightArm.pivotX, -9f, rightArm.pivotZ)
            leftArm.setPivot(leftArm.pivotX, -9f, leftArm.pivotZ)
        } else {
            rightArm.pitch = Utils.rotate270
            leftArm.pitch = Utils.rotate270
            rightArm.setPivot(rightArm.pivotX, -6f, rightArm.pivotZ)
            leftArm.setPivot(leftArm.pivotX, -6f, leftArm.pivotZ)
        }
        definitelySomethingElse.visible =
            tuffGolemEntity.hasCustomName() && "hon hon hon" == tuffGolemEntity.name.string.lowercase()

//        this.animate(tuffGolemEntity.unrollingState, ArmadilloEntityAnimations.UNROLLING, h, 1.0f)
//        this.animate(tuffGolemEntity.rollingState, ArmadilloEntityAnimations.ROLLING, h, 1.0f)
//        this.animate(tuffGolemEntity.scaredState, ArmadilloEntityAnimations.SCARED, h, 1.0f)
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
                val body = modelPartData.addChild(
                    "body",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-5f, -13f, -4f, 10f, 13f, 8f)
                        .uv(0, 0)
                        .cuboid(-1f, -10f, -6f, 2f, 4f, 2f),
                    ModelTransform.pivot(0f, 20f, 0f)
                )
                body.addChild(
                    "definitely_something_else",
                    ModelPartBuilder.create()
                        .uv(0, 26)
                        .cuboid(-12f, 0f, -5f, 24f, 6f, 0f),
                    ModelTransform.pivot(0f, 8f, 0f)
                )
                body.addChild(
                    "right_arm",
                    ModelPartBuilder.create()
                        .uv(36, 0)
                        .cuboid(-12f, 0f, -2f, 3f, 10f, 4f),
                    ModelTransform.pivot(4f, -9f, 0f)
                )
                body.addChild(
                    "left_arm",
                    ModelPartBuilder.create()
                        .uv(36, 0)
                        .mirrored()
                        .cuboid(9f, 0f, -2f, 3f, 10f, 4f),
                    ModelTransform.pivot(-4f, -9f, 0f)
                )
                modelPartData.addChild(
                    "right_leg",
                    ModelPartBuilder.create()
                        .uv(36, 14)
                        .cuboid(0f, -1f, -2f, 4f, 5f, 4f, Dilation(-0.1f)),
                    ModelTransform.pivot(-5f, 20f, 0f)
                )
                modelPartData.addChild(
                    "left_leg",
                    ModelPartBuilder.create()
                        .uv(36, 14)
                        .mirrored()
                        .cuboid(0f, -1f, -2f, 4f, 5f, 4f, Dilation(-0.1f)),
                    ModelTransform.pivot(1f, 20f, 0f)
                )
                return TexturedModelData.of(modelData, 64, 32)
            }
    }
}