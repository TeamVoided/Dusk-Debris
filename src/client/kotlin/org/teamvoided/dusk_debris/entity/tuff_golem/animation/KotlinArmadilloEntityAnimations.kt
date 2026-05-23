package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationChannel.Interpolations
import net.minecraft.client.animation.AnimationChannel.Targets
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations

object KotlinArmadilloEntityAnimations {
    val ROLLING: AnimationDefinition =
        AnimationDefinition.Builder.withLength(0.5f).addAnimation(
            "body",
            AnimationChannel(
                Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)

            )
        ).addAnimation(
            "body",
            AnimationChannel(
                Targets.POSITION,
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 6.0f, -1.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 6.0f, -1.0f), Interpolations.LINEAR),
                Keyframe(0.375f, KeyframeAnimations.posVec(0.0f, -1.0f, -1.0f), Interpolations.LINEAR)
            )
        ).addAnimation(
            "tail",
            AnimationChannel(
                Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        ).addAnimation(
            "tail",
            AnimationChannel(
                Targets.POSITION,
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 0.0f, -2.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, -2.0f), Interpolations.LINEAR)
            )
        ).addAnimation(
            "head",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(-72.5f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "head",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, -1.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 2.0f, 1.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 2.0f, 1.0f), Interpolations.LINEAR),
                    Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, 2.0f, 6.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(0.0f, 2.0f, 7.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_hind_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(-35.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 5.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.1875f, KeyframeAnimations.posVec(0.0f, 8.0f, -3.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(1.0f, 3.0f, -6.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_hind_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 5.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.1875f, KeyframeAnimations.posVec(0.0f, 8.0f, -3.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(-1.0f, 3.0f, -6.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_front_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(-27.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.degreeVec(-32.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(-85.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_front_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1875f, KeyframeAnimations.posVec(-0.5f, 11.5f, 0.5f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(-1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(-1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(-1.0f, 2.0f, 3.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_front_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(-12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.degreeVec(-35.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(-85.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_front_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1875f, KeyframeAnimations.posVec(0.5f, 11.5f, 0.5f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(1.0f, 9.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(1.0f, 2.0f, 3.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "cube",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4167f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4583f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "cube", AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 8.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.2083f, KeyframeAnimations.posVec(0.0f, 7.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 7.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.375f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4167f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4583f, KeyframeAnimations.posVec(0.0f, 0.6f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
    val WALK: AnimationDefinition = AnimationDefinition.Builder.withLength(1.4583f).looping().addAnimation(
        "body", AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 4.6f), Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 6.81f), Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.9583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -4.6f), Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -6.89f), Interpolations.CATMULLROM),
                Keyframe(1.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
            )
        )
    ).addAnimation(
        "body",
        AnimationChannel(
            Targets.POSITION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, -0.2f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.9583f, KeyframeAnimations.posVec(0.0f, -0.2f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(1.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(1.4583f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
            )
        )
    ).addAnimation(
        "tail",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(-9.17f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.75f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.2083f, KeyframeAnimations.degreeVec(-8.24f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "right_hind_leg",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.75f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.2917f, KeyframeAnimations.degreeVec(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "right_hind_leg",
        AnimationChannel(
            Targets.POSITION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(1.2917f, KeyframeAnimations.posVec(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "left_hind_leg",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.9583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "left_hind_leg",
        AnimationChannel(
            Targets.POSITION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.25f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(0.5417f, KeyframeAnimations.posVec(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.9583f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(1.2083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.25f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "right_front_leg",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.9583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "right_front_leg",
        AnimationChannel(
            Targets.POSITION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.25f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(0.5417f, KeyframeAnimations.posVec(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.9583f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(1.2083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.25f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "left_front_leg",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.75f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.2917f, KeyframeAnimations.degreeVec(-20.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "left_front_leg",
        AnimationChannel(
            Targets.POSITION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, KeyframeAnimations.posVec(0.0f, 0.0f, -0.5f), Interpolations.LINEAR),
                Keyframe(1.2917f, KeyframeAnimations.posVec(0.0f, 1.0f, -0.18f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).addAnimation(
        "head",
        AnimationChannel(
            Targets.ROTATION,
            *arrayOf(
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), Interpolations.LINEAR),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), Interpolations.LINEAR),
                Keyframe(1.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
    ).build()
    val SCARED: AnimationDefinition =
        AnimationDefinition.Builder.withLength(2.5f).addAnimation(
            "head", AnimationChannel(
                Targets.ROTATION, *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(-70.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.degreeVec(-65.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.9f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 45.0f), Interpolations.CATMULLROM),
                    Keyframe(1.15f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 45.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.degreeVec(-0.8639f, -1.4959f, -39.1287f), Interpolations.CATMULLROM),
                    Keyframe(1.6f, KeyframeAnimations.degreeVec(-0.8639f, -1.4959f, -39.1287f), Interpolations.LINEAR),
                    Keyframe(1.75f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.8f, KeyframeAnimations.degreeVec(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.85f, KeyframeAnimations.degreeVec(-70.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "head", AnimationChannel(
                Targets.POSITION, *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 1.0f, 7.0f), Interpolations.LINEAR),
                    Keyframe(0.05f, KeyframeAnimations.posVec(0.0f, 1.0f, 4.0f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.posVec(0.0f, 1.0f, 4.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 1.0f, 5.0f), Interpolations.LINEAR),
                    Keyframe(0.35f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.4f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 2.1f, 1.2f), Interpolations.LINEAR),
                    Keyframe(0.6f, KeyframeAnimations.posVec(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.posVec(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.75f, KeyframeAnimations.posVec(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.8f, KeyframeAnimations.posVec(0.0f, 0.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.95f, KeyframeAnimations.posVec(0.0f, 0.1f, 5.2f), Interpolations.LINEAR),
                    Keyframe(2.0f, KeyframeAnimations.posVec(0.0f, 0.1f, 7.2f), Interpolations.LINEAR),
                    Keyframe(2.15f, KeyframeAnimations.posVec(0.0f, 0.1f, 8.2f), Interpolations.LINEAR),
                    Keyframe(2.3f, KeyframeAnimations.posVec(0.0f, 0.1f, 5.2f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 3.0f, -2.0f), Interpolations.LINEAR))
            )
        ).addAnimation(
            "left_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 3.0f, -2.0f), Interpolations.LINEAR))
            )
        ).addAnimation(
            "right_front_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5833f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.8333f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.0f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.75f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.8f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.95f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "right_front_leg", AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    Keyframe(0.5833f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.6667f, KeyframeAnimations.posVec(-1.0f, 2.0f, -2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.8333f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.0f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.75f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.95f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(2.0f, KeyframeAnimations.posVec(-1.0f, 2.0f, 3.0f), Interpolations.CATMULLROM),
                    Keyframe(2.15f, KeyframeAnimations.posVec(-1.0f, 3.0f, 4.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "left_front_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.6667f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.8333f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.0f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.75f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.8f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.95f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "left_front_leg", AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    Keyframe(0.6667f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.75f, KeyframeAnimations.posVec(1.0f, 2.0f, -2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.8333f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.0f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.75f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.95f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.CATMULLROM),
                    Keyframe(2.0f, KeyframeAnimations.posVec(1.0f, 2.0f, 3.0f), Interpolations.CATMULLROM),
                    Keyframe(2.15f, KeyframeAnimations.posVec(1.0f, 3.0f, 4.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "cube", AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.35f, KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.6f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.05f, KeyframeAnimations.degreeVec(-17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.15f, KeyframeAnimations.degreeVec(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.3f, KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "cube", AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.35f, KeyframeAnimations.posVec(0.0f, 1.6f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 0.5f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.6f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.05f, KeyframeAnimations.posVec(0.0f, 1.2f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.15f, KeyframeAnimations.posVec(0.0f, 1.7f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.3f, KeyframeAnimations.posVec(0.0f, 1.3f, 0.0f), Interpolations.LINEAR),
                    Keyframe(2.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
    val UNROLLING: AnimationDefinition =
        AnimationDefinition.Builder.withLength(1.5f).addAnimation(
            "head", AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.1f, KeyframeAnimations.degreeVec(-50.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.4f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.65f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.85f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.9f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.95f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.05f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(7.5f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "head", AnimationChannel(
                Targets.POSITION, *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 1.0f, 5.0f), Interpolations.LINEAR),
                    Keyframe(0.05f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.1f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.posVec(0.0f, 2.1f, 1.2f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    Keyframe(0.4f, KeyframeAnimations.posVec(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    Keyframe(0.65f, KeyframeAnimations.posVec(0.0f, 1.03f, 0.13f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.posVec(0.0f, 1.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.75f, KeyframeAnimations.posVec(0.0f, 4.1f, 2.2f), Interpolations.LINEAR),
                    Keyframe(0.85f, KeyframeAnimations.posVec(0.0f, 5.1f, 3.2f), Interpolations.LINEAR),
                    Keyframe(0.9f, KeyframeAnimations.posVec(0.0f, 0.1f, 0.2f), Interpolations.LINEAR),
                    Keyframe(0.95f, KeyframeAnimations.posVec(0.0f, 0.9f, -0.8f), Interpolations.LINEAR),
                    Keyframe(1.05f, KeyframeAnimations.posVec(0.0f, 0.9f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.1f, KeyframeAnimations.posVec(0.0f, 2.6f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.15f, KeyframeAnimations.posVec(0.0f, 2.4f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.2f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.2f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_hind_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 30.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.45f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(1.1f, KeyframeAnimations.posVec(0.0f, 3.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.posVec(-1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.45f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_hind_leg",
            AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.45f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_hind_leg",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(1.1f, KeyframeAnimations.posVec(0.0f, 3.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(0.0f, 8.0f, -2.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.posVec(1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.35f, KeyframeAnimations.posVec(1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.45f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "right_front_leg", AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.05f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.55f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.6f, KeyframeAnimations.degreeVec(-92.5f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.3f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 30.0f), Interpolations.CATMULLROM),
                    Keyframe(1.4f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.45f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "right_front_leg", AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    Keyframe(0.05f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.25f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.55f, KeyframeAnimations.posVec(-1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.63f), Interpolations.CATMULLROM),
                    Keyframe(1.1f, KeyframeAnimations.posVec(-1.0f, 2.0f, 2.0f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(-1.0f, 7.0f, 2.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.posVec(-1.0f, 3.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.45f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "left_front_leg", AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.05f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.55f, KeyframeAnimations.degreeVec(-45.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.6f, KeyframeAnimations.degreeVec(-87.5f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(-90.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.3f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), Interpolations.CATMULLROM),
                    Keyframe(1.4f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.45f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "left_front_leg", AnimationChannel(
                Targets.POSITION, *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.05f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(0.15f, KeyframeAnimations.posVec(1.0f, 2.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(0.25f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.55f, KeyframeAnimations.posVec(1.0f, 2.0f, -1.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.posVec(1.0f, 2.0f, 1.88f), Interpolations.CATMULLROM),
                    Keyframe(0.75f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.67f), Interpolations.CATMULLROM),
                    Keyframe(1.1f, KeyframeAnimations.posVec(1.0f, 2.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(1.2f, KeyframeAnimations.posVec(1.0f, 8.0f, 2.0f), Interpolations.CATMULLROM),
                    Keyframe(1.25f, KeyframeAnimations.posVec(1.06f, 5.06f, 1.0f), Interpolations.CATMULLROM),
                    Keyframe(1.3f, KeyframeAnimations.posVec(1.0f, 3.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.4f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                    Keyframe(1.45f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
                )
            )
        ).addAnimation(
            "cube", AnimationChannel(
                Targets.ROTATION,
                *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.05f, KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.75f, KeyframeAnimations.degreeVec(-17.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.85f, KeyframeAnimations.degreeVec(-25.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.9f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.95f, KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.05f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.1f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "cube", AnimationChannel(
                Targets.POSITION, *arrayOf(
                    Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.05f, KeyframeAnimations.posVec(0.0f, 1.6f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.15f, KeyframeAnimations.posVec(0.0f, 0.5f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.7f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.75f, KeyframeAnimations.posVec(0.0f, 1.2f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.85f, KeyframeAnimations.posVec(0.0f, 1.7f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.9f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(0.95f, KeyframeAnimations.posVec(0.0f, 1.3f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.05f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.25f, KeyframeAnimations.posVec(0.0f, 8.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 1.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).addAnimation(
            "body",
            AnimationChannel(
                Targets.POSITION,
                *arrayOf(
                    Keyframe(1.1f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.2f, KeyframeAnimations.posVec(0.0f, 4.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.25f, KeyframeAnimations.posVec(0.0f, 5.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.3f, KeyframeAnimations.posVec(0.0f, 4.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.4f, KeyframeAnimations.posVec(0.0f, -1.0f, 0.0f), Interpolations.LINEAR),
                    Keyframe(1.5f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
                )
            )
        ).build()
}