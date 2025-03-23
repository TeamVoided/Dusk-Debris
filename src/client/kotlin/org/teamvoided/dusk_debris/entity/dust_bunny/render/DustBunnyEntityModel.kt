package org.teamvoided.dusk_debris.entity.dust_bunny.render

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.ModelWithArms
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Arm
import org.teamvoided.dusk_debris.entity.DustBunnyEntity

class DustBunnyEntityModel(root: ModelPart) : SinglePartEntityModel<DustBunnyEntity>(), ModelWithArms {
    private val root: ModelPart = root.getChild("center")

    override fun getPart(): ModelPart = this.root

    override fun setAngles(
        entity: DustBunnyEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        root.yaw = headYaw
        root.pitch = headPitch
    }

    override fun setArmAngle(arm: Arm, matrices: MatrixStack) {
        root.rotate(matrices)
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData: ModelPartData = modelData.root
                modelPartData.addChild(
                    "center", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(
                            0f, 0f, 0f,
                            0f, 0f, 0f
                        ),
                    ModelTransform.pivot(0.0f, 16.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 16, 16)
            }
    }
}