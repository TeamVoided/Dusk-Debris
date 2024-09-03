package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator
import net.minecraft.client.render.animation.PartAnimation
import net.minecraft.client.render.animation.PartAnimation.AnimationTargets
import net.minecraft.client.render.animation.PartAnimation.Interpolations

object KotlinArmadilloEntityAnimations {
    val ROLLING: Animation =
        Animation.Builder.withLength(0.5f).addPartAnimation(
            "body",
            PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.1667f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)

            )
        ).addPartAnimation(
            "body",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.1667f, Animator.translate(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.2083f, Animator.translate(0.0f, 6.0f, -1.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 6.0f, -1.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.375f, Animator.translate(0.0f, -1.0f, -1.0f), Interpolations.LINEAR)
            )
        ).addPartAnimation(
            "tail",
            PartAnimation(
                AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.1667f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        ).addPartAnimation(
            "tail",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.1667f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.2083f, Animator.translate(0.0f, 0.0f, -2.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, -2.0f), Interpolations.LINEAR)
            )
        ).addPartAnimation(
            "head",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(-72.5f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "head",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, -1.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(0.0f, 2.0f, 1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 2.0f, 1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2917f, Animator.translate(0.0f, 2.0f, 6.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(0.0f, 2.0f, 7.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_hind_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(-35.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, 5.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1875f, Animator.translate(0.0f, 8.0f, -3.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(1.0f, 3.0f, -6.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_hind_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, 5.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1875f, Animator.translate(0.0f, 8.0f, -3.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(-1.0f, 3.0f, -6.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_front_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(-27.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.rotate(-32.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(-85.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_front_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1875f, Animator.translate(-0.5f, 11.5f, 0.5f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(-1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(-1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(-1.0f, 2.0f, 3.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_front_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(-12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.rotate(-35.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(-85.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_front_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1875f, Animator.translate(0.5f, 11.5f, 0.5f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(1.0f, 2.0f, 3.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "cube",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4167f, Animator.rotate(-2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4583f, Animator.rotate(5.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "cube", PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1667f, Animator.translate(0.0f, 8.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.2083f, Animator.translate(0.0f, 7.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 7.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.375f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4167f, Animator.translate(0.0f, 1.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4583f, Animator.translate(0.0f, 0.6f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
    val WALK: Animation = Animation.Builder.withLength(1.4583f).looping().addPartAnimation(
        "body", PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 4.6f), Interpolations.SPLINE),
                AnimationKeyframe(0.2917f, Animator.rotate(0.0f, 0.0f, 6.81f), Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.7083f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.9583f, Animator.rotate(0.0f, 0.0f, -4.6f), Interpolations.SPLINE),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, -6.89f), Interpolations.SPLINE),
                AnimationKeyframe(1.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(1.4583f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
            )
        )
    ).addPartAnimation(
        "body",
        PartAnimation(
            AnimationTargets.TRANSLATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, -0.2f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.7083f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(0.9583f, Animator.translate(0.0f, -0.2f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(1.25f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                AnimationKeyframe(1.4583f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
            )
        )
    ).addPartAnimation(
        "tail",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.rotate(-9.17f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.75f, Animator.rotate(5.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.2083f, Animator.rotate(-8.24f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "right_hind_leg",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.75f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.2917f, Animator.rotate(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "right_hind_leg",
        PartAnimation(
            AnimationTargets.TRANSLATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(1.2917f, Animator.translate(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "left_hind_leg",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5417f, Animator.rotate(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.7083f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.9583f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.2083f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "left_hind_leg",
        PartAnimation(
            AnimationTargets.TRANSLATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, -0.25f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(0.5417f, Animator.translate(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                AnimationKeyframe(0.7083f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.9583f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(1.2083f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.translate(0.0f, 0.0f, -0.25f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "right_front_leg",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.2917f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5417f, Animator.rotate(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.7083f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.9583f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.2083f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "right_front_leg",
        PartAnimation(
            AnimationTargets.TRANSLATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, -0.25f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(0.5417f, Animator.translate(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                AnimationKeyframe(0.7083f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.9583f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(1.2083f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.translate(0.0f, 0.0f, -0.25f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "left_front_leg",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.75f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, Animator.rotate(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.2917f, Animator.rotate(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "left_front_leg",
        PartAnimation(
            AnimationTargets.TRANSLATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, Animator.translate(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                AnimationKeyframe(1.2917f, Animator.translate(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addPartAnimation(
        "head",
        PartAnimation(
            AnimationTargets.ROTATE,
            *arrayOf(
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, -2.5f), Interpolations.LINEAR),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(0.7083f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, 2.5f), Interpolations.LINEAR),
                AnimationKeyframe(1.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                AnimationKeyframe(1.4583f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).build()
    val SCARED: Animation =
        Animation.Builder.withLength(2.5f).addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.ROTATE, *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(-70.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.rotate(-65.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.9f, Animator.rotate(-7.5f, 0.0f, 45.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.15f, Animator.rotate(-7.5f, 0.0f, 45.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.rotate(-0.8639f, -1.4959f, -39.1287f), Interpolations.SPLINE),
                    AnimationKeyframe(1.6f, Animator.rotate(-0.8639f, -1.4959f, -39.1287f), Interpolations.LINEAR),
                    AnimationKeyframe(1.75f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.8f, Animator.rotate(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.85f, Animator.rotate(-70.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.TRANSLATE, *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 1.0f, 7.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.05f, Animator.translate(0.0f, 1.0f, 4.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.translate(0.0f, 1.0f, 4.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 1.0f, 5.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.35f, Animator.translate(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4f, Animator.translate(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.translate(0.0f, 2.1f, 1.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6f, Animator.translate(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.translate(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.75f, Animator.translate(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.8f, Animator.translate(0.0f, 0.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.95f, Animator.translate(0.0f, 0.1f, 5.2f), Interpolations.LINEAR),
                    AnimationKeyframe(2.0f, Animator.translate(0.0f, 0.1f, 7.2f), Interpolations.LINEAR),
                    AnimationKeyframe(2.15f, Animator.translate(0.0f, 0.1f, 8.2f), Interpolations.LINEAR),
                    AnimationKeyframe(2.3f, Animator.translate(0.0f, 0.1f, 5.2f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(AnimationKeyframe(0.0f, Animator.translate(0.0f, 3.0f, -2.0f), Interpolations.LINEAR))
            )
        ).addPartAnimation(
            "left_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(AnimationKeyframe(0.0f, Animator.translate(0.0f, 3.0f, -2.0f), Interpolations.LINEAR))
            )
        ).addPartAnimation(
            "right_front_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5833f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.8333f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.0f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.75f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.8f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.95f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "right_front_leg", PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5833f, Animator.translate(-1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.6667f, Animator.translate(-1.0f, 2.0f, -2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.8333f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.0f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.75f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.95f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(2.0f, Animator.translate(-1.0f, 2.0f, 3.0f), Interpolations.SPLINE),
                    AnimationKeyframe(2.15f, Animator.translate(-1.0f, 3.0f, 4.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "left_front_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6667f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.8333f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.0f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.75f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.8f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.95f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "left_front_leg", PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6667f, Animator.translate(1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.75f, Animator.translate(1.0f, 2.0f, -2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.8333f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.0f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.75f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.95f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(2.0f, Animator.translate(1.0f, 2.0f, 3.0f), Interpolations.SPLINE),
                    AnimationKeyframe(2.15f, Animator.translate(1.0f, 3.0f, 4.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "cube", PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.35f, Animator.rotate(15.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.rotate(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.05f, Animator.rotate(-17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.15f, Animator.rotate(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.3f, Animator.rotate(12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "cube", PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.35f, Animator.translate(0.0f, 1.6f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.5f, Animator.translate(0.0f, 0.5f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.05f, Animator.translate(0.0f, 1.2f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.15f, Animator.translate(0.0f, 1.7f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.25f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.3f, Animator.translate(0.0f, 1.3f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(2.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
    val UNROLLING: Animation =
        Animation.Builder.withLength(1.5f).addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.1f, Animator.rotate(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.65f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.85f, Animator.rotate(-2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.9f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.95f, Animator.rotate(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.05f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.1f, Animator.rotate(7.5f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "head", PartAnimation(
                AnimationTargets.TRANSLATE, *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 1.0f, 5.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.05f, Animator.translate(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.1f, Animator.translate(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.translate(0.0f, 2.1f, 1.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    AnimationKeyframe(0.4f, Animator.translate(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    AnimationKeyframe(0.65f, Animator.translate(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.translate(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.75f, Animator.translate(0.0f, 4.1f, 2.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.85f, Animator.translate(0.0f, 5.1f, 3.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.9f, Animator.translate(0.0f, 0.1f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(0.95f, Animator.translate(0.0f, 0.9f, -0.8f), Interpolations.LINEAR),
                    AnimationKeyframe(1.05f, Animator.translate(0.0f, 0.9f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.1f, Animator.translate(0.0f, 2.6f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.15f, Animator.translate(0.0f, 2.4f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(0.0f, 0.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.25f, Animator.translate(0.0f, 0.0f, 0.2f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.translate(0.0f, 0.0f, 0.2f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_hind_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(1.1f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.rotate(0.0f, 0.0f, 30.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.45f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(1.1f, Animator.translate(0.0f, 3.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.translate(-1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.45f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_hind_leg",
            PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(1.1f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.rotate(0.0f, 0.0f, -30.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.45f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_hind_leg",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(1.1f, Animator.translate(0.0f, 3.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.translate(1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.35f, Animator.translate(1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.45f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "right_front_leg", PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.05f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.25f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.55f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6f, Animator.rotate(-92.5f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.1f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.3f, Animator.rotate(0.0f, 0.0f, 30.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.4f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.45f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "right_front_leg", PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.05f, Animator.translate(-1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.25f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.55f, Animator.translate(-1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.translate(-1.0f, 2.0f, 2.63f), Interpolations.SPLINE),
                    AnimationKeyframe(1.1f, Animator.translate(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(-1.0f, 7.0f, 2.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.translate(-1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.45f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "left_front_leg", PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.05f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.25f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.55f, Animator.rotate(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.6f, Animator.rotate(-87.5f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.1f, Animator.rotate(-90.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.3f, Animator.rotate(0.0f, 0.0f, -30.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.4f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.45f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "left_front_leg", PartAnimation(
                AnimationTargets.TRANSLATE, *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.05f, Animator.translate(1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.15f, Animator.translate(1.0f, 2.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(0.25f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.55f, Animator.translate(1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.translate(1.0f, 2.0f, 1.88f), Interpolations.SPLINE),
                    AnimationKeyframe(0.75f, Animator.translate(1.0f, 2.0f, 2.67f), Interpolations.SPLINE),
                    AnimationKeyframe(1.1f, Animator.translate(1.0f, 2.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.2f, Animator.translate(1.0f, 8.0f, 2.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.25f, Animator.translate(1.06f, 5.06f, 1.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.3f, Animator.translate(1.0f, 3.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.4f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE),
                    AnimationKeyframe(1.45f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.SPLINE)
                )
            )
        ).addPartAnimation(
            "cube", PartAnimation(
                AnimationTargets.ROTATE,
                *arrayOf(
                    AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.05f, Animator.rotate(15.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.rotate(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.75f, Animator.rotate(-17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.85f, Animator.rotate(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.9f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.95f, Animator.rotate(12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.05f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.1f, Animator.rotate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "cube", PartAnimation(
                AnimationTargets.TRANSLATE, *arrayOf(
                    AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.05f, Animator.translate(0.0f, 1.6f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.15f, Animator.translate(0.0f, 0.5f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.25f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.7f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.75f, Animator.translate(0.0f, 1.2f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.85f, Animator.translate(0.0f, 1.7f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.9f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(0.95f, Animator.translate(0.0f, 1.3f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.05f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.25f, Animator.translate(0.0f, 8.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 1.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addPartAnimation(
            "body",
            PartAnimation(
                AnimationTargets.TRANSLATE,
                *arrayOf(
                    AnimationKeyframe(1.1f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.2f, Animator.translate(0.0f, 4.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.25f, Animator.translate(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.3f, Animator.translate(0.0f, 4.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.4f, Animator.translate(0.0f, -1.0f, 0.0f), Interpolations.LINEAR),
                    AnimationKeyframe(1.5f, Animator.translate(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
}