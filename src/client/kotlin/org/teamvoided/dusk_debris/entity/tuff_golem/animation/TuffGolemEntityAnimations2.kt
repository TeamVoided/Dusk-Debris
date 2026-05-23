package org.teamvoided.dusk_debris.entity.tuff_golem.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations.degreeVec
import net.minecraft.client.animation.KeyframeAnimations.posVec

object TuffGolemEntityAnimations2 {
    val WALK: AnimationDefinition = AnimationDefinition.Builder.withLength(2.0f).looping()
        .addAnimation(
            "left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "left_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(-22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    degreeVec(22.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(0.0f, 0.0f, -5.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    degreeVec(0.0f, 0.0f, 5.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(0.0f, posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(
                    0.5f,
                    posVec(0.0f, -1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(1.0f, posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(
                    1.5f,
                    posVec(0.0f, -1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(2.0f, posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()
    val STATUE: AnimationDefinition = AnimationDefinition.Builder.withLength(1.0f)
        .addAnimation(
            "left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(
                    0.25f,
                    posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(1.0f, posVec(0.0f, -2.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()

    val ARISE: AnimationDefinition = AnimationDefinition.Builder.withLength(0.75f)
        .addAnimation(
            "left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.375f,
                    degreeVec(-15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.375f,
                    degreeVec(-15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(
                    0.0f,
                    posVec(0.0f, -2.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(0.75f, posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()
}