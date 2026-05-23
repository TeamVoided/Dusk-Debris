package org.teamvoided.dusk_debris.entity.tuff_golem.model

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.animation.TuffGolemEntityAnimations
import org.teamvoided.dusk_debris.util.Utils

class TuffGolemEntityModel(private val root: ModelPart) : HierarchicalModel<TuffGolemEntity>() {
    val body: ModelPart = root.getChild("body")
    val definitelySomethingElse: ModelPart = body.getChild("definitely_something_else")
    val rightArm: ModelPart = body.getChild("right_arm")
    val leftArm: ModelPart = body.getChild("left_arm")
    val rightLeg: ModelPart = root.getChild("right_leg")
    val leftLeg: ModelPart = root.getChild("left_leg")

    override fun root(): ModelPart {
        return this.root
    }

    override fun setupAnim(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
        this.animateWalk(TuffGolemEntityAnimations.WALK, limbAngle, limbDistance, 16.5f, 2.5f)
        this.animate(tuffGolemEntity.risingAnimationState, TuffGolemEntityAnimations.ARISE, animationProgress, 1.0f)
        this.animate(tuffGolemEntity.statueAnimationState, TuffGolemEntityAnimations.STATUE, animationProgress, 1.0f)

        if (!tuffGolemEntity.isHoldingItem()) {
            rightArm.setPos(rightArm.x, -9f, rightArm.z)
            leftArm.setPos(leftArm.x, -9f, leftArm.z)
        } else {
            rightArm.xRot = Utils.rotate270
            leftArm.xRot = Utils.rotate270
            rightArm.setPos(rightArm.x, -6f, rightArm.z)
            leftArm.setPos(leftArm.x, -6f, leftArm.z)
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
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val body = modelPartData.addOrReplaceChild(
                    "body",
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5f, -13f, -4f, 10f, 13f, 8f)
                        .texOffs(0, 0)
                        .addBox(-1f, -10f, -6f, 2f, 4f, 2f),
                    PartPose.offset(0f, 20f, 0f)
                )
                body.addOrReplaceChild(
                    "definitely_something_else",
                    CubeListBuilder.create()
                        .texOffs(0, 26)
                        .addBox(-12f, 0f, -5f, 24f, 6f, 0f),
                    PartPose.offset(0f, -12f, 0f)
                )
                body.addOrReplaceChild(
                    "right_arm",
                    CubeListBuilder.create()
                        .texOffs(36, 0)
                        .addBox(-12f, 0f, -2f, 3f, 10f, 4f),
                    PartPose.offset(4f, -9f, 0f)
                )
                body.addOrReplaceChild(
                    "left_arm",
                    CubeListBuilder.create()
                        .texOffs(36, 0)
                        .mirror()
                        .addBox(9f, 0f, -2f, 3f, 10f, 4f),
                    PartPose.offset(-4f, -9f, 0f)
                )
                modelPartData.addOrReplaceChild(
                    "right_leg",
                    CubeListBuilder.create()
                        .texOffs(36, 14)
                        .addBox(0f, -1f, -2f, 4f, 5f, 4f, CubeDeformation(-0.1f)),
                    PartPose.offset(-5f, 20f, 0f)
                )
                modelPartData.addOrReplaceChild(
                    "left_leg",
                    CubeListBuilder.create()
                        .texOffs(36, 14)
                        .mirror()
                        .addBox(0f, -1f, -2f, 4f, 5f, 4f, CubeDeformation(-0.1f)),
                    PartPose.offset(1f, 20f, 0f)
                )
                return LayerDefinition.create(modelData, 64, 32)
            }
    }
}