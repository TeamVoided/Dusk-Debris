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

class TinyEnemyJellyfishModel(private val root: ModelPart) :
    HierarchicalModel<TinyEnemyJellyfishEntity>(RenderType::entityTranslucent) {
    val jellyfish = root.getChild("jellyfish")
    val membrane = jellyfish.getChild("membrane")
    val membraneExtra = membrane.getChild("membrane_extra")
    val tendrilsNorth = jellyfish.getChild("tendrils_north")
    val tendrilsWest = jellyfish.getChild("tendrils_west")
    val tendrilsSouth = jellyfish.getChild("tendrils_south")
    val tendrilsEast = jellyfish.getChild("tendrils_east")

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
                val membrane = jellyfish.addOrReplaceChild(
                    "membrane", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                            -4.0F, -8.0F, -4.0F,
                            8.0F, 8.0F, 8.0F
                        ),
                    PartPose.offset(0.0F, 0.0F, 0.0F)
                )
                val membraneExtra = membrane.addOrReplaceChild(
                    "membrane_extra", CubeListBuilder.create()
                        .texOffs(1, 16)
                        .addBox(
                            -3.0F, 0.0F, -3.0F,
                            6.0F, 2.0F, 6.0F
                        ),
                    PartPose.offset(0.0F, 0.0F, 0.0F)
                )
                val tendrilsNorth = jellyfish.addOrReplaceChild(
                    "tendrils_north", CubeListBuilder.create()
                        .texOffs(13, 24)
                        .addBox(
                            -3.0F, 0.0F, 2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    PartPose.offset(0.0F, 0.0F, -4.0F)
                )
                val tendrilsWest = jellyfish.addOrReplaceChild(
                    "tendrils_west", CubeListBuilder.create()
                        .texOffs(13, 18)
                        .addBox(
                            -2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    PartPose.offset(4.0F, 0.0F, 0.0F)
                )
                val tendrilsSouth = jellyfish.addOrReplaceChild(
                    "tendrils_south", CubeListBuilder.create()
                        .texOffs(1, 24)
                        .addBox(
                            -3.0F, 0.0F, -2.0F,
                            6.0F, 6.0F, 0.0F
                        ),
                    PartPose.offset(0.0F, 0.0F, 4.0F)
                )
                val tendrilsEast = jellyfish.addOrReplaceChild(
                    "tendrils_east", CubeListBuilder.create()
                        .texOffs(1, 18)
                        .addBox(
                            2.0F, 0.0F, -3.0F,
                            0.0F, 6.0F, 6.0F
                        ),
                    PartPose.offset(-4.0F, 0.0F, 0.0F)
                )
                return LayerDefinition.create(modelData, 32, 32)
            }
    }
}
