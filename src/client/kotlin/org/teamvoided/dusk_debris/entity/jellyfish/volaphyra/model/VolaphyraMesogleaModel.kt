package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation.VolaphyraEntityAnimations

class VolaphyraMesogleaModel(private val root: ModelPart) :
    HierarchicalModel<AbstractVolaphyraEntity>(RenderType::entityTranslucent) {
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

    override fun root(): ModelPart {
        return this.root
    }

    override fun setupAnim(
        entity: AbstractVolaphyraEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.root().allParts.forEach(ModelPart::resetPose)
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
            north.xRot += -Mth.cos(value * 0.5f) * mult
            south.xRot += Mth.cos(value * 0.55f) * mult
            east.zRot += Mth.cos(value * 0.6f) * mult
            west.zRot += -Mth.cos(value * 0.65f) * mult
        }

        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val modelPartData = modelData.root
                val mesoglea = modelPartData.addOrReplaceChild(
                    MESOGLEA,
                    CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8f, -16f, -8f, 16f, 16f, 16f),
                    PartPose.offset(0f, 24f, 0f)
                )
                mesoglea.addOrReplaceChild(
                    MESOGLEA_LOWER,
                    CubeListBuilder.create()
                        .texOffs(1, 32)
                        .addBox(-7f, 0f, -7f, 14f, 4f, 14f),
                    PartPose.offset(0f, 0f, 0f)
                )

                val armsNorth = mesoglea.arms(ARMS_NORTH, Direction.NORTH)
                val armsSouth = mesoglea.arms(ARMS_SOUTH, Direction.SOUTH)
                val armsEast = mesoglea.arms(ARMS_EAST, Direction.EAST)
                val armsWest = mesoglea.arms(ARMS_WEST, Direction.WEST)
                armsNorth.arms(ARMS_NORTH_LOWER, Direction.NORTH, true)
                armsSouth.arms(ARMS_SOUTH_LOWER, Direction.SOUTH, true)
                armsEast.arms(ARMS_EAST_LOWER, Direction.EAST, true)
                armsWest.arms(ARMS_WEST_LOWER, Direction.WEST, true)
                return LayerDefinition.create(modelData, 64, 128)
            }

        private fun PartDefinition.arms(
            tendril: String, direction: Direction, bottom: Boolean = false
        ): PartDefinition {
            val modelPart = if (direction.axis == Direction.Axis.Z) {
                CubeListBuilder.create()
                    .texOffs(2, if (bottom) 66 else 50)
                    .addBox(-6f, 0f, 0f, 12f, 16f, 0f)
            } else {
                CubeListBuilder.create()
                    .texOffs(34, if (bottom) 54 else 38)
                    .addBox(0f, 0f, -6f, 0f, 16f, 12f)
            }
            val pivot = if (bottom) {
                PartPose.offset(0f, 16f, 0f)
            } else {
                PartPose.offset(direction.normal.x * -4f, 0f, direction.normal.z * 4f)
            }
            return this.addOrReplaceChild(tendril, modelPart, pivot)
        }
    }
}