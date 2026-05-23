@file:Suppress("unused")

package voidlib.devin.world.gen

import net.minecraft.core.Holder
import net.minecraft.util.CubicSpline
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters


fun interpolated(fn: DensityFunction): DensityFunction = DensityFunctions.interpolated(fn)
fun interpolated(fn: () -> DensityFunction) = interpolated(fn())

fun flatCache(fn: DensityFunction): DensityFunction = DensityFunctions.flatCache(fn)
fun flatCache(fn: () -> DensityFunction) = flatCache(fn())
fun cache2D(fn: DensityFunction): DensityFunction = DensityFunctions.cache2d(fn)
fun cache2D(fn: () -> DensityFunction) = cache2D(fn())

fun cacheOnce(fn: DensityFunction): DensityFunction = DensityFunctions.cacheOnce(fn)
fun cacheOnce(fn: () -> DensityFunction) = cacheOnce(fn())

fun cacheAllInCell(fn: DensityFunction): DensityFunction = DensityFunctions.cacheAllInCell(fn)
fun cacheAllInCell(fn: () -> DensityFunction) = cacheAllInCell(fn())

fun mappedNoise(
    noise: Holder<NoiseParameters>, horizontalScale: Double, verticalScale: Double, min: Double, max: Double,
): DensityFunction = DensityFunctions.mappedNoise(noise, horizontalScale, verticalScale, min, max)

fun mappedNoise(
    noise: Holder<NoiseParameters>, horizontalScale: Number, verticalScale: Number, min: Number, max: Number,
) = mappedNoise(noise, horizontalScale.toDouble(), verticalScale.toDouble(), min.toDouble(), max.toDouble())

fun mappedNoise(
    noise: Holder<NoiseParameters>, verticalScale: Double, min: Double, max: Double,
): DensityFunction = DensityFunctions.mappedNoise(noise, verticalScale, min, max)

fun mappedNoise(
    noise: Holder<NoiseParameters>, verticalScale: Number, min: Number, max: Number,
) = mappedNoise(noise, verticalScale.toDouble(), min.toDouble(), max.toDouble())

fun mappedNoise(noise: Holder<NoiseParameters>, min: Double, max: Double): DensityFunction =
    DensityFunctions.mappedNoise(noise, min, max)

fun mappedNoise(noise: Holder<NoiseParameters>, min: Number, max: Number) =
    mappedNoise(noise, min.toDouble(), max.toDouble())


fun shiftedNoise2d(
    shiftX: DensityFunction, shiftZ: DensityFunction, scale: Double, noise: Holder<NoiseParameters>,
): DensityFunction = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, scale, noise)

fun shiftedNoise2d(
    shiftX: DensityFunction, shiftZ: DensityFunction, scale: Number, noise: Holder<NoiseParameters>,
) = shiftedNoise2d(shiftX, shiftZ, scale.toDouble(), noise)

fun noise(noise: Holder<NoiseParameters>): DensityFunction = DensityFunctions.noise(noise)

fun noise(noise: Holder<NoiseParameters>, horizontalScale: Double, verticalScale: Double): DensityFunction =
    DensityFunctions.noise(noise, horizontalScale, verticalScale)

fun noise(noise: Holder<NoiseParameters>, horizontalScale: Number, verticalScale: Number) =
    noise(noise, horizontalScale.toDouble(), verticalScale.toDouble())

fun noise(noise: Holder<NoiseParameters>, verticalScale: Double): DensityFunction =
    DensityFunctions.noise(noise, verticalScale)

fun noise(noise: Holder<NoiseParameters>, verticalScale: Number) = noise(noise, verticalScale.toDouble())

fun rangeChoice(
    input: DensityFunction,
    minInclusive: Number, maxInclusive: Number,
    whenInRange: DensityFunction, whenOutOfRange: DensityFunction,
): DensityFunction =
    DensityFunctions.rangeChoice(input, minInclusive.toDouble(), maxInclusive.toDouble(), whenInRange, whenOutOfRange)

