package org.teamvoided.dusk_debris.entity.jellyfish.tiny.model

import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.animation.TinyEnemyJellyfishAnimations

class TinyEnemyJellyfishModel(private val root: ModelPart) :
    SinglePartEntityModel<TinyEnemyJellyfishEntity>(RenderLayer::getEntityTranslucent) {
    val jellyfish = root.getChild("jellyfish")
    val membrane = jellyfish.getChild("membrane")
    val membraneExtra = membrane.getChild("membrane_extra")
    val tendrilsNorth = jellyfish.getChild("tendrils_north")
    val tendrilsWest = jellyfish.getChild("tendrils_west")
    val tendrilsSouth = jellyfish.getChild("tendrils_south")
    val tendrilsEast = jellyfish.getChild("tendrils_east")

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
        this.part.traverse().forEach(ModelPart::resetTransform)
        this.animate(entity.idleAnimationState, TinyEnemyJellyfishAnimations.IDLE, animationProgress, 1.0f)
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val jellyfish = modelPartData.addChild(
                    "jellyfish", ModelPartBuilder.create(),
                    ModelTransform.pivot(0.0F, 24.0F, 0.0F)
                )
                val membrane = jellyfish.addChild(
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
                val tendrilsNorth = jellyfish.addChild(
                    "tendrils_north", ModelPartBuilder.create()
                        .uv(13, 24)
                        .cuboid(
                            -3.0F, 0.0F, 2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, -4.0F)
                )
                val tendrilsWest = jellyfish.addChild(
                    "tendrils_west", ModelPartBuilder.create()
                        .uv(13, 18)
                        .cuboid(
                            -2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    ModelTransform.pivot(4.0F, 0.0F, 0.0F)
                )
                val tendrilsSouth = jellyfish.addChild(
                    "tendrils_south", ModelPartBuilder.create()
                        .uv(1, 24)
                        .cuboid(
                            -3.0F, 0.0F, -2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, 4.0F)
                )
                val tendrilsEast = jellyfish.addChild(
                    "tendrils_east", ModelPartBuilder.create()
                        .uv(1, 18)
                        .cuboid(
                            2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    ModelTransform.pivot(-4.0F, 0.0F, 0.0F)
                )
                return TexturedModelData.of(modelData, 32, 32)
            }
    }
}
