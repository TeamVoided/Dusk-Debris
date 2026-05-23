package org.teamvoided.dusk_debris.util

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.animation.KeyframeAnimations
import org.joml.Vector3f

fun PoseStack.scale(scale: Float) = this.scale(scale, scale, scale)

fun scale(x: Float, y: Float, z: Float): Vector3f {
    return KeyframeAnimations.scaleVec(x.toDouble(), y.toDouble(), z.toDouble())
}