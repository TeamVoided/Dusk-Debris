package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset

object OverworldTerrainCreator {
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { if (it < 0f) it * 1.2f else it * 2f }

    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5f)) }

    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    val CONT: List<Float> = listOf(-1.1f, -1.02f, -0.7f, -0.3f, -0.11f, -0.1f, 0.1f, 0.11f, 0.2f, 0.4f, 1f)
    val EROS: List<Float> = listOf(-0.85f, -0.7f, -0.4f, -0.35f, -0.1f, 0.2f, 0.4f, 0.55f, 0.7f)



    /** CONTINENTALNESS VALUES
     *
     * types
     * - Mushroom Island, mushroom island spline
     * - Deepest ocean, deepest point of all oceans
     * - Deep ocean, shallowest point of deep ocean biomes
     * - Ocean, deepest point of ocean biomes
     * - Coast 1, shallowest part of ocean
     * - Coast 2, cliffs over ocean or same as Coast 1
     * - Shoreline 1, beachtop or smaller outland
     * - Shoreline 2, cliffs in beach or same as Shoreline 1
     * - Outland, slightly shorter terrain
     * - Midland, baseline terrain
     * - Inland, tallest points
     **/

    /** VANILLA CONTINENTALNESS VALUES
     *
     * types, repeat names are not mistakes, the game just copies those splines
     * - Mushroom Island, flat mushroom island
     * - Deep Ocean, flat deep ocean
     * - Deep Ocean, flat deep ocean
     * - Ocean, flat ocean
     * - Ocean, flat ocean
     * - Shoreline, flat (for a different reason) shore
     * - Shoreline, flat (for a different reason) shore
     * - Outland, predominantly flat terrain, save a few mountains at low erosion
     * - Midland, mountains loose rivers and create valleys, windswept hills and plateaus start here
     * - Inland, steeper lower erosion terrain
     **/

    fun <C, I : ToFloatFunction<C>> offsetSpline(
        data: TerrainParametersData<C, I>,
        amplified: Boolean
    ): Spline<C, I> {
        data.amplifier = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val island = Offset.mushroomIsland(data)
        val deepestOcean = Offset.ocean(2f, data)
        val deepOcean = Offset.ocean(1.5f, data)
        val ocean = Offset.ocean(1f, data)
        val coast1 = Offset.ocean(0.8f, data)
        val coast2 = Offset.offsetBeach(data)
        val shoreline1 = Offset.offsetBeach(data)
        val shoreline2 = Offset.offsetEros(0.2f, data)
        val outland = Offset.offsetEros(0.4f, data)
        val midland = Offset.offsetEros(0.6f, data)
        val inland = Offset.offsetEros(1f, data)




        val offset = Spline.builder(data.continents, data.amplifier)
        offset.add(-1f, outland)
        offset.add(0.25f, midland)
        offset.add(1f, inland)

        //offset.add(CONT[0], island)
        //offset.add(CONT[1], deepestOcean)
        //offset.add(CONT[2], deepOcean)
        //offset.add(CONT[3], ocean)
        //offset.add(CONT[4], oceanShallow)
        //offset.add(CONT[5], shoreline)
        //offset.add(CONT[6], coast)
        //offset.add(CONT[7], outland)
        //offset.add(CONT[8], midland)
        //offset.add(CONT[9], inland)
        return offset.build()
    }

    fun <C, I : ToFloatFunction<C>> factorSpline(
        data: TerrainParametersDataSimple<C, I>,
        amplified: Boolean
    ): Spline<C, I> {
        data.amplifier = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        val factorErosion = Spline.builder(data.erosion, data.amplifier)
            .add(0f, 10f)
        val factorRidgesFolded = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-0.8f, 6f)
            .add(-0.7f, factorErosion.build())

        return factorErosion.build()
    }

    fun <C, I : ToFloatFunction<C>> jaggednessSpline(
        data: TerrainParametersDataSimple<C, I>,
        amplified: Boolean
    ): Spline<C, I> {
        data.amplifier = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        val spline = Spline.builder(data.erosion, data.amplifier)
            .add(-1f, 0f)
        return spline.build()
    }

    data class TerrainParametersData<C, I : ToFloatFunction<C>>(
        val continents: I,
        val erosion: I,
        val ridges: I,
        val ridgesFolded: I,
        val grandCanyonRF: I,
        var amplifier: ToFloatFunction<Float> = NO_TRANSFORM
    ) {
        fun simple(): TerrainParametersDataSimple<C, I> =
            TerrainParametersDataSimple(continents, erosion, ridges, ridgesFolded, amplifier)
    }

    data class TerrainParametersDataSimple<C, I : ToFloatFunction<C>>(
        val continents: I,
        val erosion: I,
        val ridges: I,
        val ridgesFolded: I,
        var amplifier: ToFloatFunction<Float> = NO_TRANSFORM
    )

    //private data class Values(val contNumb: Float, val ) {}
}