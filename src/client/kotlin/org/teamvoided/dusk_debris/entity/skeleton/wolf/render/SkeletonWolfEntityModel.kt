package org.teamvoided.dusk_debris.entity.skeleton.wolf.render

import com.google.common.collect.ImmutableList
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.ColorableAgeableListModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.entity.SkeletonWolfEntity

@Environment(EnvType.CLIENT)
class SkeletonWolfEntityModel<T : SkeletonWolfEntity>(root: ModelPart) : ColorableAgeableListModel<T>() {
    private val head: ModelPart = root.getChild("head")
    private val realHead: ModelPart = head.getChild(REAL_HEAD)
    private val torso: ModelPart = root.getChild("body")
    private val rightHindLeg: ModelPart = root.getChild("right_hind_leg")
    private val leftHindLeg: ModelPart = root.getChild("left_hind_leg")
    private val rightFrontLeg: ModelPart = root.getChild("right_front_leg")
    private val leftFrontLeg: ModelPart = root.getChild("left_front_leg")
    private val tail: ModelPart = root.getChild("tail")
    private val realTail: ModelPart = tail.getChild(REAL_TAIL)

    private val neck: ModelPart = root.getChild(UPPER_BODY)

    override fun headParts(): Iterable<ModelPart> {
        return ImmutableList.of(this.head)
    }

    override fun bodyParts(): Iterable<ModelPart> {
        return ImmutableList.of(
            this.torso,
            this.rightHindLeg,
            this.leftHindLeg,
            this.rightFrontLeg,
            this.leftFrontLeg,
            this.tail,
            this.neck
        )
    }

    override fun prepareMobModel(wolfEntity: T, f: Float, g: Float, h: Float) {
        if (wolfEntity.isAngry) {
            tail.yRot = 0.0f
        } else {
            tail.yRot = Mth.cos(f * 0.6662f) * 1.4f * g
        }

        torso.setPos(0.0f, 14.0f, 2.0f)
        torso.xRot = 1.5707964f
        neck.setPos(-1.0f, 14.0f, -3.0f)
        neck.xRot = torso.xRot
        tail.setPos(-1.0f, 12.0f, 8.0f)
        rightHindLeg.setPos(-2.5f, 16.0f, 7.0f)
        leftHindLeg.setPos(0.5f, 16.0f, 7.0f)
        rightFrontLeg.setPos(-2.5f, 16.0f, -4.0f)
        leftFrontLeg.setPos(0.5f, 16.0f, -4.0f)
        rightHindLeg.xRot = Mth.cos(f * 0.6662f) * 1.4f * g
        leftHindLeg.xRot = Mth.cos(f * 0.6662f + 3.1415927f) * 1.4f * g
        rightFrontLeg.xRot = Mth.cos(f * 0.6662f + 3.1415927f) * 1.4f * g
        leftFrontLeg.xRot = Mth.cos(f * 0.6662f) * 1.4f * g
    }

    override fun setupAnim(wolfEntity: T, f: Float, g: Float, h: Float, i: Float, j: Float) {
        head.xRot = j * 0.017453292f
        head.yRot = i * 0.017453292f
        tail.xRot = h
    }

    companion object {
        private const val REAL_HEAD = "real_head"
        private const val UPPER_BODY = "upper_body"
        private const val REAL_TAIL = "real_tail"
        private const val LEG_SIZE = 8

        fun texturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val modelPartData2 =
                modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0f, 13.5f, -7.0f))
            modelPartData2.addOrReplaceChild(
                REAL_HEAD,
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -3.0f, -2.0f, 6.0f, 6.0f, 4.0f)
                    .texOffs(16, 14)
                    .addBox(-2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).texOffs(16, 14)
                    .addBox(2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).texOffs(0, 10)
                    .addBox(-0.5f, -0.001f, -5.0f, 3.0f, 3.0f, 4.0f),
                PartPose.ZERO
            )
            modelPartData.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(18, 14).addBox(-3.0f, -2.0f, -3.0f, 6.0f, 9.0f, 6.0f),
                PartPose.offsetAndRotation(0.0f, 14.0f, 2.0f, 1.5707964f, 0.0f, 0.0f)
            )
            modelPartData.addOrReplaceChild(
                UPPER_BODY,
                CubeListBuilder.create().texOffs(21, 0).addBox(-3.0f, -3.0f, -3.0f, 8.0f, 6.0f, 7.0f),
                PartPose.offsetAndRotation(-1.0f, 14.0f, -3.0f, 1.5707964f, 0.0f, 0.0f)
            )
            val modelPartBuilder =
                CubeListBuilder.create().texOffs(0, 18).addBox(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f)
            modelPartData.addOrReplaceChild("right_hind_leg", modelPartBuilder, PartPose.offset(-2.5f, 16.0f, 7.0f))
            modelPartData.addOrReplaceChild("left_hind_leg", modelPartBuilder, PartPose.offset(0.5f, 16.0f, 7.0f))
            modelPartData.addOrReplaceChild("right_front_leg", modelPartBuilder, PartPose.offset(-2.5f, 16.0f, -4.0f))
            modelPartData.addOrReplaceChild("left_front_leg", modelPartBuilder, PartPose.offset(0.5f, 16.0f, -4.0f))
            val modelPartData3 = modelPartData.addOrReplaceChild(
                "tail",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1.0f, 12.0f, 8.0f, 0.62831855f, 0.0f, 0.0f)
            )
            modelPartData3.addOrReplaceChild(
                REAL_TAIL,
                CubeListBuilder.create().texOffs(9, 18).addBox(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f),
                PartPose.ZERO
            )
            return LayerDefinition.create(modelData, 64, 32)
        }
    }
}