package org.teamvoided.dusk_debris.entity.jellyfish.tiny.animation

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations
import org.teamvoided.dusk_debris.util.scale

object TinyEnemyJellyfishAnimations {
    val IDLE: AnimationDefinition = AnimationDefinition.Builder.withLength(2.0f).looping()
        .addAnimation(
            "jellyfish", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(
                    0.75f,
                    KeyframeAnimations.posVec(0.0f, -0.5f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.375f,
                    KeyframeAnimations.posVec(0.0f, 1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(2.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "membrane", AnimationChannel(
                AnimationChannel.Targets.SCALE,
                Keyframe(0.0f, scale(1.0f, 1.0f, 1.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, scale(1.05f, 0.95f, 1.05f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, scale(0.95f, 1.05f, 0.95f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.8333f, scale(1.0f, 1.0f, 1.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "membrane_extra", AnimationChannel(
                AnimationChannel.Targets.SCALE,
                Keyframe(0.0f, scale(1.0f, 1.0f, 1.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, scale(1.05f, 0.75f, 1.05f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.125f, scale(1.0f, 1.0f, 1.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tendrils_north", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(-30.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tendrils_west", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tendrils_south", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(30.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tendrils_east", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()
}