package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator.EROS
import kotlin.math.max

object Offset {
    const val SEA_LEVEL = 63
    private fun elev(inputY: Number): Float = (inputY.toFloat() - SEA_LEVEL) / 128f
    private fun addSL(inputY: Number): Float = inputY.toFloat() / 128f

    fun <C, I : ToFloatFunction<C>> offsetBeach(data: OverworldTerrainCreator.TerrainParametersData<C, I>): Spline<C, I> {
        val spline = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-1f, elev(48))
            .add(-0.4f, 0f)
        return spline.build()
    }

    fun <C, I : ToFloatFunction<C>> offsetEros(
        contNumber: Float, //increase this number the further inland you go, from 0 to 1 (outland to inland)
        data: OverworldTerrainCreator.TerrainParametersData<C, I>,
    ): Spline<C, I> {
        val spline = Spline.builder(data.erosion, data.amplifier)
            .add(-1f, createMountain(108, 256, true, data))
            .add(-0.6f, createMountain(50, 256, false, data))
            .add(-0.55f, createCanyon(contNumber, data))
            .add(-0.35f, createCanyon(contNumber, data))
        return spline.build()
    }

    /**CANYON TYPES
     * Canyon: default, just a plateau
     * Eroded: plateau but the start is halfway inland (no change on riverbank, create a slope)
     * Layered: two plateaus, one that starts at the Eroded point, and one half as high at the Default point
     **/
    private fun <C, I : ToFloatFunction<C>> createCanyon(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val carve = if (contNumber > 0.5f) 1f else 0f

        val riverbed = -1f to (elev(30 + carve * 8))
        val plateau1 = -0.7f to elev(80 + carve * 20)
        val plateau2 = -0.3f to elev(108 + carve * 20)
        val final = 1f to elev(120 + carve * 20)

        val plateauSlope = calculateSlope(plateau2, final)

        val spline = Spline.builder(data.grandCanyonRF, data.amplifier)
            .add(riverbed)
            //.add(riverbank)
            .add(plateau2, 0f)
            .add(final, plateauSlope)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> createMountain(
        riverbed: Int,
        peak: Int,
        valley: Boolean,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val mountainRiverbed = -1f to elev(riverbed)
        val mountainPeak = 1f to elev(peak)
        val mountainSlope = calculateSlope(mountainRiverbed, mountainPeak)

        val mountain = Spline.builder(data.ridgesFolded, data.amplifier)
        if (valley) {
            mountain.add(mountainRiverbed.first, max(0.2f, mountainRiverbed.second))
            mountain.add(0.0f, (mountainRiverbed.second + mountainPeak.second) / 2f, mountainSlope)
        } else {
            mountain.add(mountainRiverbed, mountainSlope)
        }

        mountain.add(mountainPeak, mountainSlope)
        return mountain.build()
    }

    fun <C, I : ToFloatFunction<C>> ocean(
        depthMult: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {

        val trenchBottom = -1f to addSL(-100) * depthMult
        val trenchTop = 0.5f to addSL(-60) * depthMult
        val trenches = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(trenchBottom)
            .add(-0.4f, addSL(-70) * depthMult, calculateSlope(trenchBottom, trenchTop))
            .add(trenchTop).build()
        val moderate = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-1f, addSL(-80) * depthMult)
            .add(0f, addSL(-60) * depthMult)
            .add(1f, addSL(-50) * depthMult).build()
        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-1f, addSL(-60) * depthMult)
            .add(-0.4f, addSL(-45) * depthMult)
            .add(1f, addSL(-30) * depthMult).build()


        val ocean = Spline.builder(data.erosion, data.amplifier)
            .add(EROS[0], trenches)
            .add(EROS[4], moderate)
            .add(EROS.last(), flats)
        return ocean.build()
    }

    fun <C, I : ToFloatFunction<C>> mushroomIsland(data: OverworldTerrainCreator.TerrainParametersData<C, I>): Spline<C, I> {
        val riverbed = -1f to elev(52)
        val mushroomPeaks = createMountain(48, 200, false, data)

        val plateauBank = -0.4f to elev(92)
        val plateauPeak = 1f to elev(111)
        val plateauSlope = calculateSlope(plateauBank, plateauPeak)
        val mushroomPlateaus = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(riverbed, calculateSlope(riverbed, plateauPeak))
            .add(plateauBank, plateauSlope)
            .add(plateauPeak, plateauSlope)

        val flats = -0.4f to elev(78)
        val mushroomFlats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(riverbed, calculateSlope(riverbed, flats) * 2)
            .add(flats)

        return Spline.builder(data.erosion, data.amplifier)
            .add(EROS[1], mushroomPeaks, -0.1f)
            .add(EROS[2], mushroomPlateaus.build())
            .add(EROS[3], mushroomPlateaus.build())
            .add(EROS[5], mushroomFlats.build()).build()
        //NOTE: add a mushroom swamp
    }
}