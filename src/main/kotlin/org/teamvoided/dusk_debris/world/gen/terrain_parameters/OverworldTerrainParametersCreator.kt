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
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5f)) }

    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    val CONT: List<Float> = listOf(-1.1f, -1.02f, -0.7f, -0.3f, -0.1f, 0.1f, 0.11f, 0.2f, 0.4f, 1f)
    val EROS: List<Float> = listOf(-0.85f, -0.7f, -0.4f, -0.35f, -0.1f, 0.2f, 0.4f, 0.55f, 0.7f)


    fun <C, I : ToFloatFunction<C>> offsetSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        amplified: Boolean = false
    ): Spline<C, I> {
        val amplifier = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val island = Offset.mushroomIsland(erosion, ridgesFolded, amplifier)
        val deepestOcean = Offset.ocean(2f, erosion, ridgesFolded, amplifier)
        val deepOcean = Offset.ocean(1.5f, erosion, ridgesFolded, amplifier)
        val ocean = Offset.ocean(1f, erosion, ridgesFolded, amplifier)
        val oceanShallow = Offset.ocean(0.8f, erosion, ridgesFolded, amplifier)
        val shoreline = Offset.offsetBeach(erosion, ridgesFolded, amplifier)
        val coast = Offset.offsetCont(0f, erosion, ridgesFolded, amplifier)
        val outland = Offset.offsetCont(1 / 3f, erosion, ridgesFolded, amplifier)
        val midland = Offset.offsetCont(2 / 3f, erosion, ridgesFolded, amplifier)
        val inland = Offset.offsetCont(1f, erosion, ridgesFolded, amplifier)

        val offset = Spline.builder(continents, amplifier)
        offset.add(-0.5f, coast)
        offset.add(-0.2f, outland)
        offset.add(0.2f, midland)
        offset.add(0.5f, inland)
        //offset.add(CONT[0], island)
        //offset.add(CONT[1], deepestOcean)
        //offset.add(CONT[2], deepOcean)
        //offset.add(CONT[3], ocean)
        //offset.add(CONT[4], oceanShallow) //shoreline ocean connection
        //offset.add(CONT[5], shoreline)
        //offset.add(CONT[6], coast)   //shoreline outland connection, usually just the same as shoreline, or cliffs
        //offset.add(CONT[7], outland)
        //offset.add(CONT[8], midland)
        //offset.add(CONT[9], inland)
        return offset.build()
    }

    //data class denseFuncs<C, I : ToFloatFunction<C>>(val canyon: I) {}

    fun <C, I : ToFloatFunction<C>> factorSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): Spline<C, I> {
        val amplifiedTransformer = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        val factorErosion = Spline.builder(erosion, amplifiedTransformer)
            .add(0f, 10f)
        val factorRidgesFolded = Spline.builder(ridgesFolded, amplifiedTransformer)
            .add(-0.8f, 6f)
            .add(-0.7f, factorErosion.build())

        return factorErosion.build()
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