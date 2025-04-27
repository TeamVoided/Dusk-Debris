package org.teamvoided.dusk_debris.entity.magic.vengeful_spirit

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.scale
import org.teamvoided.dusk_debris.util.sendMessageIngame

class VengefulSpiritModel(val root: ModelPart) : SinglePartEntityModel<Entity>() {
    val bone = root.getChild("bone")
    override fun setAngles(
        entity: Entity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        this.bone.yaw = -headYaw
        this.bone.pitch = -headPitch
        this.bone.pivotY = 8 * entity.height
        this.bone.scaleX = 2 * entity.width
        this.bone.scaleY = 2 * entity.height
        this.bone.scaleZ = 2 * entity.width
        sendMessageIngame(bone.pivotY.toString())
    }

    override fun method_2828(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        overlay: Int,
        color: Int
    ) {
        root.method_22699(matrices, vertexConsumer, light, overlay, color);
    }

    override fun getPart(): ModelPart = this.root

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "bone",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                            -4f, -4f, -4f,
                            8f, 8f, 8f
                        ),
                    ModelTransform.pivot(0f, 4f, 0f)
                )
                return TexturedModelData.of(modelData, 32, 16)
            }
    }
}