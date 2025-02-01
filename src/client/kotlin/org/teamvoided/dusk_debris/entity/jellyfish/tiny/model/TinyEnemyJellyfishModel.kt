package org.teamvoided.dusk_debris.entity.jellyfish.tiny.model

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity

class TinyEnemyJellyfishModel(private val root: ModelPart) :
    SinglePartEntityModel<TinyEnemyJellyfishEntity>(RenderLayer::getEntityTranslucent) {
    val root1 = root.getChild("root")
    val membrane = root.getChild("membrane")
    val membraneExtra = root.getChild("membrane_extra")
    val tendrilsNorth = root.getChild("tendrils_north")
    val tendrilsWest = root.getChild("tendrils_west")
    val tendrilsSouth = root.getChild("tendrils_south")
    val tendrilsEast = root.getChild("tendrils_east")

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        entity: TinyEnemyJellyfishEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {

    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val root =
                    modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F))

                val membrane = root.addChild(
                    "membrane", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                            -4.0F, -8.0F, -4.0F,
                            8.0F, 8.0F, 8.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )

                val membraneExtra = membrane.addChild(
                    "membrane_extra", ModelPartBuilder.create()
                        .uv(1, 16)
                        .cuboid(
                            -3.0F, 0.0F, -3.0F,
                            6.0F, 2.0F, 6.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )

                val tendrilsNorth = root.addChild(
                    "tendrils_north", ModelPartBuilder.create()
                        .uv(13, 24)
                        .cuboid(
                            -3.0F, 0.0F, 2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, -4.0F)
                )

                val tendrilsWest = root.addChild(
                    "tendrils_west", ModelPartBuilder.create()
                        .uv(13, 18)
                        .cuboid(
                            -2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    ModelTransform.pivot(4.0F, 0.0F, 0.0F)
                )

                val tendrilsSouth = root.addChild(
                    "tendrils_south", ModelPartBuilder.create()
                        .uv(1, 24)
                        .cuboid(
                            -3.0F, 0.0F, -2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, 4.0F)
                )

                val tendrilsEast = root.addChild(
                    "tendrils_east", ModelPartBuilder.create()
                        .uv(1, 18)
                        .cuboid(
                            2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    ModelTransform.pivot(-4.0F, 0.0F, 0.0F)
                )
                return TexturedModelData.of(modelData, 32, 32);
            }
    }
}
