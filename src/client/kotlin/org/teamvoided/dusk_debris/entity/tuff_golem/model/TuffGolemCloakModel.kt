package org.teamvoided.dusk_debris.entity.tuff_golem.model


import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.animation.TuffGolemEntityAnimations

class TuffGolemCloakModel(private val root: ModelPart) : HierarchicalModel<TuffGolemEntity>() {
    val body: ModelPart = root.getChild("body")
    val cloak_item: ModelPart = body.getChild("cloak_item")
    val cloak_no_item: ModelPart = body.getChild("cloak_no_item")

    override fun setupAnim(
        tuffGolemEntity: TuffGolemEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
        this.animateWalk(TuffGolemEntityAnimations.WALK, limbAngle, limbDistance, 16.5f, 2.5f)
        this.animate(tuffGolemEntity.risingAnimationState, TuffGolemEntityAnimations.ARISE, animationProgress, 1.0f)
        this.animate(tuffGolemEntity.statueAnimationState, TuffGolemEntityAnimations.STATUE, animationProgress, 1.0f)

        cloak_item.visible = tuffGolemEntity.isHoldingItem()
        cloak_no_item.visible = !tuffGolemEntity.isHoldingItem()
    }

    override fun root(): ModelPart {
        return this.root
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val body = modelPartData.addOrReplaceChild(
                    "body",
                    CubeListBuilder.create(),
                    PartPose.offset(0f, 20f, 0f)
                )
                body.addOrReplaceChild(
                    "cloak_no_item",
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                            -5f, -5f, -4f,
                            10f, 8f, 8f,
                            CubeDeformation(0.5f)
                        ),
                    PartPose.offset(0f, 0f, 0f)
                )
                body.addOrReplaceChild(
                    "cloak_item",
                    CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                            -5f, -5f, -9f,
                            10f, 8f, 13f,
                            CubeDeformation(0.5f)
                        ),
                    PartPose.offset(0f, 0f, 0f)
                )
                return LayerDefinition.create(modelData, 64, 64)
            }
    }
}