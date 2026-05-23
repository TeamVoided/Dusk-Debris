package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model


import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation.VolaphyraEntityAnimations
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_EAST
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_NORTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_SOUTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_WEST
import org.teamvoided.dusk_debris.util.Utils.rotate45

class VolaphyraCoreModel(val root: ModelPart, val offset: Float = 0f) :
    HierarchicalModel<AbstractVolaphyraEntity>() {
    val core: ModelPart = root.getChild("core")
    var manubrium: ModelPart = core.getChild("manubrium")
    var armsNorth: ModelPart = core.getChild(ARMS_NORTH)
    var armsSouth: ModelPart = core.getChild(ARMS_SOUTH)
    var armsEast: ModelPart = core.getChild(ARMS_EAST)
    var armsWest: ModelPart = core.getChild(ARMS_WEST)

    override fun root(): ModelPart {
        return this.root
    }

    override fun setupAnim(
        entity: AbstractVolaphyraEntity, limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
        if (offset != 0f) {
            core.setPos(core.x, core.y - offset, core.z)
        }
        this.animate(entity.idleAnimationState, VolaphyraEntityAnimations.IDLE, animationProgress, 1.0f)
        VolaphyraMesogleaModel.animateArms(
            limbAngle,
            limbDistance,
            animationProgress,
            armsNorth,
            armsSouth,
            armsEast,
            armsWest,
            if (entity.target != null) 1f else 0.4f
        )
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val core = modelPartData.addOrReplaceChild(
                    "core",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-4f, -8f, -4f, 8f, 8f, 8f),
                    PartPose.offset(0f, 24f, 0f)
                )
                core.addOrReplaceChild(
                    "manubrium",
                    CubeListBuilder.create().texOffs(32, -8).addBox(0f, -4f, -4f, 0f, 20f, 8f).texOffs(32, 0)
                        .addBox(-4f, -4f, 0f, 8f, 20f, 0f),
                    PartPose.offsetAndRotation(0f, 2f, 0f, 0f, -rotate45, 0f)
                )
                core.addOrReplaceChild(
                    ARMS_NORTH,
                    CubeListBuilder.create().texOffs(0, 16).addBox(-4f, 0f, 0f, 8f, 16f, 0f),
                    PartPose.offset(0f, 0f, -3f)
                )
                core.addOrReplaceChild(
                    ARMS_SOUTH,
                    CubeListBuilder.create().texOffs(16, 16).addBox(-4f, 0f, 0f, 8f, 16f, 0f),
                    PartPose.offset(0f, 0f, 3f)
                )
                core.addOrReplaceChild(
                    ARMS_EAST,
                    CubeListBuilder.create().texOffs(16, 8).addBox(0f, 0f, -4f, 0f, 16f, 8f),
                    PartPose.offset(-3f, 0f, 0f)
                )
                core.addOrReplaceChild(
                    ARMS_WEST,
                    CubeListBuilder.create().texOffs(0, 8).addBox(0f, 0f, -4f, 0f, 16f, 8f),
                    PartPose.offset(3f, 0f, 0f)
                )
                return LayerDefinition.create(modelData, 64, 32)
            }
    }
}