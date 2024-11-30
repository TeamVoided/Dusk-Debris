package org.teamvoided.dusk_debris.entity.volaphyra.model

import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity

class VolaphyramesogleaModel(private val root: ModelPart) :
    SinglePartEntityModel<AbstractVolaphyraEntity>(RenderLayer::getEntityTranslucent) {
    val mesoglea: ModelPart = root.getChild("mesoglea")
    val mesogleaLower: ModelPart = mesoglea.getChild("mesoglea_lower")
    val armsNorth: ModelPart = mesoglea.getChild("arms_north")
    val armsSouth: ModelPart = mesoglea.getChild("arms_south")
    val armsEast: ModelPart = mesoglea.getChild("arms_east")
    val armsWest: ModelPart = mesoglea.getChild("arms_west")
    val armsNorthLower: ModelPart = armsNorth.getChild("arms_north_lower")
    val armsSouthLower: ModelPart = armsSouth.getChild("arms_south_lower")
    val armsEastLower: ModelPart = armsEast.getChild("arms_east_lower")
    val armsWestLower: ModelPart = armsWest.getChild("arms_west_lower")

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
        animatearms(
            limbAngle,
            limbDistance,
            animationProgress,
            armsNorth,
            armsSouth,
            armsEast,
            armsWest
        )
//        val l: Float = animationProgress * 0.1f + limbAngle * 0.5f
//        this.mesoglea.roll = l * 0.01f
    }

    companion object {
        fun animatearms(
            limbAngle: Float,
            limbDistance: Float,
            animationProgress: Float,
            north: ModelPart,
            south: ModelPart,
            east: ModelPart,
            west: ModelPart
        ) {
            val value: Float = animationProgress * 0.1f + limbAngle * 0.5f
            val mult: Float = 0.08f + limbDistance * 0.4f
            north.pitch = -MathHelper.cos(value * 0.1f) * mult
            south.pitch = MathHelper.cos(value * 0.15f) * mult
            east.roll = MathHelper.cos(value * 0.2f) * mult
            west.roll = -MathHelper.cos(value * 0.25f) * mult
        }

        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val mesoglea = modelPartData.addChild(
                    "mesoglea",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-8f, -16f, -8f, 16f, 16f, 16f),
                    ModelTransform.pivot(0f, 24f, 0f)
                )
                mesoglea.addChild(
                    "mesoglea_lower",
                    ModelPartBuilder.create()
                        .uv(1, 32)
                        .cuboid(-7f, 0f, -7f, 14f, 4f, 14f),
                    ModelTransform.pivot(0f, 0f, 0f)
                )

                val armsNorth = mesoglea.arms("arms_north")
                val armsSouth = mesoglea.arms("arms_south", Direction.SOUTH)
                val armsEast = mesoglea.arms("arms_east", Direction.EAST)
                val armsWest = mesoglea.arms("arms_west", Direction.WEST)
                armsNorth.arms("arms_north_lower", Direction.NORTH, true)
                armsSouth.arms("arms_south_lower", Direction.SOUTH, true)
                armsEast.arms("arms_east_lower", Direction.EAST, true)
                armsWest.arms("arms_west_lower", Direction.WEST, true)
                return TexturedModelData.of(modelData, 64, 128)
            }

        private fun ModelPartData.arms(
            tendril: String, direction: Direction = Direction.NORTH, bottom: Boolean = false
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
                ModelTransform.pivot(direction.vector.x * 4f, 0f, direction.vector.z * 4f)
            }
            return this.addChild(tendril, modelPart, pivot)
        }
    }
}