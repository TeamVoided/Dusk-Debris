package org.teamvoided.dusk_debris.entity.volaphyra.model

import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.VolaphyraEntity

class VolaphyraEntityModel(private val root: ModelPart) :
    SinglePartEntityModel<AbstractVolaphyraEntity>(RenderLayer::getEntityTranslucent) {
    val membrane: ModelPart = root.getChild("membrane")
    val membraneLower: ModelPart = membrane.getChild("membrane_lower")
    val tendrilsNorth: ModelPart = membrane.getChild("tendrils_north")
    val tendrilsSouth: ModelPart = membrane.getChild("tendrils_south")
    val tendrilsEast: ModelPart = membrane.getChild("tendrils_east")
    val tendrilsWest: ModelPart = membrane.getChild("tendrils_west")
    val tendrilsNorthLower: ModelPart = tendrilsNorth.getChild("tendrils_north_lower")
    val tendrilsSouthLower: ModelPart = tendrilsSouth.getChild("tendrils_south_lower")
    val tendrilsEastLower: ModelPart = tendrilsEast.getChild("tendrils_east_lower")
    val tendrilsWestLower: ModelPart = tendrilsWest.getChild("tendrils_west_lower")

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
//        val l: Float = animationProgress * 0.1f + limbAngle * 0.5f
//        this.membrane.roll = l * 0.01f

    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val membrane = modelPartData.addChild(
                    "membrane",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-8f, -16f, -8f, 16f, 16f, 16f),
                    ModelTransform.pivot(0f, 24f, 0f)
                )
                membrane.addChild(
                    "membrane_lower",
                    ModelPartBuilder.create()
                        .uv(1, 32)
                        .cuboid(-7f, 0f, -7f, 14f, 4f, 14f),
                    ModelTransform.pivot(0f, 0f, 0f)
                )

                val tendrilsNorth = membrane.tendrils("tendrils_north")
                val tendrilsSouth = membrane.tendrils("tendrils_south", Direction.SOUTH)
                val tendrilsEast = membrane.tendrils("tendrils_east", Direction.EAST)
                val tendrilsWest = membrane.tendrils("tendrils_west", Direction.WEST)
                tendrilsNorth.tendrils("tendrils_north_lower", Direction.NORTH, true)
                tendrilsSouth.tendrils("tendrils_south_lower", Direction.SOUTH, true)
                tendrilsEast.tendrils("tendrils_east_lower", Direction.EAST, true)
                tendrilsWest.tendrils("tendrils_west_lower", Direction.WEST, true)
                return TexturedModelData.of(modelData, 64, 128)
            }

        private fun ModelPartData.tendrils(
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