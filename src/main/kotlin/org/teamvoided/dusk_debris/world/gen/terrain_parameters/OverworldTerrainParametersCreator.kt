package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
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

    val CONT: List<Float> = listOf(-1.1f, -1.02f, -0.7f, -0.3f, -0.1f, 0.1f, 0.11f, 0.2f, 0.4f, 1f)
    val EROS: List<Float> = listOf(-0.85f, -0.7f, -0.4f, -0.35f, -0.1f, 0.2f, 0.4f, 0.55f, 0.7f)

    fun <C, I : ToFloatFunction<C>> offsetSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean = false
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val island = Offset.mushroomIsland(erosion, ridgesFolded, amplifiedTransformer)
        val deepOcean = -60 / 256f
        val ocean = -30 / 256f
        val shoreline = 0f
        val shoreline2 = 0f
        val outland = 20 / 256f
        val midland = 60 / 256f
        val inland = 100 / 256f

        val offset = Spline.builder(continents, amplifiedTransformer)
        offset.add(CONT[0], island)
        offset.add(CONT[1], deepOcean)
        offset.add(CONT[2], deepOcean)
        offset.add(CONT[3], ocean)
        offset.add(CONT[4], ocean)      //shoreline ocean connection
        offset.add(CONT[5], shoreline)
        offset.add(CONT[6], shoreline2) //shoreline outland connection, usually just the same as shoreline, or cliffs
        offset.add(CONT[7], outland)
        offset.add(CONT[8], midland)
        offset.add(CONT[9], inland)
        return offset.build()
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

        return jaggedErosion.build()
    }

    fun <C, I : ToFloatFunction<C>> jaggednessSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        val spline = Spline.builder(erosion, amplifiedTransformer)
            .add(-1f, 0f)
        return spline.build()
    }
}