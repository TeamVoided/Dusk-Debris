package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator.rotate
import net.minecraft.client.render.animation.Animator.translate
import net.minecraft.client.render.animation.PartAnimation
import net.minecraft.client.render.animation.PartAnimation.AnimationTargets
import net.minecraft.client.render.animation.PartAnimation.Interpolations
import net.minecraft.util.math.Direction
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
    val IDLE: Animation = Animation.Builder.withLength(3f)
        .addPartAnimation(
            "root", PartAnimation(
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0f, translate(0f, 0f, 0f), Interpolations.SPLINE),
                AnimationKeyframe(0.7083f, translate(0f, -2f, 0f), Interpolations.SPLINE),
                AnimationKeyframe(1.5f, translate(0f, 4f, 0f), Interpolations.SPLINE),
                AnimationKeyframe(3f, translate(0f, 0f, 0f), Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            MESOGLEA, PartAnimation(
                AnimationTargets.SCALE,
                AnimationKeyframe(
                    0.2917f,
                    scale(1f, 1f, 1f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7083f,
                    scale(1.025f, 1f, 1.025f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.25f,
                    scale(1f, 0.85f, 1f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2f,
                    scale(1f, 1f, 1f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            MESOGLEA_LOWER, PartAnimation(
                AnimationTargets.SCALE,
                AnimationKeyframe(
                    0.2917f,
                    scale(1f, 1f, 1f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    scale(1.1f, 0.7f, 1.1f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7083f,
                    scale(1f, 1f, 1f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(ARMS_NORTH, arms(Direction.NORTH))
        .addPartAnimation(ARMS_SOUTH, arms(Direction.SOUTH))
        .addPartAnimation(ARMS_WEST, arms(Direction.WEST))
        .addPartAnimation(ARMS_EAST, arms(Direction.EAST))
        .addPartAnimation(ARMS_NORTH_LOWER, arms(Direction.NORTH, true))
        .addPartAnimation(ARMS_SOUTH_LOWER, arms(Direction.SOUTH, true))
        .addPartAnimation(ARMS_WEST_LOWER, arms(Direction.WEST, true))
        .addPartAnimation(ARMS_EAST_LOWER, arms(Direction.EAST, true))
        .build()

    private fun arms(direction: Direction, lower: Boolean = false): PartAnimation {
        return if (!lower)
            PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0f,
                    rotate(0f, 0f, 0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(60f * direction.vector.z, 0f, 60f * direction.vector.x),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1f,
                    rotate(0f, 0f, 0f),
                    Interpolations.SPLINE
                )
            )
        else
            PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0f,
                    rotate(0f, 0f, 0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(-80f * direction.vector.z, 0f, -80f * direction.vector.x),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7917f,
                    rotate(40f * direction.vector.z, 0f, 40f * direction.vector.x),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1f,
                    rotate(0f, 0f, 0f),
                    Interpolations.SPLINE
                )
            )
    }
}