package org.teamvoided.dusk_debris.entity.piffling.model

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.entity.PifflingPumpkinEntity
import org.teamvoided.dusk_debris.entity.piffling.animation.PifflingPumpkinAnimations

class PifflingPumpkinModel(private val root: ModelPart) : HierarchicalModel<PifflingPumpkinEntity>() {
    val bone: ModelPart = root.getChild("bone")
    val body: ModelPart = bone.getChild("body")
    val head: ModelPart = body.getChild("head")
    val rightArm: ModelPart = body.getChild("right_arm")
    val leftArm: ModelPart = body.getChild("left_arm")
    val rightLeg: ModelPart = bone.getChild("right_leg")
    val leftLeg: ModelPart = bone.getChild("left_leg")

    override fun root(): ModelPart = this.root

    override fun setupAnim(
        entity: PifflingPumpkinEntity,
        limbAngle: Float, //f //limb position
        limbDistance: Float, //g //limb speed
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
        this.animateIdlePose(animationProgress)
//        if (entity.isSprinting)
            this.animateWalk(PifflingPumpkinAnimations.RUN, limbAngle, limbDistance, 3f, 5f)
//        else
//            this.animateWalk(PifflingPumpkinAnimations.WALK, limbAngle, limbDistance, 3f, 5f)
    }

    private fun animateIdlePose(angle: Float) {
        val f = angle * 0.1f
        val cos = Mth.cos(f)
        val sin = Mth.sin(f)

        this.body.zRot += 0.025f * sin
        this.body.xRot += 0.05f * cos + 0.05f

        this.head.zRot += 0.06f * Mth.cos(f / 2)
        this.head.xRot += 0.06f * Mth.sin(f / 2)

        this.rightArm.zRot += 0.025f * -cos
        this.rightArm.xRot += 0.05f * -sin - 0.1f

        this.leftArm.zRot += 0.025f * cos
        this.leftArm.xRot += 0.05f * -sin - 0.1f
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val base = modelPartData.addOrReplaceChild(
                    "bone",
                    CubeListBuilder.create(),
//                        .uv(0, 0)
//                        .cuboid(-1f, -1f, -1f, 2f, 2f, 2f),
                    PartPose.offset(0f, 24f, 0f)
                )
                val body = base.addOrReplaceChild(
                    "body",
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3f, -5f, -2f, 6f, 5f, 4f),
                    PartPose.offset(0f, -3f, 0f)
                )
                body.addOrReplaceChild(
                    "head",
                    CubeListBuilder.create()
                        .addBox(0f, 0f, 0f, 0f, 0f, 0f),
                    PartPose.offset(0f, -5f, 0f)
                )
                body.addOrReplaceChild(
                    "right_arm",
                    CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(-1f, -1f, -1f, 2f, 6f, 2f),
                    PartPose.offsetAndRotation(-4f, -4f, 0f, 0f, 0f, 0.34907f)
                )
                body.addOrReplaceChild(
                    "left_arm",
                    CubeListBuilder.create()
                        .texOffs(20, 0)
                        .mirror()
                        .addBox(-1f, -1f, -1f, 2f, 6f, 2f),
                    PartPose.offsetAndRotation(4f, -4f, 0f, 0f, 0f, -0.34907f)
                )
                base.addOrReplaceChild(
                    "right_leg",
                    CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1f, 0f, -1f, 2f, 3f, 2f, CubeDeformation(-0.01f)),
                    PartPose.offset(-2f, -3f, 0f)
                )
                base.addOrReplaceChild(
                    "left_leg",
                    CubeListBuilder.create()
                        .texOffs(0, 9)
                        .mirror()
                        .addBox(-1f, 0f, -1f, 2f, 3f, 2f, CubeDeformation(-0.01f)),
                    PartPose.offset(2f, -3f, 0f)
                )
                return LayerDefinition.create(modelData, 32, 16)
            }
    }
}