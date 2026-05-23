package org.teamvoided.dusk_debris.entity.dice.render

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import org.teamvoided.dusk_debris.entity.DiceEntity
import java.lang.Float.min

class DiceEntityModel(root: ModelPart) : HierarchicalModel<DiceEntity>() {
    private val root: ModelPart = root.getChild("die")

    override fun root(): ModelPart = this.root

    override fun setupAnim(
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
        root.xRot = entity.rotationVec.x
        root.zRot = entity.rotationVec.z
        root.yRot = entity.rotationVec.y
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData: PartDefinition = modelData.root
                modelPartData.addOrReplaceChild(
                    "die",
                    CubeListBuilder.create()
                        .texOffs(0, 0).addBox(
                            -4.0f, -4.0f, -4.0f,
                            8.0f, 8.0f, 8.0f
                        ),
                    PartPose.offset(0.0f, 4.0f, 0.0f)
                )
                return LayerDefinition.create(modelData, 32, 16)
            }
    }
}