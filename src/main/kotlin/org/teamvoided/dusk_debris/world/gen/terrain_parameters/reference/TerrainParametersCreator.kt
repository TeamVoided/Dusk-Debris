package org.teamvoided.nether_flame.world.biome.source.util.future_reference

import net.minecraft.util.CubicSpline
import net.minecraft.util.Mth
import net.minecraft.util.ToFloatFunction
import net.minecraft.world.level.levelgen.NoiseRouterData
import org.teamvoided.dusk_debris.util.world_helper.add
import kotlin.math.max
import kotlin.math.min

object TerrainParametersCreator {
    //continentalness values
    private val warpedIsland = -1.1f
    private val deepLavaOceanDistant = -1.02f
    private val deepLavaOcean = -0.51f
    private val lavaOceanDistant = -0.44f
    private val lavaOcean = -0.18f
    private val shoreline = -0.15f
    private val outland = -0.1f
    private val inland = 0.25f
    private val inlandExtreme = 1.0f
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    //field_38029
    //if above sea level, terrain height gets multiplied by 2, otherwise, return the same
    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> if (f < 0.0f) f else f * 2.0f }

    //field_38030
    //does a funky if continentalness is greater than -0.1
    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> 1.25f - (6.25f / (f + 5.0f)) }

    //field_38031
    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { f: Float -> f * 2.0f }

    fun TerrainParametersCreator() {

    }

    //method_42056
    fun <C, I : ToFloatFunction<C>> offsetSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
//toFloatFunction4
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val shorelineSpline = createContinentalOffsetSpline(
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
        val inlandSpline = createContinentalOffsetSpline(
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
        val inlandExtremeSpline = createContinentalOffsetSpline(
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
            .addPoint(warpedIsland, 0.044f)
            .addPoint(deepLavaOceanDistant, -0.2222f)
            .addPoint(deepLavaOcean, -0.2222f)
            .addPoint(lavaOceanDistant, -0.12f)
            .addPoint(lavaOcean, -0.12f)
            .addPoint(-0.16f, shorelineSpline)
            .addPoint(shoreline, shorelineSpline)
            .addPoint(outland, outlandSpline)
            .addPoint(inland, inlandSpline)
            .addPoint(inlandExtreme, inlandExtremeSpline)
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
        val toFloatFunction5 = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(continents, NO_TRANSFORM)
            .addPoint(-0.19f, 3.95f)
            .addPoint(
                -0.15f,
                factorErosion(erosion, ridges, ridgesFolded, 6.25f, true, NO_TRANSFORM)
            )
            .addPoint(
                -0.1f,
                factorErosion(erosion, ridges, ridgesFolded, 5.47f, true, toFloatFunction5)
            )
            .addPoint(
                0.03f,
                factorErosion(erosion, ridges, ridgesFolded, 5.08f, true, toFloatFunction5)
            )
            .addPoint(
                0.06f,
                factorErosion(erosion, ridges, ridgesFolded, 4.69f, false, toFloatFunction5)
            )
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
        val toFloatFunction5 = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(continents, toFloatFunction5)
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
                    toFloatFunction5
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
                    toFloatFunction5
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

    //method_42047
    private fun calculateSlope(f: Float, g: Float, h: Float, i: Float): Float {
        return (g - f) / (i - h)
    }

    //method_42050
    private fun <C, I : ToFloatFunction<C>> buildMountainRidgeSplineWithPoints(
        ridgesFolded: I,
        input: Float,
        inland: Boolean,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val spline = CubicSpline.builder(ridgesFolded, amplifier)
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
            spline.addPoint(h, i, q)
            spline.add(o, p)
            spline.add(m, n)
            val r = getOffsetValue(l, input, g)
            val s = calculateSlope(r, k, l, i)
            spline.add(l - 0.01f, r)
            spline.addPoint(l, r, s)
            spline.addPoint(i, k, s)
        } else {
            n = calculateSlope(i, k, h, i)
            if (inland) {
                spline.add(h, max(0.2, i.toDouble()).toFloat())
                spline.addPoint(0.0f, Mth.lerp(0.5f, i, k), n)
            } else {
                spline.addPoint(h, i, n)
            }

            spline.addPoint(i, k, n)
        }

        return spline.build()
    }

    //method_42046
    private fun getOffsetValue(f: Float, g: Float, h: Float): Float {
        val i = 1.17f
        val j = 0.46082947f
        val k = 1.0f - (1.0f - g) * 0.5f
        val l = 0.5f * (1.0f - g)
        val m = (f + i) * j
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
                .addPoint(0.2f, flatlands)
        if (inland) {
            erosionSplineBuilder
                .addPoint(0.4f, flatlands)
                .addPoint(0.45f, windsweptHill)
                .addPoint(0.55f, windsweptHill)
                .addPoint(0.58f, flatlands)
        }
        erosionSplineBuilder.addPoint(0.7f, swamp)
        return erosionSplineBuilder.build()
    }

    //method_42048
    private fun <C, I : ToFloatFunction<C>> ridgeSpline(
        ridgesFolded: I,
        continentalness: Float,
        g: Float,
        h: Float,
        i: Float,
        j: Float,
        b: Float,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val k = max((0.5f * (g - continentalness)).toDouble(), b.toDouble()).toFloat()
        val l = 5.0f * (h - g)
        return CubicSpline.builder(ridgesFolded, amplifier)
            .addPoint(-1.0f, continentalness, k)
            .addPoint(-0.4f, g, min(k, l))
            .addPoint(0.0f, h, l)
            .addPoint(0.4f, i, 2.0f * (i - h))
            .addPoint(1.0f, j, 0.7f * (j - i))
            .build()
    }


}
