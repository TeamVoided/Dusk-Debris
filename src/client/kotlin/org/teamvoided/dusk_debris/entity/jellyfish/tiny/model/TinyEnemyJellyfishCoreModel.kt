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

class TinyEnemyJellyfishCoreModel(private val root: ModelPart) :
    SinglePartEntityModel<TinyEnemyJellyfishEntity>(RenderLayer::getEntityTranslucent) {
    val jellyfish = root.getChild("jellyfish")
    val core = jellyfish.getChild("core")

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
                val core = jellyfish.addChild(
                    "core", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                            -2.0F, -6.0F, -2.0F,
                            4.0F, 4.0F, 4.0F
                        ),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )
                return TexturedModelData.of(modelData, 16, 16)
            }
    }
}
