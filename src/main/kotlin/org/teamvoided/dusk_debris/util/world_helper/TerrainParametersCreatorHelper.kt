package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline

fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(location: Float, f: Float): Spline.Builder<C, I> =
    this.method_41294(location, f)

//fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(
//    location: Float,
//    value: Float,
//    derivative: Float
//): Spline.Builder<C, I> {
//    return this.add(location, value, derivative)
//}

fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(location: Float, value: Spline<C, I>): Spline.Builder<C, I> =
    this.method_41295(location, value)
