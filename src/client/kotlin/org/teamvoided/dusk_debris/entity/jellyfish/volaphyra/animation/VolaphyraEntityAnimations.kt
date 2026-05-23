package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationChannel.Interpolations
import net.minecraft.client.animation.AnimationChannel.Targets
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations.degreeVec
import net.minecraft.client.animation.KeyframeAnimations.posVec
import net.minecraft.core.Direction
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_EAST
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_EAST_LOWER
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_NORTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_NORTH_LOWER
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_SOUTH
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_SOUTH_LOWER
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_WEST
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.ARMS_WEST_LOWER
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.MESOGLEA
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel.Companion.MESOGLEA_LOWER
import org.teamvoided.dusk_debris.util.scale

object VolaphyraEntityAnimations {
    val IDLE: AnimationDefinition = AnimationDefinition.Builder.withLength(3f)
        .addAnimation(
            "root", AnimationChannel(
                Targets.POSITION,
                Keyframe(0f, posVec(0f, 0f, 0f), Interpolations.CATMULLROM),
                Keyframe(0.7083f, posVec(0f, -2f, 0f), Interpolations.CATMULLROM),
                Keyframe(1.5f, posVec(0f, 4f, 0f), Interpolations.CATMULLROM),
                Keyframe(3f, posVec(0f, 0f, 0f), Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            MESOGLEA, AnimationChannel(
                Targets.SCALE,
                Keyframe(
                    0.2917f,
                    scale(1f, 1f, 1f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7083f,
                    scale(1.025f, 1f, 1.025f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.25f,
                    scale(1f, 0.85f, 1f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    2f,
                    scale(1f, 1f, 1f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            MESOGLEA_LOWER, AnimationChannel(
                Targets.SCALE,
                Keyframe(
                    0.2917f,
                    scale(1f, 1f, 1f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    scale(1.1f, 0.7f, 1.1f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7083f,
                    scale(1f, 1f, 1f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(ARMS_NORTH, arms(Direction.NORTH))
        .addAnimation(ARMS_SOUTH, arms(Direction.SOUTH))
        .addAnimation(ARMS_WEST, arms(Direction.WEST))
        .addAnimation(ARMS_EAST, arms(Direction.EAST))
        .addAnimation(ARMS_NORTH_LOWER, arms(Direction.NORTH, true))
        .addAnimation(ARMS_SOUTH_LOWER, arms(Direction.SOUTH, true))
        .addAnimation(ARMS_WEST_LOWER, arms(Direction.WEST, true))
        .addAnimation(ARMS_EAST_LOWER, arms(Direction.EAST, true))
        .build()

    private fun arms(direction: Direction, lower: Boolean = false): AnimationChannel {
        return if (!lower)
            AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0f,
                    degreeVec(0f, 0f, 0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(60f * direction.normal.z, 0f, 60f * direction.normal.x),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1f,
                    degreeVec(0f, 0f, 0f),
                    Interpolations.CATMULLROM
                )
            )
        else
            AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0f,
                    degreeVec(0f, 0f, 0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(-80f * direction.normal.z, 0f, -80f * direction.normal.x),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7917f,
                    degreeVec(40f * direction.normal.z, 0f, 40f * direction.normal.x),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1f,
                    degreeVec(0f, 0f, 0f),
                    Interpolations.CATMULLROM
                )
            )
    }
}