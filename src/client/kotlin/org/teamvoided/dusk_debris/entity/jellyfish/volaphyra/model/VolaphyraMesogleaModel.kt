package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model

import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation.VolaphyraEntityAnimations

class VolaphyraMesogleaModel(private val root: ModelPart) :
    SinglePartEntityModel<AbstractVolaphyraEntity>(RenderLayer::getEntityTranslucent) {
    val mesoglea: ModelPart = root.getChild(MESOGLEA)
    val mesogleaLower: ModelPart = mesoglea.getChild(MESOGLEA_LOWER)
    val armsNorth: ModelPart = mesoglea.getChild(ARMS_NORTH)
    val armsSouth: ModelPart = mesoglea.getChild(ARMS_SOUTH)
    val armsEast: ModelPart = mesoglea.getChild(ARMS_EAST)
    val armsWest: ModelPart = mesoglea.getChild(ARMS_WEST)
    val armsNorthLower: ModelPart = armsNorth.getChild(ARMS_NORTH_LOWER)
    val armsSouthLower: ModelPart = armsSouth.getChild(ARMS_SOUTH_LOWER)
    val armsEastLower: ModelPart = armsEast.getChild(ARMS_EAST_LOWER)
    val armsWestLower: ModelPart = armsWest.getChild(ARMS_WEST_LOWER)

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        entity: AbstractVolaphyraEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.part.traverse().forEach(ModelPart::resetTransform)
        this.animate(entity.idleAnimationState, VolaphyraEntityAnimations.IDLE, animationProgress, 1.0f)
        animateArms(
            limbAngle,
            limbDistance,
            animationProgress,
            armsNorth,
            armsSouth,
            armsEast,
            armsWest
        )
    }

    companion object {
        const val MESOGLEA: String = "mesoglea"
        const val MESOGLEA_LOWER: String = "mesoglea_lower"
        const val ARMS_NORTH: String = "arms_north"
        const val ARMS_SOUTH: String = "arms_south"
        const val ARMS_EAST: String = "arms_east"
        const val ARMS_WEST: String = "arms_west"
        const val ARMS_NORTH_LOWER: String = "arms_north_lower"
        const val ARMS_SOUTH_LOWER: String = "arms_south_lower"
        const val ARMS_EAST_LOWER: String = "arms_east_lower"
        const val ARMS_WEST_LOWER: String = "arms_west_lower"

        fun animateArms(
            limbAngle: Float,
            limbDistance: Float,
            animationProgress: Float,
            north: ModelPart,
            south: ModelPart,
            east: ModelPart,
            west: ModelPart,
            speed: Float = 0.4f
        ) {
            val value: Float = animationProgress * 0.1f + limbAngle * 0.5f
            val mult: Float = 0.08f + limbDistance * speed
            north.pitch += -MathHelper.cos(value * 0.5f) * mult
            south.pitch += MathHelper.cos(value * 0.55f) * mult
            east.roll += MathHelper.cos(value * 0.6f) * mult
            west.roll += -MathHelper.cos(value * 0.65f) * mult
        }

        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val mesoglea = modelPartData.addChild(
                    MESOGLEA,
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-8f, -16f, -8f, 16f, 16f, 16f),
                    ModelTransform.pivot(0f, 24f, 0f)
                )
                mesoglea.addChild(
                    MESOGLEA_LOWER,
                    ModelPartBuilder.create()
                        .uv(1, 32)
                        .cuboid(-7f, 0f, -7f, 14f, 4f, 14f),
                    ModelTransform.pivot(0f, 0f, 0f)
                )

                val armsNorth = mesoglea.arms(ARMS_NORTH, Direction.NORTH)
                val armsSouth = mesoglea.arms(ARMS_SOUTH, Direction.SOUTH)
                val armsEast = mesoglea.arms(ARMS_EAST, Direction.EAST)
                val armsWest = mesoglea.arms(ARMS_WEST, Direction.WEST)
                armsNorth.arms(ARMS_NORTH_LOWER, Direction.NORTH, true)
                armsSouth.arms(ARMS_SOUTH_LOWER, Direction.SOUTH, true)
                armsEast.arms(ARMS_EAST_LOWER, Direction.EAST, true)
                armsWest.arms(ARMS_WEST_LOWER, Direction.WEST, true)
                return TexturedModelData.of(modelData, 64, 128)
            }

        private fun ModelPartData.arms(
            tendril: String, direction: Direction, bottom: Boolean = false
        ): ModelPartData {
            val modelPart = if (direction.axis == Direction.Axis.Z) {
                ModelPartBuilder.create()
                    .uv(2, if (bottom) 66 else 50)
                    .cuboid(-6f, 0f, 0f, 12f, 16f, 0f)
            } else {
                ModelPartBuilder.create()
                    .uv(34, if (bottom) 54 else 38)
                    .cuboid(0f, 0f, -6f, 0f, 16f, 12f)
            }
            val pivot = if (bottom) {
                ModelTransform.pivot(0f, 16f, 0f)
            } else {
                ModelTransform.pivot(direction.vector.x * -4f, 0f, direction.vector.z * 4f)
            }
            return this.addChild(tendril, modelPart, pivot)
        }
    }
}