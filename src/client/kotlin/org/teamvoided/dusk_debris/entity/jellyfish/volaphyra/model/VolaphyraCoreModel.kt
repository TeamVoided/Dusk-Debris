package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model


import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation.VolaphyraEntityAnimations
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_EAST
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_NORTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_SOUTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_WEST
import org.teamvoided.dusk_debris.util.Utils.rotate45

class VolaphyraCoreModel(val root: ModelPart, val offset: Float = 0f) :
    SinglePartEntityModel<AbstractVolaphyraEntity>() {
    val core: ModelPart = root.getChild("core")
    var manubrium: ModelPart = core.getChild("manubrium")
    var armsNorth: ModelPart = core.getChild(ARMS_NORTH)
    var armsSouth: ModelPart = core.getChild(ARMS_SOUTH)
    var armsEast: ModelPart = core.getChild(ARMS_EAST)
    var armsWest: ModelPart = core.getChild(ARMS_WEST)

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        entity: AbstractVolaphyraEntity, limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.part.traverse().forEach(ModelPart::resetTransform)
        if (offset != 0f) {
            core.setPivot(core.pivotX, core.pivotY - offset, core.pivotZ)
        }
        this.animate(entity.propulsionAnimationState, VolaphyraEntityAnimations.IDLE, animationProgress, 1.0f)
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
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val core = modelPartData.addChild(
                    "core",
                    ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f),
                    ModelTransform.pivot(0f, 24f, 0f)
                )
                core.addChild(
                    "manubrium",
                    ModelPartBuilder.create().uv(32, -8).cuboid(0f, -4f, -4f, 0f, 20f, 8f).uv(32, 0)
                        .cuboid(-4f, -4f, 0f, 8f, 20f, 0f),
                    ModelTransform.of(0f, 2f, 0f, 0f, -rotate45, 0f)
                )
                core.addChild(
                    ARMS_NORTH,
                    ModelPartBuilder.create().uv(0, 16).cuboid(-4f, 0f, 0f, 8f, 16f, 0f),
                    ModelTransform.pivot(0f, 0f, -3f)
                )
                core.addChild(
                    ARMS_SOUTH,
                    ModelPartBuilder.create().uv(16, 16).cuboid(-4f, 0f, 0f, 8f, 16f, 0f),
                    ModelTransform.pivot(0f, 0f, 3f)
                )
                core.addChild(
                    ARMS_EAST,
                    ModelPartBuilder.create().uv(16, 8).cuboid(0f, 0f, -4f, 0f, 16f, 8f),
                    ModelTransform.pivot(-3f, 0f, 0f)
                )
                core.addChild(
                    ARMS_WEST,
                    ModelPartBuilder.create().uv(0, 8).cuboid(0f, 0f, -4f, 0f, 16f, 8f),
                    ModelTransform.pivot(3f, 0f, 0f)
                )
                return TexturedModelData.of(modelData, 64, 32)
            }
    }
}