package org.teamvoided.dusk_debris.entity.skeleton.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SkeletonEntityModel
import org.teamvoided.dusk_debris.entity.GloomEntity

@Environment(EnvType.CLIENT)
class GloomEntityModel(modelPart: ModelPart) : SkeletonEntityModel<GloomEntity>(modelPart) {
//    private val mushrooms: ModelPart = modelPart.getChild("head").getChild("mushrooms")

    override fun animateModel(gloomEntity: GloomEntity, f: Float, g: Float, h: Float) {
//        mushrooms.visible = !gloomEntity.isSheared
        super.animateModel(gloomEntity, f, g, h)
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = getModelData(Dilation.NONE, 0.0f)
                val modelPartData = modelData.root
                addLimbs(modelPartData)
                val modelPartBandana =
                    modelPartData.getChild("head")
                        .addChild("bandana_extra", ModelPartBuilder.create(), ModelTransform.NONE)
                modelPartBandana.addChild(
                    "bandana_1",
                    ModelPartBuilder.create().uv(0, 0).cuboid(
                        -3.0f,
                        -3.0f,
                        0.0f,
                        4.0f,
                        8.0f,
                        0.0f
                    ),
                    ModelTransform.of(
                        3.0f,
                        -8.0f,
                        3.0f,
                        0.0f,
                        0.75f,
                        0.0f
                    )
                )
                modelPartBandana.addChild(
                    "bandana_2",
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(
                            -3.0f,
                            -3.0f,
                            0.0f,
                            4.0f,
                            8.0f,
                            0.0f
                        ),
                    ModelTransform.of(
                        3.0f,
                        -8.0f,
                        3.0f,
                        0.0f,
                        2.356f,
                        0.0f
                    )
                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_1",
//                    ModelPartBuilder.create().uv(50, 22).cuboid(-3.0f, -3.0f, 0.0f, 6.0f, 4.0f, 0.0f),
//                    ModelTransform.of(-3.0f, -8.0f, -3.0f, 0.0f, 0.785f, 0.0f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_2",
//                    ModelPartBuilder.create().uv(50, 22).cuboid(-3.0f, -3.0f, 0.0f, 6.0f, 4.0f, 0.0f),
//                    ModelTransform.of(-3.0f, -8.0f, -3.0f, 0.0f, 2.356f, 0.0f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_3",
//                    ModelPartBuilder.create().uv(50, 28).cuboid(-3.0f, -4.0f, 0.0f, 6.0f, 4.0f, 0.0f),
//                    ModelTransform.of(-2.0f, -1.0f, 4.0f, -1.57f, 0.0f, 0.785f)
//                )
//                modelPartBandana.addChild(
//                    "brown_mushroom_4",
//                    ModelPartBuilder.create().uv(50, 28).cuboid(-3.0f, -4.0f, 0.0f, 6.0f, 4.0f, 0.0f),
//                    ModelTransform.of(-2.0f, -1.0f, 4.0f, -1.57f, 0.0f, 2.356f)
//                )
                return TexturedModelData.of(modelData, 64, 32)
            }
    }
}