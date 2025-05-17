package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Plateaus

object OverworldTerrainCreator {
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { if (it < 0f) it * 1.2f else it * 2f }

    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5f)) }

    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    val EROS: List<Float> = listOf(-0.85f, -0.7f, -0.4f, -0.35f, -0.2f, -0.1f, 0.2f, 0.4f, 0.55f, 0.7f)

    /** VANILLA CONTINENTALNESS VALUES
     *
     * types, repeat names are not mistakes, the game just copies those splines
     * - -1.1 - Mushroom Island, flat mushroom island
     * - -1.02 - Deep Ocean, flat deep ocean
     * - -0.51 - Deep Ocean, flat deep ocean
     * - -0.44 - Ocean, flat ocean
     * - -0.18 - Ocean, flat ocean
     * - -0.16 - Shoreline, flat (for a different reason) shore
     * - -0.15 - Shoreline, flat (for a different reason) shore
     * - -0.1 - Outland, predominantly flat terrain, save a few mountains at low erosion
     * - 0.25 - Midland, mountains loose rivers and create valleys, windswept hills and plateaus start here
     * - 1 - Inland, steeper lower erosion terrain
     *
     * CONTINENTALNESS VALUES
     *
     * types
     * - -1.1 - Mushroom Island, mushroom island spline
     * - -1.02 - Deepest ocean, deepest point of all oceans
     * - -0.7 - Deep ocean, shallowest point of deep ocean biomes
     * - -0.3 - Ocean, deepest point of ocean biomes
     * - -0.11 - Coast 1, shallowest part of ocean
     * - -0.1 - Coast 2, cliffs over ocean or same as Coast 1
     * - 0.1 - Shoreline 1, beachtop or smaller outland
     * - 0.11 - Shoreline 2, cliffs in beach or same as Shoreline 1
     * - 0.2 - Outland, slightly shorter terrain
     * - 0.4 - Midland, baseline terrain
     * - 1 - Inland, tallest points
     **/
    enum class Cont(val f: Float) {
        MushroomIsland(-1.1f),
        DeepestOcean(-1.02f),
        DeepOcean(-0.7f),
        Ocean(-0.3f),
        Coast1(-0.11f),
        Coast2(-0.1f),
        Shoreline1(0.1f),
        Shoreline2(0.11f),
        Outland(0.2f),
        Midland(0.4f),
        Inland(1f);
    }

    /** VANILLA EROSION VALUES
     *
     * types, repeat names are not mistakes, the game just copies those splines
     * - -0.85 Tall Mountains
     * - -0.7 Mountains
     * - -0.4 Mountains Inland
     * - -0.35 Plateaus Inland
     * - -0.1 Valley inland
     * - 0.2 Flats
     * - 0.4 Flats, no defined outland
     * - 0.45 Windswept Hills Inland, no defined outland
     * - 0.55 Windswept Hills Inland, no defined outland
     * - 0.58 Flats, no defined outland
     * - 0.7 Swamps
     **/

    enum class Eros(val f: Float) {
        TallMountain(-0.85f),
        Mountain(-0.7f),
        MountainInland(-0.4f),
        Plateau1(-0.35f),
        Plateau2(-0.2f),
        Valley(-0.1f),
        FlatsHigh(0.2f),
        FlatsMed(0.4f),
        FlatsLow(0.55f),
        Swamp(0.7f);
    }

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

        offset.add(Cont.MushroomIsland.f, island)
        offset.add(Cont.DeepestOcean.f, deepestOcean)
        offset.add(Cont.DeepOcean.f, deepOcean)
        offset.add(Cont.Ocean.f, ocean)
        offset.add(Cont.Coast1.f, coast1)
        offset.add(Cont.Coast2.f, coast2)
        offset.add(Cont.Shoreline1.f, shoreline1)
        offset.add(Cont.Shoreline2.f, shoreline2)
        offset.add(Cont.Outland.f, outland)
        offset.add(Cont.Midland.f, midland)
        offset.add(Cont.Inland.f, inland)
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

    fun <C, I : ToFloatFunction<C>> undergroundRiverCondition(y: I, data: TerrainParametersData<C, I>): Spline<C, I> {
        val elevation = Spline.builder(y)
            .add(54f, 0f)
            .add(55f, 1f)
            .add(75f, 1f)
            .add(76f, 0f)
            .build()
        val ridgesF = Spline.builder(data.ridgesFolded)
            .add(-0.75f, elevation)
            .add(-0.7f, 0f)
            .build()
        val plateauType = Spline.builder(data.plateauType)
            .add(Plateaus.PlatType.Plateau.max, 0f)
            .add(Plateaus.PlatType.Cave.min, ridgesF)
            .build()
        val erosion = Spline.builder(data.erosion)
            .add(Eros.MountainInland.f, 0f)
            .add(Eros.Plateau1.f, plateauType)
            .add(Eros.Plateau2.f, plateauType)
            .add(Eros.Valley.f, 0f)
            .build()
        val continents = Spline.builder(data.continents)
            .add(Cont.Coast2.f, 0f)
            .add(Cont.Shoreline1.f, erosion)
            .build()
        return continents
    }

    data class TerrainParametersData<C, I : ToFloatFunction<C>>(
        val continents: I,
        val erosion: I,
        val ridges: I,
        val ridgesFolded: I,
        val plateauType: I,
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
}