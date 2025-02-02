package org.teamvoided.dusk_debris.entity.jellyfish.tiny.animation

import net.minecraft.client.render.animation.Animation
import net.minecraft.client.render.animation.AnimationKeyframe
import net.minecraft.client.render.animation.Animator
import net.minecraft.client.render.animation.PartAnimation
import org.teamvoided.dusk_debris.util.scale

object TinyEnemyJellyfishAnimations {
    val IDLE: Animation = Animation.Builder.withLength(2.0f).looping()
        .addPartAnimation(
            "jellyfish", PartAnimation(
                PartAnimation.AnimationTargets.TRANSLATE,
                AnimationKeyframe(0.0f, Animator.translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(
                    0.75f,
                    Animator.translate(0.0f, -0.5f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(
                    1.375f,
                    Animator.translate(0.0f, 1.0f, 0.0f),
                    PartAnimation.Interpolations.SPLINE
                ),
                AnimationKeyframe(2.0f, Animator.translate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "membrane", PartAnimation(
                PartAnimation.AnimationTargets.SCALE,
                AnimationKeyframe(0.0f, scale(1.0f, 1.0f, 1.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5417f, scale(1.05f, 0.95f, 1.05f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.0f, scale(0.95f, 1.05f, 0.95f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.8333f, scale(1.0f, 1.0f, 1.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "membrane_extra", PartAnimation(
                PartAnimation.AnimationTargets.SCALE,
                AnimationKeyframe(0.0f, scale(1.0f, 1.0f, 1.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5417f, scale(1.05f, 0.75f, 1.05f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.125f, scale(1.0f, 1.0f, 1.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "tendrils_north", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.rotate(-30.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "tendrils_west", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, -30.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "tendrils_south", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.rotate(30.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .addPartAnimation(
            "tendrils_east", PartAnimation(
                PartAnimation.AnimationTargets.ROTATE,
                AnimationKeyframe(0.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(0.5f, Animator.rotate(0.0f, 0.0f, 30.0f), PartAnimation.Interpolations.SPLINE),
                AnimationKeyframe(1.0f, Animator.rotate(0.0f, 0.0f, 0.0f), PartAnimation.Interpolations.SPLINE)
            )
        )
        .build()
}