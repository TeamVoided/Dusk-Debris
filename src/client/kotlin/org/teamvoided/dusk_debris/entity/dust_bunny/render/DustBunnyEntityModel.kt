package org.teamvoided.dusk_debris.entity.dust_bunny.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.ArmedModel
import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.world.entity.HumanoidArm
import org.teamvoided.dusk_debris.entity.DustBunnyEntity

class DustBunnyEntityModel(root: ModelPart) : HierarchicalModel<DustBunnyEntity>(), ArmedModel {
    private val root: ModelPart = root.getChild("center")

    override fun root(): ModelPart = this.root

    override fun setupAnim(
        entity: DustBunnyEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        root.yRot = headYaw
        root.xRot = headPitch
    }

    override fun translateToHand(arm: HumanoidArm, matrices: PoseStack) {
        root.translateAndRotate(matrices)
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData: PartDefinition = modelData.root
                modelPartData.addOrReplaceChild(
                    "center", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(
                            0f, 0f, 0f,
                            0f, 0f, 0f
                        ),
                    PartPose.offset(0.0f, 16.0f, 0.0f)
                )
                return LayerDefinition.create(modelData, 16, 16)
            }
    }
}