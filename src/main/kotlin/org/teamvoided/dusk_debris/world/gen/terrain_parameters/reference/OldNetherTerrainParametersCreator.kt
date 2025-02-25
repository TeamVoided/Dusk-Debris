package org.teamvoided.nether_flame.world.biome.source.util

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Spline
import net.minecraft.world.gen.noise.NoiseRouterData
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
    ): Spline<C, I> {
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
        return Spline.builder(continents, amplifiedTransformer)
            .method_41294(warpedIsland, netherFloorElevation(180))
            .method_41294(lavaOceanDeep, netherFloorElevation(-18))
            .method_41294(lavaOcean, netherFloorElevation(24))
            .method_41295(shoreline, midlandSpline)
            .method_41295(outland, midlandSpline)
            .method_41295(inland, inlandSpline)
            .method_41295(inlandExtreme, inlandSpline)
            .build()
    }

    fun <C, I : ToFloatFunction<C>> offsetCeilingSpline(
        dropCeiling: I,
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) this.OFFSET_AMPLIFIED else NO_TRANSFORM
        return Spline.builder(dropCeiling, amplifiedTransformer)
            .method_41294(-1f, netherCeilingElevation(230))
            .method_41294(0f, netherCeilingElevation(210))
            .method_41294(1f, netherCeilingElevation(180))
            .build()
    }

    //method_42055
    fun <C, I : ToFloatFunction<C>> factorSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        return Spline.builder(continents, NO_TRANSFORM)
            .method_41294(-0.19f, 3.95f)
            .method_41295(
                -0.15f,
                factorErosion(erosion, ridges, ridgesFolded, 6.25f, true, NO_TRANSFORM)
            )
