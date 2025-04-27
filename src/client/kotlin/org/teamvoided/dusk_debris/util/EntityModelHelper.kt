package org.teamvoided.dusk_debris.util

import net.minecraft.client.render.animation.Animator
import net.minecraft.client.util.math.MatrixStack
import org.joml.Vector3f

fun MatrixStack.scale(scale: Float) = this.scale(scale, scale, scale)

fun scale(x: Float, y: Float, z: Float): Vector3f {
    return Animator.scale(x.toDouble(), y.toDouble(), z.toDouble())
}