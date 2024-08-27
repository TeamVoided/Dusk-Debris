package org.teamvoided.dusk_debris.entity.skeleton.render

import com.google.common.collect.ImmutableList
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.TintableAnimalModel
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.SkeletonWolfEntity

@Environment(EnvType.CLIENT)
class SkeletonWolfEntityModel<T : SkeletonWolfEntity>(root: ModelPart) : TintableAnimalModel<T>() {
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

    override fun getHeadParts(): Iterable<ModelPart> {
        return ImmutableList.of(this.head)
    }

    override fun getBodyParts(): Iterable<ModelPart> {
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

    override fun animateModel(wolfEntity: T, f: Float, g: Float, h: Float) {
        if (wolfEntity.hasAngerTime()) {
            tail.yaw = 0.0f
        } else {
            tail.yaw = MathHelper.cos(f * 0.6662f) * 1.4f * g
        }

        torso.setPivot(0.0f, 14.0f, 2.0f)
        torso.pitch = 1.5707964f
        neck.setPivot(-1.0f, 14.0f, -3.0f)
        neck.pitch = torso.pitch
        tail.setPivot(-1.0f, 12.0f, 8.0f)
        rightHindLeg.setPivot(-2.5f, 16.0f, 7.0f)
        leftHindLeg.setPivot(0.5f, 16.0f, 7.0f)
        rightFrontLeg.setPivot(-2.5f, 16.0f, -4.0f)
        leftFrontLeg.setPivot(0.5f, 16.0f, -4.0f)
        rightHindLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g
        leftHindLeg.pitch = MathHelper.cos(f * 0.6662f + 3.1415927f) * 1.4f * g
        rightFrontLeg.pitch = MathHelper.cos(f * 0.6662f + 3.1415927f) * 1.4f * g
        leftFrontLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g
    }

    override fun setAngles(wolfEntity: T, f: Float, g: Float, h: Float, i: Float, j: Float) {
        head.pitch = j * 0.017453292f
        head.yaw = i * 0.017453292f
        tail.pitch = h
    }

    companion object {
        private const val REAL_HEAD = "real_head"
        private const val UPPER_BODY = "upper_body"
        private const val REAL_TAIL = "real_tail"
        private const val LEG_SIZE = 8

        fun texturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root
            val modelPartData2 =
                modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(-1.0f, 13.5f, -7.0f))
            modelPartData2.addChild(
                REAL_HEAD,
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.0f, -3.0f, -2.0f, 6.0f, 6.0f, 4.0f)
                    .uv(16, 14)
                    .cuboid(-2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).uv(16, 14)
                    .cuboid(2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).uv(0, 10)
                    .cuboid(-0.5f, -0.001f, -5.0f, 3.0f, 3.0f, 4.0f),
                ModelTransform.NONE
            )
            modelPartData.addChild(
                "body",
                ModelPartBuilder.create().uv(18, 14).cuboid(-3.0f, -2.0f, -3.0f, 6.0f, 9.0f, 6.0f),
                ModelTransform.of(0.0f, 14.0f, 2.0f, 1.5707964f, 0.0f, 0.0f)
            )
            modelPartData.addChild(
                UPPER_BODY,
                ModelPartBuilder.create().uv(21, 0).cuboid(-3.0f, -3.0f, -3.0f, 8.0f, 6.0f, 7.0f),
                ModelTransform.of(-1.0f, 14.0f, -3.0f, 1.5707964f, 0.0f, 0.0f)
            )
            val modelPartBuilder =
                ModelPartBuilder.create().uv(0, 18).cuboid(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f)
            modelPartData.addChild("right_hind_leg", modelPartBuilder, ModelTransform.pivot(-2.5f, 16.0f, 7.0f))
            modelPartData.addChild("left_hind_leg", modelPartBuilder, ModelTransform.pivot(0.5f, 16.0f, 7.0f))
            modelPartData.addChild("right_front_leg", modelPartBuilder, ModelTransform.pivot(-2.5f, 16.0f, -4.0f))
            modelPartData.addChild("left_front_leg", modelPartBuilder, ModelTransform.pivot(0.5f, 16.0f, -4.0f))
            val modelPartData3 = modelPartData.addChild(
                "tail",
                ModelPartBuilder.create(),
                ModelTransform.of(-1.0f, 12.0f, 8.0f, 0.62831855f, 0.0f, 0.0f)
            )
            modelPartData3.addChild(
                REAL_TAIL,
                ModelPartBuilder.create().uv(9, 18).cuboid(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f),
                ModelTransform.NONE
            )
            return TexturedModelData.of(modelData, 64, 32)
        }
    }
}