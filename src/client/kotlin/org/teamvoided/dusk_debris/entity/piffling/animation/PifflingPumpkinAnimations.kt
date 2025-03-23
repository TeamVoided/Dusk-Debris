package org.teamvoided.dusk_debris.entity.piffling.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator.rotate
import net.minecraft.client.render.animation.PartAnimation
import net.minecraft.client.render.animation.PartAnimation.AnimationTargets
import net.minecraft.client.render.animation.PartAnimation.Interpolations

object PifflingPumpkinAnimations {
    val WALK: Animation = Animation.Builder.withLength(1.0f).looping()
        .addPartAnimation(
            "bone", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, rotate(2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(
                    0.25f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(
                    0.3333f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.8333f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(-12.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.875f,
                    rotate(-12.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "left_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(-12.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.875f,
                    rotate(-12.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(-2.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_leg", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(-20.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(20.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "left_leg", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(20.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-20.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        ).build()

    val RUN: Animation = Animation.Builder.withLength(0.3333f).looping()
        .addPartAnimation(
            "bone", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.0833f,
                    rotate(0.0f, 0.0f, 5.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(-5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 0.0f, -5.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-5.0f, -5.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.0833f,
                    rotate(0.0f, 5.0f, 5.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(5.0f, -5.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 5.0f, -5.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(-5.0f, -5.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.0833f,
                    rotate(0.0f, 0.0f, 10.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 0.0f, -10.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(-5.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-200.0f, 0.0f, -40.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(-160.0f, 0.0f, -40.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(-200.0f, 0.0f, -40.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "left_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-160.0f, 0.0f, 40.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(-200.0f, 0.0f, 40.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(-160.0f, 0.0f, 40.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_leg", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(-25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "left_leg", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(-25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.1667f,
                    rotate(25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.3333f,
                    rotate(-25.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        ).build()
}