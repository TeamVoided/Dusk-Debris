package org.teamvoided.dusk_debris.entity.block.treasure_chest

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

class TreasureChestBlockEntityModel(root: ModelPart) : Model(RenderLayer::getEntitySolid) {
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

    override fun method_2828(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        overlay: Int,
        color: Int
    ) {
//        singleChestLid.method_22699(matrices, vertexConsumer, light, overlay, color)
//        singleChestBase.method_22699(matrices, vertexConsumer, light, overlay, color)
//        singleChestLocks.method_22699(matrices, vertexConsumer, light, overlay, color)
    }

    companion object {
        const val BASE = "bottom"
        const val LID = "lid"
        const val LATCH = "lock"

        val singleTexturedModelData: TexturedModelData
            get() {
                val meshdefinition = ModelData()
                val partdefinition = meshdefinition.root

                val base = partdefinition.addChild(
                    BASE,
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-7.0f, -2.0f, -7.0f, 14.0f, 10.0f, 14.0f, Dilation(-0.001f)),
                    ModelTransform.pivot(0.0f, 16.0f, 0.0f)
                )

                val lid = partdefinition.addChild(
                    LID,
                    ModelPartBuilder.create().uv(0, 24)
                        .cuboid(-7.0f, -6.0f, -7.0f, 14.0f, 5.0f, 14.0f)
                        .uv(0, 0).cuboid(2.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f)
                        .uv(0, 0).cuboid(-4.0f, -3.0f, -8.0f, 2.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 16.0f, 0.0f)
                )

                val locks = partdefinition.addChild(
                    LATCH,
                    ModelPartBuilder.create().uv(0, 5)
                        .cuboid(-13.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f)
                        .uv(0, 5).cuboid(-7.0f, -9.0f, 0.5f, 4.0f, 7.0f, 0.0f),
                    ModelTransform.pivot(8.0f, 24.0f, -8.0f)
                )

                return TexturedModelData.of(meshdefinition, 64, 64)
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

        val rightDoubleTexturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    BASE,
                    ModelPartBuilder.create()
                        .uv(0, 19)
                        .cuboid(1.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    LID,
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(1.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addChild(
                    LATCH,
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(15.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }

        val leftDoubleTexturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    BASE,
                    ModelPartBuilder.create().uv(0, 19).cuboid(0.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    LID,
                    ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addChild(
                    LATCH,
                    ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}