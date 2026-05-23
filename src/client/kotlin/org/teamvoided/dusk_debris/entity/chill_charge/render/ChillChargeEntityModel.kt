package org.teamvoided.dusk_debris.entity.chill_charge.render

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.*
import net.minecraft.client.renderer.RenderType
import org.teamvoided.dusk_debris.entity.ChillChargeEntity

class ChillChargeEntityModel(root: ModelPart) :
    HierarchicalModel<ChillChargeEntity>(RenderType::entityTranslucent) {
    private val bone: ModelPart = root.getChild("bone")
    private val chillCharge: ModelPart = bone.getChild("chill_charge")

    private val chill: ModelPart = bone.getChild("chill")

    override fun setupAnim(
        entity: ChillChargeEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        chillCharge.yRot = animationProgress * rotationSpeed * 0.0175f
        chill.yRot = animationProgress * rotationSpeed * 0.0175f
    }

    override fun root(): ModelPart = this.bone


    companion object {
        private const val rotationSpeed = 16f
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData: PartDefinition = modelData.root
                val modelPartData2: PartDefinition = modelPartData.addOrReplaceChild(
                    "bone",
                    CubeListBuilder.create(),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )
                modelPartData2.addOrReplaceChild(
                    "chill",
                    CubeListBuilder.create()
                        .texOffs(0, 9).addBox(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, CubeDeformation(0.25f)),
//                        .uv(15, 20).cuboid(-4.0f, -1.0f, -4.0f, 8.0f, 2.0f, 8.0f, Dilation(0.0f))
//                        .uv(0, 9).cuboid(-3.0f, -2.0f, -3.0f, 6.0f, 4.0f, 6.0f, Dilation(0.0f)),
                    PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, -0.7854f, 0.0f)
                )
                modelPartData2.addOrReplaceChild(
                    "chill_charge",
                    CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )
                return LayerDefinition.create(modelData, 16, 16)
            }
    }
}