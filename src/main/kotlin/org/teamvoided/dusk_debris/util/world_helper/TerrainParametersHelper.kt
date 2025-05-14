package org.teamvoided.dusk_debris.util.world_helper

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline

/* - - math - -*/
fun calculateSlope(value1: Float, value2: Float, point1: Float, point2: Float): Float {
    return (value2 - value1) / (point2 - point1)
}

fun calculateSlope(pair1: Pair<Float, Float>, pair2: Pair<Float, Float>): Float {
    return (pair2.second - pair1.second) / (pair2.first - pair1.first)
}


/* - - Vannilla mapped - -*/
fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(location: Float, value: Float): Spline.Builder<C, I> =
    this.method_41294(location, value)

//fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(
//    location: Float,
//    value: Float,
//    derivative: Float
//): Spline.Builder<C, I> {
//    return this.add(location, value, derivative)
//}

fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(location: Float, value: Spline<C, I>): Spline.Builder<C, I> =
    this.method_41295(location, value)

/* - - custom - -*/
fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(input: Pair<Float, Float>): Spline.Builder<C, I> =
    this.method_41294(input.first, input.second)

fun <C, I : ToFloatFunction<C>> Spline.Builder<C, I>.add(
    input: Pair<Float, Float>,
    derivative: Float
): Spline.Builder<C, I> {
    return this.add(input.first, input.second, derivative)
}
