package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainParametersCreator.EROS

object Offset {
    const val SEA_LEVEL = 63
    private fun elev(inputY: Int): Float {
        val output = (inputY - SEA_LEVEL) / 256f
        return output
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

    fun <C, I : ToFloatFunction<C>> createMountain(
        ridgesFolded: I,
        riverbed: Int,
        peak: Int,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val mountainRiverbed = -1f to elev(riverbed)
        val mountainPeak = 1f to elev(peak)
        val mountainMid = -0.2f to (mountainRiverbed.second + mountainPeak.second) / 2f
        val mountainSlope = calculateSlope(mountainRiverbed, mountainPeak)
        val mountain = Spline.builder(ridgesFolded, amplifier)
            .add(mountainRiverbed, mountainSlope)
            .add(mountainMid, mountainSlope)
            .add(mountainPeak, mountainSlope)
        return mountain.build()
    }

    fun <C, I : ToFloatFunction<C>> mushroomIsland(
        erosion: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val riverbed = -1f to elev(52)
        val mushroomPeaks = createMountain(ridgesFolded, 48, 200, amplifier)

        val plateauBank = -0.4f to elev(92)
        val plateauPeak = 1f to elev(111)
        val plateauSlope = calculateSlope(plateauBank, plateauPeak)
        val mushroomPlateaus = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, calculateSlope(riverbed, plateauPeak))
            .add(plateauBank, plateauSlope)
            .add(plateauPeak, plateauSlope)

        val flats = -0.4f to elev(78)
        val mushroomFlats = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, calculateSlope(riverbed, flats) * 2)
            .add(flats)

        return Spline.builder(erosion, amplifier)
            .add(EROS[1], mushroomPeaks, -0.1f)
            .add(EROS[2], mushroomPlateaus.build())
            .add(EROS[3], mushroomPlateaus.build())
            .add(EROS[5], mushroomFlats.build()).build()
        //NOTE: add a mushroom swamp
    }
}