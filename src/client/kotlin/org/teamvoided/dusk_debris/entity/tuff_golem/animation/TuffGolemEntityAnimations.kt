package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator.*
import net.minecraft.client.render.animation.PartAnimation
import net.minecraft.client.render.animation.PartAnimation.*

object TuffGolemEntityAnimations {
    val WALK: Animation = Animation.Builder.withLength(2.0f).looping()
        .addPartAnimation(
            "left_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.4583f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.5417f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.4583f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.5417f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    2.0f,
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
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.4583f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.5417f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.4583f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.5417f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    2.0f,
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
                    0.4583f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.5417f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.4583f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.5417f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
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
                    0.4583f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.5417f,
                    rotate(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.4583f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.5417f,
                    rotate(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.4583f,
                    rotate(0.0f, 0.0f, -5.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    0.5417f,
                    rotate(0.0f, 0.0f, -5.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.4583f,
                    rotate(0.0f, 0.0f, 5.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    1.5417f,
                    rotate(0.0f, 0.0f, 5.0f),
                    Interpolations.LINEAR
                ),
                AnimationKeyframe(
                    2.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                )
            )
        )
        .addPartAnimation(
            "body", PartAnimation(
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.4583f, translate(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5417f, translate(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(1.4583f, translate(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.5417f, translate(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(2.0f, translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
            )
        )
        .build()

    val STATUE: Animation = Animation.Builder.withLength(1.0f)
        .addPartAnimation(
            "left_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.2917f,
                    rotate(15.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7083f,
                    rotate(-20.0f, 0.0f, 0.0f),
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
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.2917f,
                    rotate(15.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7083f,
                    rotate(-20.0f, 0.0f, 0.0f),
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
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.2917f,
                    rotate(-10.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.7083f,
                    rotate(10.0f, 0.0f, 0.0f),
                    Interpolations.LINEAR
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
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(1.0f, translate(0.0f, -2.0f, 0.0f), Interpolations.SPLINE)
            )
        )
        .build()

    val ARISE: Animation = Animation.Builder.withLength(1.0f)
        .addPartAnimation(
            "left_arm", PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(30.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-45.0f, 0.0f, 0.0f),
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
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(30.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(-45.0f, 0.0f, 0.0f),
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
                AnimationKeyframe(
                    0.0f,
                    rotate(0.0f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.375f,
                    rotate(-7.5f, 0.0f, 0.0f),
                    Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    0.75f,
                    rotate(15.0f, 0.0f, 0.0f),
                    Interpolations.LINEAR
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
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.5833f, translate(0.0f, -2.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
        .build()
}