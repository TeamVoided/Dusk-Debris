package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.nether.OffsetFloor
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset

object OverworldTerrainParametersCreator {
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    //if above sea level, terrain height gets multiplied by 2, otherwise, return the same
    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { if (it < 0f) it else it * 2f }

    //does a funky if continentalness is greater than -0.1
    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5.0f)) }

    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    fun <C, I : ToFloatFunction<C>> offsetSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        ridges: I,
        amplified: Boolean = false
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val island = Offset.mushroomIsland(erosion, ridgesFolded, amplifiedTransformer)
        val deepOcean = -0.45f
        val ocean = -0.12f
        val shoreline = 0f
        val outland = 0.1f
        val midland = 0.25f
        val inland = 0.5f


        //OFFSET CONTINENTALNESS
        return Spline.builder(continents, amplifiedTransformer)
            .add(-1.1f, island) //mushroom island
            .add(-1.02f, deepOcean) //deep ocean
            .add(-0.7f, deepOcean) //deep ocean
            .add(-0.3f, ocean) //ocean
            .add(-0.1f, ocean) //shoreline ocean connection
            .add(0.1f, shoreline) //shoreline
            .add(0.11f, shoreline) //shoreline outland connection, usually just the same as shoreline
            .add(0.2f, outland) //outland
            .add(0.4f, midland) //midland
            .add(1f, inland) //inland
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
        val jaggedErosion = Spline.builder(erosion, amplifiedTransformer)
            .add(0f, 3f)
        val jaggedRidgesFolded = Spline.builder(ridgesFolded, amplifiedTransformer)
            .add(-0.8f, 6f)
            .add(-0.7f, jaggedErosion.build())
        return jaggedRidgesFolded.build()
    }

    fun <C, I : ToFloatFunction<C>> jaggednessSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        jaggedness: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        val spline = Spline.builder(jaggedness, amplifiedTransformer)
            .add(-1f, 0f)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> offset(
        howFarInland: Float,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val spline = Spline.builder(erosion, amplifier)
            .add(-0.85f, 0f)
            .add(0.7f, 0f)
        return spline.build()
    }
}