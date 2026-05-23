package org.teamvoided.dusk_debris.entity.magic.vengeful_spirit

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.Entity

class VengefulSpiritModel(val root: ModelPart) : HierarchicalModel<Entity>() {
    private val bone: ModelPart = root.getChild("bone")
    override fun setupAnim(
        entity: Entity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val mult = if (animationProgress < 10) animationProgress / 10f else 1f
        this.bone.yRot = -headYaw
        this.bone.xRot = -headPitch
        this.bone.y = 8 * entity.bbHeight
        this.bone.xScale = 2 * entity.bbWidth * mult
        this.bone.yScale = 2 * entity.bbHeight * mult
        this.bone.zScale = 2 * entity.bbWidth * mult
    }

    override fun renderToBuffer(
        matrices: PoseStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        overlay: Int,
        color: Int
    ) = root.render(matrices, vertexConsumer, light, overlay, color)

    override fun root(): ModelPart = this.root

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val bone = modelPartData.addOrReplaceChild(
                    "bone",
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                            -4f, -4f, -4f,
                            8f, 8f, 8f
                        ),
                    PartPose.offset(0f, 4f, 0f)
                )
                return LayerDefinition.create(modelData, 32, 16)
            }
    }
}