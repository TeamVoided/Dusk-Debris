package org.teamvoided.dusk_debris.entity.bird.render

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.*
import net.minecraft.client.renderer.RenderType
import org.teamvoided.dusk_debris.entity.BirdEntity

class BirdEntityModel(root: ModelPart) :
    HierarchicalModel<BirdEntity>(RenderType::entityTranslucent) {
    private val bone: ModelPart = root.getChild("bone")
    private val block: ModelPart = bone.getChild("block")

    override fun setupAnim(
        entity: BirdEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        block.yRot = headYaw * 0.017453292F;
        block.xRot = headPitch * 0.017453292F;
    }

    override fun root(): ModelPart = this.bone

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData: PartDefinition = modelData.root
                val modelPartData2: PartDefinition = modelPartData.addOrReplaceChild(
                    "bone",
                    CubeListBuilder.create(),
                    PartPose.offset(0f, 16f, 0f)
                )
                modelPartData2.addOrReplaceChild(
                    "block",
                    CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0f, -2.0f, -2.0f, 4.0f, 10.0f, 4.0f, CubeDeformation.NONE),
                    PartPose.offset(0f, 0f, 0f)
                ).addOrReplaceChild(
                    "beak",
                    CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0f, 0.0f, -4.0f, 2.0f, 2.0f, 2.0f, CubeDeformation.NONE),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )
                return LayerDefinition.create(modelData, 16, 16)
            }
    }
}