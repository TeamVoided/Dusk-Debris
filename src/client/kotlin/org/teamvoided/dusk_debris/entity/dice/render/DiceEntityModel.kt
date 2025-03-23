package org.teamvoided.dusk_debris.entity.dice.render

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.DiceEntity
import java.lang.Float.min

class DiceEntityModel(root: ModelPart) : SinglePartEntityModel<DiceEntity>() {
    private val root: ModelPart = root.getChild("die")

    override fun getPart(): ModelPart = this.root

    override fun setAngles(
        entity: DiceEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
//        root.pitch = animationProgress * entity.rotationVec.pitch
//        root.roll = animationProgress * entity.rotationVec.roll
//        root.yaw = animationProgress * entity.rotationVec.yaw

        val lerp = min(1f, entity.timeSinceLastFall.toFloat() / 100f)
        root.pitch = entity.rotationVec.pitch
        root.roll = entity.rotationVec.roll
        root.yaw = entity.rotationVec.yaw
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData: ModelPartData = modelData.root
                modelPartData.addChild(
                    "die",
                    ModelPartBuilder.create()
                        .uv(0, 0).cuboid(
                            -4.0f, -4.0f, -4.0f,
                            8.0f, 8.0f, 8.0f
                        ),
                    ModelTransform.pivot(0.0f, 4.0f, 0.0f)
                )
                return TexturedModelData.of(modelData, 32, 16)
            }
    }
}