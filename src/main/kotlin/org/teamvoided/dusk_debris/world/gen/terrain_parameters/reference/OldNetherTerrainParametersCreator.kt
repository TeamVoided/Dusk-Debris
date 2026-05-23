package org.teamvoided.nether_flame.world.biome.source.util

import net.minecraft.util.CubicSpline
import net.minecraft.util.Mth
import net.minecraft.util.ToFloatFunction
import net.minecraft.world.level.levelgen.NoiseRouterData
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object OldNetherTerrainParametersCreator {
    //continentalness values
    private val warpedIsland = -1.1f
    private val lavaOceanDeep = -1.02f
    private val lavaOcean = -0.25f
    private val shoreline = -0.15f
    private val outland = -0.1f
    private val inland = 0.25f
    private val inlandExtreme = 1f
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY
    private val highestLevel = 256
    private val lowestLevel = 0
    private val roofLevel = highestLevel - 32
    private val seaLevel = lowestLevel + 32

    //if above sea level, terrain height gets multiplied by 2, otherwise, return the same
    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> if (f < 0f) f else f * 2f }

    //does a funky if continentalness is greater than -0.1
    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> 1.25f - (6.25f / (f + 5.0f)) }

    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> f * 2.0f }

    fun NetherTerrainParametersCreator() {
    }

    fun <C, I : ToFloatFunction<C>> offsetFloorSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) this.OFFSET_AMPLIFIED else NO_TRANSFORM
        val seashoreSpline = createContinentalOffsetSpline(
            erosion,
            ridgesFolded,
            -0.15f,
            0.0f,
            0.0f,
            0.1f,
            0.0f,
            -0.03f,
            false,
            false,
            amplifiedTransformer
        )
        val outlandSpline = createContinentalOffsetSpline(
            erosion,
            ridgesFolded,
            -0.1f,
            0.03f,
            0.1f,
            0.1f,
            0.01f,
            -0.03f,
            false,
            false,
            amplifiedTransformer
        )
        val midlandSpline = createContinentalOffsetSpline(
            erosion,
            ridgesFolded,
            -0.1f,
            0.03f,
            0.1f,
            0.7f,
            0.01f,
            -0.03f,
            true,
            true,
            amplifiedTransformer
        )
        val inlandSpline = createContinentalOffsetSpline(
            erosion,
            ridgesFolded,
            -0.05f,
            0.03f,
            0.1f,
            1.0f,
            0.01f,
            0.01f,
            true,
            true,
            amplifiedTransformer
        )
//OFFSET CONTINENTALNESS
        return CubicSpline.builder(continents, amplifiedTransformer)
            .addPoint(warpedIsland, netherFloorElevation(180))
            .addPoint(lavaOceanDeep, netherFloorElevation(-18))
            .addPoint(lavaOcean, netherFloorElevation(24))
            .addPoint(shoreline, midlandSpline)
            .addPoint(outland, midlandSpline)
            .addPoint(inland, inlandSpline)
            .addPoint(inlandExtreme, inlandSpline)
            .build()
    }

    fun <C, I : ToFloatFunction<C>> offsetCeilingSpline(
        dropCeiling: I,
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) this.OFFSET_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(dropCeiling, amplifiedTransformer)
            .addPoint(-1f, netherCeilingElevation(230))
            .addPoint(0f, netherCeilingElevation(210))
            .addPoint(1f, netherCeilingElevation(180))
            .build()
    }

    //method_42055
    fun <C, I : ToFloatFunction<C>> factorSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(continents, NO_TRANSFORM)
            .addPoint(-0.19f, 3.95f)
            .addPoint(
                -0.15f,
                factorErosion(erosion, ridges, ridgesFolded, 6.25f, true, NO_TRANSFORM)
            )
