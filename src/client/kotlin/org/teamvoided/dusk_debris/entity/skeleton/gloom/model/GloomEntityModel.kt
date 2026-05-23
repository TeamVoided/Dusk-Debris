package org.teamvoided.dusk_debris.entity.skeleton.gloom.model

import net.minecraft.client.model.SkeletonModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.util.Utils.rotate225
import org.teamvoided.dusk_debris.util.Utils.rotate315

class GloomEntityModel(modelPart: ModelPart) : SkeletonModel<GloomEntity>(modelPart) {
//    private val mushrooms: ModelPart = modelPart.getChild("head").getChild("mushrooms")

    override fun prepareMobModel(gloomEntity: GloomEntity, f: Float, g: Float, h: Float) {
//        mushrooms.visible = !gloomEntity.isSheared
        super.prepareMobModel(gloomEntity, f, g, h)
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = createMesh(CubeDeformation.NONE, 0f)
                val modelPartData = modelData.root
                createDefaultSkeletonMesh(modelPartData)
                val modelPartBandana =
                    modelPartData.getChild("head")
                        .addOrReplaceChild("bandana_extra", CubeListBuilder.create(), PartPose.ZERO)
                modelPartBandana.addOrReplaceChild(
                    "bandana_1",
                    CubeListBuilder.create().texOffs(56, 16).addBox(
                        0f,
                        0f,
                        0f,
                        4f,
                        8f,
                        0f,
                        CubeDeformation(0.25f, 0.25f, 0f)
                    ),
                    PartPose.offsetAndRotation(
                        0f,
                        -8f,
                        4.25f,
                        0f,
                        rotate225,
                        0f
                    )
                )
                modelPartBandana.addOrReplaceChild(
                    "bandana_2",
                    CubeListBuilder.create().texOffs(56, 24)
                        .addBox(
                            0f,
                            0f,
                            0f,
                            4f,
                            8f,
                            0f,
                            CubeDeformation(0.25f, 0.25f, 0f)
                        ),
                    PartPose.offsetAndRotation(
                        0f,
                        -8f,
                        4.25f,
                        0f,
                        rotate315,
                        0f
                    )
                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_1",
//                    ModelPartBuilder.create().uv(50, 22).cuboid(-3f, -3f, 0f, 6f, 4f, 0f),
//                    ModelTransform.of(-3f, -8f, -3f, 0f, 0.785f, 0f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_2",
//                    ModelPartBuilder.create().uv(50, 22).cuboid(-3f, -3f, 0f, 6f, 4f, 0f),
//                    ModelTransform.of(-3f, -8f, -3f, 0f, 2.356f, 0f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_3",
//                    ModelPartBuilder.create().uv(50, 28).cuboid(-3f, -4f, 0f, 6f, 4f, 0f),
//                    ModelTransform.of(-2f, -1f, 4f, -1.57f, 0f, 0.785f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_4",
//                    ModelPartBuilder.create().uv(50, 28).cuboid(-3f, -4f, 0f, 6f, 4f, 0f),
//                    ModelTransform.of(-2f, -1f, 4f, -1.57f, 0f, 2.356f)
//                )
                return LayerDefinition.create(modelData, 64, 32)
            }
    }
}