fun shiftX(noise: Holder<NoiseParameters>): DensityFunction = DensityFunctions.shiftA(noise)
fun shiftZ(noise: Holder<NoiseParameters>): DensityFunction = DensityFunctions.shiftB(noise)
fun shift(noise: Holder<NoiseParameters>): DensityFunction = DensityFunctions.shift(noise)

fun blendDensity(fn: DensityFunction): DensityFunction = DensityFunctions.blendDensity(fn)
fun blendDensity(fn: () -> DensityFunction) = blendDensity(fn())

fun endIslands(random: Long): DensityFunction = DensityFunctions.endIslands(random)
fun endIslands(random: Number) = endIslands(random.toLong())

//fun weirdScaledSampler(fn: DensityFunction, noise: Holder<NoiseParameters>, type: WeirdScaledSampler.Type, ): DensityFunction = DensityFunctions.endIslands(fn, noise, type)

fun add(fn: DensityFunction, fn1: DensityFunction): DensityFunction = DensityFunctions.add(fn, fn1)
fun add(const: Number, fn: DensityFunction) = add(const(const.toDouble()), fn)
fun add(const: Number, fn: () -> DensityFunction) = add(const(const.toDouble()), fn())

fun multiply(fn: DensityFunction, fn1: DensityFunction): DensityFunction = DensityFunctions.mul(fn, fn1)
fun multiply(const: Number, fn: DensityFunction) = multiply(const(const.toDouble()), fn)
fun multiply(const: Number, fn: () -> DensityFunction) = multiply(const(const.toDouble()), fn())

fun min(fn: DensityFunction, fn1: DensityFunction): DensityFunction = DensityFunctions.min(fn, fn1)
fun min(const: Number, fn: DensityFunction) = min(const(const.toDouble()), fn)
fun min(const: Number, fn: () -> DensityFunction) = min(const(const.toDouble()), fn())

fun max(fn: DensityFunction, fn1: DensityFunction): DensityFunction = DensityFunctions.max(fn, fn1)
fun max(const: Number, fn: DensityFunction) = max(const(const.toDouble()), fn)
fun max(const: Number, fn: () -> DensityFunction) = max(const(const.toDouble()), fn())

fun copySpline(spline: CubicSpline<DensityFunctions.Spline.Point, DensityFunctions.Spline.Coordinate>): DensityFunction =
    DensityFunctions.spline(spline)

fun zero(): DensityFunction = DensityFunctions.zero()

fun const(value: Double): DensityFunction = DensityFunctions.constant(value)
fun const(value: Number) = const(value.toDouble())

fun clampedGradientY(fromY: Int, toY: Int, fromValue: Double, toValue: Double): DensityFunction =
    DensityFunctions.yClampedGradient(fromY, toY, fromValue, toValue)

fun clampedGradientY(fromY: Number, toY: Number, fromValue: Number, toValue: Number) =
    clampedGradientY(fromY.toInt(), toY.toInt(), fromValue.toDouble(), toValue.toDouble())

//fun mapped(input: DensityFunction, type: Mapped.Type): DensityFunction = DensityFunctions.mapped(input, type)

fun mapFromUnitToValue(value: DensityFunction, min: Double, max: Double): DensityFunction =
    DensityFunctions.mapFromUnitTo(value, min, max)

fun mapFromUnitToValue(value: DensityFunction, min: Number, max: Number) =
    mapFromUnitToValue(value, min.toDouble(), max.toDouble())

fun mapFromUnitToValue(min: Number, max: Number, value: () -> DensityFunction) =
    mapFromUnitToValue(value(), min.toDouble(), max.toDouble())

fun blendAlpha(): DensityFunction = DensityFunctions.blendAlpha()

fun getBlendOffset(): DensityFunction = DensityFunctions.blendOffset()

fun lerp(delta: DensityFunction, start: DensityFunction, end: DensityFunction): DensityFunction =
    DensityFunctions.lerp(delta, start, end)

