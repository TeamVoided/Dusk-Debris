package org.teamvoided.dusk_debris.entity.jellyfish.tiny.model

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.RenderType
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.animation.TinyEnemyJellyfishAnimations

class TinyEnemyJellyfishCoreModel(private val root: ModelPart) :
    HierarchicalModel<TinyEnemyJellyfishEntity>(RenderType::entityTranslucent) {
    val jellyfish = root.getChild("jellyfish")
    val core = jellyfish.getChild("core")

    override fun root(): ModelPart {
        return this.root
    }

    override fun setupAnim(
        entity: TinyEnemyJellyfishEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
        this.animate(entity.idleAnimationState, TinyEnemyJellyfishAnimations.IDLE, animationProgress, 1.0f)
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val jellyfish = modelPartData.addOrReplaceChild(
                    "jellyfish", CubeListBuilder.create(),
                    PartPose.offset(0.0F, 24.0F, 0.0F)
                )
                val core = jellyfish.addOrReplaceChild(
                    "core", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                            -2.0F, -6.0F, -2.0F,
                            4.0F, 4.0F, 4.0F
                        ),
                    PartPose.offset(0.0F, 0.0F, 0.0F)
                )
                return LayerDefinition.create(modelData, 16, 16)
            }
    }
}
