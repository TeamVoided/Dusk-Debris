package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationChannel.Interpolations
import net.minecraft.client.animation.AnimationChannel.Targets
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations.degreeVec
import net.minecraft.client.animation.KeyframeAnimations.posVec

object TuffGolemEntityAnimations {
    val WALK: AnimationDefinition = AnimationDefinition.Builder.withLength(2.0f).looping()
        .addAnimation(
            "left_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.4583f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4583f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.5417f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.4583f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4583f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.5417f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "left_leg", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.4583f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4583f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.5417f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_leg", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.4583f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4583f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.5417f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.4583f,
                    degreeVec(0.0f, 0.0f, -5.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    degreeVec(0.0f, 0.0f, -5.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4583f,
                    degreeVec(0.0f, 0.0f, 5.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.5417f,
                    degreeVec(0.0f, 0.0f, 5.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.POSITION,
                Keyframe(0.0f, posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(0.4583f, posVec(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                Keyframe(0.5417f, posVec(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(1.4583f, posVec(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.5417f, posVec(0.0f, -0.4f, 0.0f), Interpolations.LINEAR),
                Keyframe(2.0f, posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM)
            )
        )
        .build()

    val STATUE: AnimationDefinition = AnimationDefinition.Builder.withLength(1.0f)
        .addAnimation(
            "left_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.2917f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7083f,
                    degreeVec(-20.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.2917f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7083f,
                    degreeVec(-20.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.2917f,
                    degreeVec(-10.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.7083f,
                    degreeVec(10.0f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.POSITION,
                Keyframe(0.0f, posVec(0.0f, 0.0f, 0.0f), Interpolations.CATMULLROM),
                Keyframe(1.0f, posVec(0.0f, -2.0f, 0.0f), Interpolations.CATMULLROM)
            )
        )
        .build()

    val ARISE: AnimationDefinition = AnimationDefinition.Builder.withLength(1.0f)
        .addAnimation(
            "left_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.375f,
                    degreeVec(30.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-45.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.375f,
                    degreeVec(30.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-45.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.375f,
                    degreeVec(-7.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.POSITION,
                Keyframe(0.5833f, posVec(0.0f, -2.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(1.0f, posVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR)
            )
        )
        .build()
}