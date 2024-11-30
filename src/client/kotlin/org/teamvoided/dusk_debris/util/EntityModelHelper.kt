package org.teamvoided.dusk_debris.util

import net.minecraft.client.render.animation.Animator
import org.joml.Vector3f

fun scale(x: Float, y: Float, z: Float): Vector3f {
    return Animator.scale(x.toDouble(), y.toDouble(), z.toDouble())
}