//            .addPoint(
//                -0.1f,
//                factorErosion(erosion, ridges, ridgesFolded, 5.47f, true, amplifiedTransformer)
//            )
//            .addPoint(
//                0.03f,
//                factorErosion(erosion, ridges, ridgesFolded, 5.08f, true, amplifiedTransformer)
//            )
//            .addPoint(
//                0.06f,
//                factorErosion(erosion, ridges, ridgesFolded, 4.69f, false, amplifiedTransformer)
//            )
            .build()
    }

    //method_42058
    fun <C, I : ToFloatFunction<C>> jaggednessSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(continents, amplifiedTransformer)
            .addPoint(-0.11f, 0.0f)
            .addPoint(
                0.03f,
                jaggednessErosion(
                    erosion,
                    ridges,
                    ridgesFolded,
                    1.0f,
                    0.5f,
                    0.0f,
                    0.0f,
                    amplifiedTransformer
                )
            )
            .addPoint(
                0.65f,
                jaggednessErosion(
                    erosion,
                    ridges,
                    ridgesFolded,
                    1.0f,
                    1.0f,
                    1.0f,
                    0.0f,
                    amplifiedTransformer
                )
            )
            .build()
    }

    //method_42053
    private fun <C, I : ToFloatFunction<C>> jaggednessErosion(
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        f: Float,
        g: Float,
        h: Float,
        i: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val j = -0.5775f
        val jaggedExtreme = jaggednessRidges(ridges, ridgesFolded, f, h, amplifier)
        val jagged = jaggednessRidges(ridges, ridgesFolded, g, i, amplifier)
        return CubicSpline.builder(erosion, amplifier)
            .addPoint(-1.0f, jaggedExtreme)
            .addPoint(-0.78f, jagged)
            .addPoint(-0.5775f, jagged)
            .addPoint(-0.375f, 0.0f)
            .build()
    }

    //method_42052
    private fun <C, I : ToFloatFunction<C>> jaggednessRidges(
        ridges: I,
        ridgesFolded: I,
        f: Float,
        g: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val h = NoiseRouterData.peaksAndValleys(0.4f)
        val i = NoiseRouterData.peaksAndValleys(0.56666666f)
        val j = (h + i) / 2.0f
        val builder = CubicSpline.builder(ridgesFolded, amplifier)
        builder.addPoint(h, 0.0f)
        if (g > 0.0f) {
            builder.addPoint(j, jaggednessWeirdness(ridges, g, amplifier))
        } else {
            builder.addPoint(j, 0.0f)
        }

        if (f > 0.0f) {
            builder.addPoint(1.0f, jaggednessWeirdness(ridges, f, amplifier))
        } else {
            builder.addPoint(1.0f, 0.0f)
        }
        return builder.build()
    }

    //method_42049
    private fun <C, I : ToFloatFunction<C>> jaggednessWeirdness(
        ridgesFolded: I,
        f: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val g = 0.63f * f
        val h = 0.3f * f
        return CubicSpline.builder(ridgesFolded, amplifier)
            .addPoint(-0.01f, g)
            .addPoint(0.01f, h)
            .build()
    }

    //method_42054
    private fun <C, I : ToFloatFunction<C>> factorErosion(
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        f: Float,
        isOutland: Boolean,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val spline = CubicSpline.builder(ridges, amplifier)
            .addPoint(-0.2f, 6.3f)
            .addPoint(0.2f, f)
            .build()
        val builder = CubicSpline.builder(erosion, amplifier)
            .addPoint(-0.6f, spline)
            .addPoint(
                -0.5f,
                CubicSpline.builder(ridges, amplifier)
                    .addPoint(-0.05f, 6.3f)
                    .addPoint(0.05f, 2.67f)
                    .build()
            )
            .addPoint(-0.35f, spline)
            .addPoint(-0.25f, spline)
            .addPoint(
                -0.1f,
                CubicSpline.builder(ridges, amplifier)
                    .addPoint(-0.05f, 2.67f)
                    .addPoint(0.05f, 6.3f)
                    .build()
            )
            .addPoint(0.03f, spline)
        val spline2: CubicSpline<*, *>
        val spline3: CubicSpline<*, *>
        if (isOutland) {
            spline2 =
                CubicSpline.builder(ridges, amplifier)
                    .addPoint(0.0f, f)
                    .addPoint(0.1f, 0.625f)
                    .build()
            spline3 =
                CubicSpline.builder(ridgesFolded, amplifier)
                    .addPoint(-0.9f, f)
                    .addPoint(-0.69f, spline2)
                    .build()
            builder.addPoint(0.35f, f)
                .addPoint(0.45f, spline3)
                .addPoint(0.55f, spline3)
                .addPoint(0.62f, f)
        } else {
            spline2 = CubicSpline.builder(ridgesFolded, amplifier)
                .addPoint(-0.7f, spline)
                .addPoint(-0.15f, 1.37f)
                .build()
            spline3 =
                CubicSpline.builder(ridgesFolded, amplifier)
                    .addPoint(0.45f, spline)
                    .addPoint(0.7f, 1.56f)
                    .build()
            builder.addPoint(0.05f, spline3)
                .addPoint(0.4f, spline3)
                .addPoint(0.45f, spline2)
                .addPoint(0.55f, spline2)
                .addPoint(0.58f, f)
        }

        return builder.build()
    }

    private fun calculateSlope(value1: Float, value2: Float, point1: Float, point2: Float): Float {
        return (value2 - value1) / (point2 - point1)
    }

    private fun <C, I : ToFloatFunction<C>> buildMountainRidgeSplineWithPoints(
        ridgesFolded: I,
        input: Float,
        inland: Boolean,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val builder = CubicSpline.builder(ridgesFolded, amplifier)
        val g = -0.7f
        val h = -1.0f
        val i = getOffsetValue(h, input, g)
        val k = getOffsetValue(i, input, g)
        val l = calculateMountainRidgeZeroContinentalnessPoint(input)
        val m = -0.65f
        val n: Float
        if (m < l && l < i) {
            n = getOffsetValue(m, input, g)
            val o = -0.75f
            val p = getOffsetValue(o, input, g)
            val q = calculateSlope(i, p, h, o)
            builder.addPoint(h, i, q)
            builder.addPoint(o, p)
            builder.addPoint(m, n)
            val r = getOffsetValue(l, input, g)
            val s = calculateSlope(r, k, l, i)
            builder.addPoint(l - 0.01f, r)
            builder.addPoint(l, r, s)
            builder.addPoint(i, k, s)
        } else {
            n = calculateSlope(i, k, h, i)
            if (inland) {
                builder.addPoint(h, max(0.2, i.toDouble()).toFloat())
                builder.addPoint(0.0f, Mth.lerp(0.5f, i, k), n)
            } else {
                builder.addPoint(h, i, n)
            }
            builder.addPoint(1f, k, n)
        }

        return builder.build()
    }

    private fun getOffsetValue(f: Float, g: Float, h: Float): Float {
        val i = 1.17f
        val j = 0.46082947f
        val k = 1.0f - (1.0f - g) * 0.5f
        val l = 0.5f * (1.0f - g)
        val m = (f + 1.17f) * 0.46082947f
        val n = m * k - l
        return if (f < h) max(n.toDouble(), -0.2222).toFloat() else max(n.toDouble(), 0.0).toFloat()
    }

    //method_42045
    private fun calculateMountainRidgeZeroContinentalnessPoint(f: Float): Float {
        val g = 1.17f
        val h = 0.46082947f
        val i = 1.0f - (1.0f - f) * 0.5f
        val j = 0.5f * (1.0f - f)
        return j / (h * i) - g
    }

    //method_42051
    fun <C, I : ToFloatFunction<C>> createContinentalOffsetSpline(
        erosion: I,
        ridgesFolded: I,
        continentalness: Float,
        middleValue: Float,
        highValue: Float,
        highestValue: Float,
        lowValue: Float,
        swampbedValue: Float,
        inland: Boolean,
        inlandMountain: Boolean,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val l = 0.6f
        val m = 0.5f
        val n = 0.5f
        val mountainTallest = buildMountainRidgeSplineWithPoints(
            ridgesFolded,
            Mth.lerp(highestValue, 0.6f, 1.5f),
            inlandMountain,
            amplifier
        )
        val mountain = buildMountainRidgeSplineWithPoints(
            ridgesFolded,
            Mth.lerp(highestValue, 0.6f, 1.0f),
            inlandMountain,
            amplifier
        )
        val mountainInland = buildMountainRidgeSplineWithPoints(
            ridgesFolded,
            highestValue,
            inlandMountain,
            amplifier
        )
        val plateau = ridgeSpline(
            ridgesFolded,
            continentalness - 0.15f,
            0.5f * highestValue,
            Mth.lerp(0.5f, 0.5f, 0.5f) * highestValue,
            0.5f * highestValue,
            0.6f * highestValue,
            0.5f,
            amplifier
        )
        val plateauInland = ridgeSpline(
            ridgesFolded,
            continentalness,
            lowValue * highestValue,
            middleValue * highestValue,
            0.5f * highestValue,
            0.6f * highestValue,
            0.5f,
            amplifier
        )
        val flatlands = ridgeSpline(
            ridgesFolded,
            continentalness,
            lowValue,
            lowValue,
            middleValue,
            highValue,
            0.5f,
            amplifier
        )
        val windsweptHill = CubicSpline.builder(ridgesFolded, amplifier)
            .addPoint(-1.0f, continentalness)
            .addPoint(-0.4f, flatlands)
            .addPoint(0.0f, highValue + 0.07f)
            .build()
        val swamp = ridgeSpline(
            ridgesFolded,
            -0.02f,
            swampbedValue,
            swampbedValue,
            middleValue,
            highValue,
            0.0f,
            amplifier
        )
        val erosionSplineBuilder =
            CubicSpline.builder(erosion, amplifier)
                .addPoint(-0.85f, mountainTallest)
                .addPoint(-0.7f, mountain)
                .addPoint(-0.4f, mountainInland)
                .addPoint(-0.35f, plateau)
                .addPoint(-0.1f, plateauInland)
//                .addPoint(0.2f, flatlands)
        if (inland) {
            erosionSplineBuilder
//                .addPoint(0.4f, flatlands)
                .addPoint(0.45f, windsweptHill)
                .addPoint(0.55f, windsweptHill)
                .addPoint(0.58f, flatlands)
        }
        erosionSplineBuilder.addPoint(0.7f, swamp)
        return erosionSplineBuilder.build()
    }

    private fun <C, I : ToFloatFunction<C>> ridgeSpline2(
        ridgesFolded: I,
        deepestPoint: Float,
        highestPoint: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val derivative = ((highestPoint - deepestPoint) / 2)
        return CubicSpline.builder(ridgesFolded, amplifier)
            .addPoint(-1.0f, deepestPoint, derivative)
            .addPoint(1.0f, highestPoint, derivative)
            .build()
    }

    private fun <C, I : ToFloatFunction<C>> ridgeSpline(
        ridgesFolded: I,
        point1: Float,
        point2: Float,
        point3: Float,
        point4: Float,
        point5: Float,
        derivativeAlt: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val derivative1 = max((0.5f * (point2 - point1)).toDouble(), derivativeAlt.toDouble()).toFloat()
        val derivative2 = 5.0f * (point3 - point2)
        return CubicSpline.builder(ridgesFolded, amplifier)
            .addPoint(-1.0f, point1, derivative1)
            .addPoint(-0.4f, point2, min(derivative1, derivative2))
            .addPoint(0.0f, point3, derivative2)
            .addPoint(0.4f, point4, 2.0f * (point4 - point3))
            .addPoint(1.0f, point5, 0.7f * (point5 - point4))
            .build()
    }

    fun netherFloorElevation(inputY: Int): Float {
        val output = ((inputY - seaLevel).toFloat() / ((highestLevel / 2) - seaLevel).toFloat())
        println("floor $inputY")
        println(output)
        println(output * 1000)
        println(round(1000 * output))
        println((round(1000 * output)) / 1000)
        return (round(1000 * output)) / 1000
    }

    fun netherCeilingElevation(inputY: Int): Float {
        val output = ((inputY - roofLevel).toFloat() / ((highestLevel / 2) - roofLevel).toFloat())
        println("ceiling $inputY")
        println(output)
        println(output * 1000)
        println(round(1000 * output))
        println((round(1000 * output)) / 1000)
        return (round(1000 * output)) / 1000
    }
//    fun calculateNetherElevation(inputY: Int): Float {
//        return ((inputY - (roofLevel / 2)) * (1 / roofLevel / 2)).toFloat()
//    }
//val steps = Spline.builder(ridgesFolded, amplifier)
//    .addPoint(-1f, riverValue)
//    .add(-0.4f, lowValue, 0.5f)
//    .add(-0.2f, lowValue + 0.07f, 0.25f)
//    .add(-0.1f, valleyValue, 0.5f)
//    .add(0f, valleyValue + 0.07f, 0.25f)
//    .add(0.1f, middleValue, 0.5f)
//    .add(0.4f, middleValue + 0.07f, 0.25f)
//    .add(0.5f, mountainValue, 0.5f)
//    .add(1f, mountainValue + 0.1f, 0.25f)
//    .build()
}