//            .method_41295(
//                -0.1f,
//                factorErosion(erosion, ridges, ridgesFolded, 5.47f, true, amplifiedTransformer)
//            )
//            .method_41295(
//                0.03f,
//                factorErosion(erosion, ridges, ridgesFolded, 5.08f, true, amplifiedTransformer)
//            )
//            .method_41295(
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
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        return Spline.builder(continents, amplifiedTransformer)
            .method_41294(-0.11f, 0.0f)
            .method_41295(
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
            .method_41295(
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
    ): Spline<C, I> {
        val j = -0.5775f
        val jaggedExtreme = jaggednessRidges(ridges, ridgesFolded, f, h, amplifier)
        val jagged = jaggednessRidges(ridges, ridgesFolded, g, i, amplifier)
        return Spline.builder(erosion, amplifier)
            .method_41295(-1.0f, jaggedExtreme)
            .method_41295(-0.78f, jagged)
            .method_41295(-0.5775f, jagged)
            .method_41294(-0.375f, 0.0f)
            .build()
    }

    //method_42052
    private fun <C, I : ToFloatFunction<C>> jaggednessRidges(
        ridges: I,
        ridgesFolded: I,
        f: Float,
        g: Float,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val h = NoiseRouterData.getPeaksAndValleys(0.4f)
        val i = NoiseRouterData.getPeaksAndValleys(0.56666666f)
        val j = (h + i) / 2.0f
        val builder = Spline.builder(ridgesFolded, amplifier)
        builder.method_41294(h, 0.0f)
        if (g > 0.0f) {
            builder.method_41295(j, jaggednessWeirdness(ridges, g, amplifier))
        } else {
            builder.method_41294(j, 0.0f)
        }

        if (f > 0.0f) {
            builder.method_41295(1.0f, jaggednessWeirdness(ridges, f, amplifier))
        } else {
            builder.method_41294(1.0f, 0.0f)
        }
        return builder.build()
    }

    //method_42049
    private fun <C, I : ToFloatFunction<C>> jaggednessWeirdness(
        ridgesFolded: I,
        f: Float,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val g = 0.63f * f
        val h = 0.3f * f
        return Spline.builder(ridgesFolded, amplifier)
            .method_41294(-0.01f, g)
            .method_41294(0.01f, h)
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
    ): Spline<C, I> {
        val spline = Spline.builder(ridges, amplifier)
            .method_41294(-0.2f, 6.3f)
            .method_41294(0.2f, f)
            .build()
        val builder = Spline.builder(erosion, amplifier)
            .method_41295(-0.6f, spline)
            .method_41295(
                -0.5f,
                Spline.builder(ridges, amplifier)
                    .method_41294(-0.05f, 6.3f)
                    .method_41294(0.05f, 2.67f)
                    .build()
            )
            .method_41295(-0.35f, spline)
            .method_41295(-0.25f, spline)
            .method_41295(
                -0.1f,
                Spline.builder(ridges, amplifier)
                    .method_41294(-0.05f, 2.67f)
                    .method_41294(0.05f, 6.3f)
                    .build()
            )
            .method_41295(0.03f, spline)
        val spline2: Spline<*, *>
        val spline3: Spline<*, *>
        if (isOutland) {
            spline2 =
                Spline.builder(ridges, amplifier)
                    .method_41294(0.0f, f)
                    .method_41294(0.1f, 0.625f)
                    .build()
            spline3 =
                Spline.builder(ridgesFolded, amplifier)
                    .method_41294(-0.9f, f)
                    .method_41295(-0.69f, spline2)
                    .build()
            builder.method_41294(0.35f, f)
                .method_41295(0.45f, spline3)
                .method_41295(0.55f, spline3)
                .method_41294(0.62f, f)
        } else {
            spline2 = Spline.builder(ridgesFolded, amplifier)
                .method_41295(-0.7f, spline)
                .method_41294(-0.15f, 1.37f)
                .build()
            spline3 =
                Spline.builder(ridgesFolded, amplifier)
                    .method_41295(0.45f, spline)
                    .method_41294(0.7f, 1.56f)
                    .build()
            builder.method_41295(0.05f, spline3)
                .method_41295(0.4f, spline3)
                .method_41295(0.45f, spline2)
                .method_41295(0.55f, spline2)
                .method_41294(0.58f, f)
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
    ): Spline<C, I> {
        val builder = Spline.builder(ridgesFolded, amplifier)
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
            builder.add(h, i, q)
            builder.method_41294(o, p)
            builder.method_41294(m, n)
            val r = getOffsetValue(l, input, g)
            val s = calculateSlope(r, k, l, i)
            builder.method_41294(l - 0.01f, r)
            builder.add(l, r, s)
            builder.add(i, k, s)
        } else {
            n = calculateSlope(i, k, h, i)
            if (inland) {
                builder.method_41294(h, max(0.2, i.toDouble()).toFloat())
                builder.add(0.0f, MathHelper.lerp(0.5f, i, k), n)
            } else {
                builder.add(h, i, n)
            }
            builder.add(1f, k, n)
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
    ): Spline<C, I> {
        val l = 0.6f
        val m = 0.5f
        val n = 0.5f
        val mountainTallest = buildMountainRidgeSplineWithPoints(
            ridgesFolded,
            MathHelper.lerp(highestValue, 0.6f, 1.5f),
            inlandMountain,
            amplifier
        )
        val mountain = buildMountainRidgeSplineWithPoints(
            ridgesFolded,
            MathHelper.lerp(highestValue, 0.6f, 1.0f),
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
            MathHelper.lerp(0.5f, 0.5f, 0.5f) * highestValue,
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
        val windsweptHill = Spline.builder(ridgesFolded, amplifier)
            .method_41294(-1.0f, continentalness)
            .method_41295(-0.4f, flatlands)
            .method_41294(0.0f, highValue + 0.07f)
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
            Spline.builder(erosion, amplifier)
                .method_41295(-0.85f, mountainTallest)
                .method_41295(-0.7f, mountain)
                .method_41295(-0.4f, mountainInland)
                .method_41295(-0.35f, plateau)
                .method_41295(-0.1f, plateauInland)
//                .method_41295(0.2f, flatlands)
        if (inland) {
            erosionSplineBuilder
//                .method_41295(0.4f, flatlands)
                .method_41295(0.45f, windsweptHill)
                .method_41295(0.55f, windsweptHill)
                .method_41295(0.58f, flatlands)
        }
        erosionSplineBuilder.method_41295(0.7f, swamp)
        return erosionSplineBuilder.build()
    }

    private fun <C, I : ToFloatFunction<C>> ridgeSpline2(
        ridgesFolded: I,
        deepestPoint: Float,
        highestPoint: Float,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val derivative = ((highestPoint - deepestPoint) / 2)
        return Spline.builder(ridgesFolded, amplifier)
            .add(-1.0f, deepestPoint, derivative)
            .add(1.0f, highestPoint, derivative)
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
    ): Spline<C, I> {
        val derivative1 = max((0.5f * (point2 - point1)).toDouble(), derivativeAlt.toDouble()).toFloat()
        val derivative2 = 5.0f * (point3 - point2)
        return Spline.builder(ridgesFolded, amplifier)
            .add(-1.0f, point1, derivative1)
            .add(-0.4f, point2, min(derivative1, derivative2))
            .add(0.0f, point3, derivative2)
            .add(0.4f, point4, 2.0f * (point4 - point3))
            .add(1.0f, point5, 0.7f * (point5 - point4))
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
//    .method_41294(-1f, riverValue)
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