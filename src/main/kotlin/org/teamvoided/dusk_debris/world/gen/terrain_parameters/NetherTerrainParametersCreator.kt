package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import kotlin.math.round

object NetherTerrainParametersCreator {
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    //continentalness values
    private const val WARPED_ISLAND = -1.1f
    private const val LAVA_OCEAN_DEEP = -1.02f
    private const val LAVA_OCEAN = -0.25f
    private const val SHORELINE = -0.15f
    private const val OUTLAND = -0.1f
    private const val INLAND = 0.25f
    private const val INLAND_EXTREME = 1f
    private const val HIGHEST_LEVEL = 256
    private const val LOWEST_LEVEL = 0
    private const val ROOF_LEVEL = HIGHEST_LEVEL - 32
    private const val SEA_LEVEL = LOWEST_LEVEL + 32

    //if above sea level, terrain height gets multiplied by 2, otherwise, return the same
    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { if (it < 0f) it else it * 2f }

    //does a funky if continentalness is greater than -0.1
    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5.0f)) }

    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    fun NetherTerrainParametersCreator() {
    }

    fun <C, I : ToFloatFunction<C>> offsetFloorSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean = false
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val shoreline = offsetFloor(0f, erosion, ridgesFolded, amplifiedTransformer)

        //OFFSET CONTINENTALNESS
        return Spline.builder(continents, amplifiedTransformer)
//            .add(WARPED_ISLAND, nFloor(180))
//            .add(LAVA_OCEAN_DEEP, nFloor(-18))
//            .add(LAVA_OCEAN, nFloor(24))
            .add(SHORELINE, shoreline)
//            .add(OUTLAND, midlandSpline)
//            .add(INLAND, inlandSpline)
//            .add(INLAND_EXTREME, inlandSpline)
            .build()
    }

    fun <C, I : ToFloatFunction<C>> offsetCeilingSpline(
        dropCeiling: I,
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM
        return Spline.builder(dropCeiling, amplifiedTransformer)
            .add(-1f, nCeil(230))
//            .add(0f, nCeil(210))
//            .add(1f, nCeil(180))
            .build()
    }

    //method_42054
    fun <C, I : ToFloatFunction<C>> factorSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        val spline = Spline.builder(erosion, amplifiedTransformer)
            .add(0f, 1f)
        return spline.build()
    }
    fun <C, I : ToFloatFunction<C>> jaggednessSpline (
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        val spline = Spline.builder(erosion, amplifiedTransformer)
            .add(0f, 0f)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> offsetFloor(
        continents: Float,
        erosion: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, nFloor(21))
            .add(-0.8f, nFloor(48))
            .add(-0.5f, nFloor(64))
            .add(1f, nFloor(128))
        return spline.build()
    }

//    private fun <C, I : ToFloatFunction<C>> spline(
//        densityFunction: I,
//        amplifier: ToFloatFunction<Float>,
//        vararg splines: Pair<Float, Float>
//    ): Spline<C, I> {
//        val spline = Spline.builder(densityFunction, amplifier)
//        splines.forEach {
//            spline.add(it.first, it.second)
//        }
//        return spline.build()
//    }

    private fun calculateSlope(value1: Float, value2: Float, point1: Float, point2: Float): Float {
        return (value2 - value1) / (point2 - point1)
    }

    private fun nFloor(inputY: Int): Float {
        val output = ((inputY - SEA_LEVEL).toFloat() / ((HIGHEST_LEVEL / 2) - SEA_LEVEL).toFloat())
//        println("floor $inputY")
        return (round(1000f * output)) / 1000f
    }

    private fun nCeil(inputY: Int): Float {
        val output = ((inputY - ROOF_LEVEL).toFloat() / ((HIGHEST_LEVEL / 2) - ROOF_LEVEL))
//        println("ceiling $inputY")
        return (round(1000f * output)) / 1000f
    }
}