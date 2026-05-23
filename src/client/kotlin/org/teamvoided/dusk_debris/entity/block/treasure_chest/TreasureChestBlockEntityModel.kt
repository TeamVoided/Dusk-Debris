package org.teamvoided.dusk_debris.entity.block.treasure_chest

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.entity.Entity

class TreasureChestBlockEntityModel(root: ModelPart) : Model(RenderType::entitySolid) {
    private val singleChestLid: ModelPart = root.getChild("lid")
    private val singleChestBase: ModelPart = root.getChild("base")
    private val singleChestLocks: ModelPart = root.getChild("locks")
    private val doubleChestRightLid: ModelPart = root.getChild("lid")
    private val doubleChestRightBase: ModelPart = root.getChild("base")
    private val doubleChestRightLocks: ModelPart = root.getChild("locks")
    private val doubleChestLeftLid: ModelPart = root.getChild("lid")
    private val doubleChestLeftBase: ModelPart = root.getChild("base")
    private val doubleChestLeftLocks: ModelPart = root.getChild("locks")

    fun setupAnim(
        entity: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {

    }

    override fun renderToBuffer(
        matrices: PoseStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        overlay: Int,
        color: Int
    ) {
//        singleChestLid.render(matrices, vertexConsumer, light, overlay, color)
//        singleChestBase.render(matrices, vertexConsumer, light, overlay, color)
//        singleChestLocks.render(matrices, vertexConsumer, light, overlay, color)
    }

    companion object {
        const val BASE = "bottom"
        const val LID = "lid"
        const val LATCH = "lock"

        val singleTexturedModelData: LayerDefinition
            get() {
                val meshdefinition = MeshDefinition()
                val partdefinition = meshdefinition.root

                val base = partdefinition.addOrReplaceChild(
                    BASE,
                    CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.0f, -2.0f, -7.0f, 14.0f, 10.0f, 14.0f, CubeDeformation(-0.001f)),
                    PartPose.offset(0.0f, 16.0f, 0.0f)
                )

                val lid = partdefinition.addOrReplaceChild(
                    LID,
                    CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-7.0f, -6.0f, -7.0f, 14.0f, 5.0f, 14.0f)
                        .texOffs(0, 0).addBox(2.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f)
                        .texOffs(0, 0).addBox(-4.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f),
                    PartPose.offset(0.0f, 16.0f, 0.0f)
                )

                val locks = partdefinition.addOrReplaceChild(
                    LATCH,
                    CubeListBuilder.create().texOffs(0, 5)
                        .addBox(-13.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f)
                        .texOffs(0, 5).addBox(-7.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f),
                    PartPose.offset(8.0f, 24.0f, -8.0f)
                )

                return LayerDefinition.create(meshdefinition, 64, 64)
            }

        //copied from vanilla
//        val singleTexturedModelData: TexturedModelData
//            get() {
//                val modelData = ModelData()
//                val modelPartData = modelData.root
//                modelPartData.addChild(
//                    "bottom",
//                    ModelPartBuilder.create().uv(0, 19).cuboid(1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f),
//                    ModelTransform.NONE
//                )
//                modelPartData.addChild(
//                    "lid",
//                    ModelPartBuilder.create().uv(0, 0).cuboid(1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f),
//                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
//                )
//                modelPartData.addChild(
//                    "lock",
//                    ModelPartBuilder.create().uv(0, 0).cuboid(7.0f, -2.0f, 14.0f, 2.0f, 4.0f, 1.0f),
//                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
//                )
//                return TexturedModelData.of(modelData, 64, 64)
//            }

        val rightDoubleTexturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                modelPartData.addOrReplaceChild(
                    BASE,
                    CubeListBuilder.create()
                        .texOffs(0, 19)
                        .addBox(1.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    PartPose.ZERO
                )
                modelPartData.addOrReplaceChild(
                    LID,
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    PartPose.offset(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addOrReplaceChild(
                    LATCH,
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(15.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    PartPose.offset(0.0f, 9.0f, 1.0f)
                )
                return LayerDefinition.create(modelData, 64, 64)
            }

        val leftDoubleTexturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                modelPartData.addOrReplaceChild(
                    BASE,
                    CubeListBuilder.create().texOffs(0, 19).addBox(0.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    PartPose.ZERO
                )
                modelPartData.addOrReplaceChild(
                    LID,
                    CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    PartPose.offset(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addOrReplaceChild(
                    LATCH,
                    CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    PartPose.offset(0.0f, 9.0f, 1.0f)
                )
                return LayerDefinition.create(modelData, 64, 64)
            }
    }
}