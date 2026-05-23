package org.teamvoided.dusk_debris.entity.piffling.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationChannel.Interpolations
import net.minecraft.client.animation.AnimationChannel.Targets
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations.degreeVec

object PifflingPumpkinAnimations {
    val WALK: AnimationDefinition = AnimationDefinition.Builder.withLength(1.0f).looping()
        .addAnimation(
            "bone", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(2.5f, 0.0f, 0.0f),
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
                Keyframe(0.0f, degreeVec(2.5f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(
                    0.25f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                Targets.ROTATION,
                Keyframe(0.0f, degreeVec(0.0f, 0.0f, 0.0f), Interpolations.LINEAR),
                Keyframe(
                    0.3333f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    degreeVec(5.0f, 0.0f, 0.0f),
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
                    degreeVec(-2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.375f,
                    degreeVec(-12.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(-2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.875f,
                    degreeVec(-12.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(-2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "left_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.375f,
                    degreeVec(-12.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(-2.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-7.5f, 0.0f, 0.0f),
                    Interpolations.LINEAR
                ),
                Keyframe(
                    0.875f,
                    degreeVec(-12.5f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(-2.5f, 0.0f, 0.0f),
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
                    0.25f,
                    degreeVec(-20.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(20.0f, 0.0f, 0.0f),
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
            "left_leg", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(20.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.5f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.75f,
                    degreeVec(-20.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.0f,
                    degreeVec(0.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        ).build()

    val RUN: AnimationDefinition = AnimationDefinition.Builder.withLength(0.3333f).looping()
        .addAnimation(
            "bone", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.0833f,
                    degreeVec(0.0f, 0.0f, 5.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(-5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 0.0f, -5.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "body", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-5.0f, -5.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.0833f,
                    degreeVec(0.0f, 5.0f, 5.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(5.0f, -5.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 5.0f, -5.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(-5.0f, -5.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.0833f,
                    degreeVec(0.0f, 0.0f, 10.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.25f,
                    degreeVec(0.0f, 0.0f, -10.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(-5.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-200.0f, 0.0f, -40.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(-160.0f, 0.0f, -40.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(-200.0f, 0.0f, -40.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "left_arm", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-160.0f, 0.0f, 40.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(-200.0f, 0.0f, 40.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(-160.0f, 0.0f, 40.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "right_leg", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(-25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "left_leg", AnimationChannel(
                Targets.ROTATION,
                Keyframe(
                    0.0f,
                    degreeVec(-25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.1667f,
                    degreeVec(25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.3333f,
                    degreeVec(-25.0f, 0.0f, 0.0f),
                    Interpolations.CATMULLROM
                )
            )
        ).build()
}