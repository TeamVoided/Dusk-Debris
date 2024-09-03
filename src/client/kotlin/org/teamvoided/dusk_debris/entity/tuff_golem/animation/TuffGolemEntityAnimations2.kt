package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator.*
import net.minecraft.client.render.animation.PartAnimation

object TuffGolemEntityAnimations2 {
    val WALK: Animation = Animation.Builder.withLength(2.0f).looping()
        .addPartAnimation(
            "left_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.5f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.5f,
                    rotate(22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "left_leg", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.5f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_leg", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.5f,
                    rotate(22.5f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(0.0f, 0.0f, -5.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.5f,
                    rotate(0.0f, 0.0f, 5.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(
                    0.5f,
                    translate(0.0f, -1.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(1.0f, translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(
                    1.5f,
                    translate(0.0f, -1.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(2.0f, translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .build()
    val STATUE: Animation = Animation.Builder.withLength(1.0f)
        .addPartAnimation(
            "left_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.25f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.TRANSLATE,
                AnimationKeyframe(
                    0.25f,
                    translate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(1.0f, translate(0.0f, -2.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .build()

    val ARISE: Animation = Animation.Builder.withLength(0.75f)
        .addPartAnimation(
            "left_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(-15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "right_arm", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(-15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.25f,
                    rotate(15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.5f,
                    rotate(15.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(0.0f, 0.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                PartAnimation.AnimationTargets.TRANSLATE,
                AnimationKeyframe(
                    0.0f,
                    translate(0.0f, -2.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(0.75f, translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .build()
}