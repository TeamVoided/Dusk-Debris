package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope

object Offset {
    private const val SEA_LEVEL = 63
    private fun elev(inputY: Int): Float {
        val output = (inputY - SEA_LEVEL) / 256f
        return output
    }

    fun <C, I : ToFloatFunction<C>> createMountain(
        ridgesFolded: I,
        riverbed: Int,
        peak: Int,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val mountainRiverbed = -1f to elev(riverbed)
        val mountainPeak = 1f to elev(peak)
        val mountainMid = -0.2f to (mountainRiverbed.second + mountainPeak.second) / 2
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
        val mushroomPlateaus = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, calculateSlope(riverbed, plateauPeak))
            .add(plateauBank, calculateSlope(plateauBank, plateauPeak))
            .add(plateauPeak, calculateSlope(plateauBank, plateauPeak))

        val flats = -0.4f to elev(78)
        val mushroomFlats = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, calculateSlope(riverbed, flats) * 2)
            .add(flats)

        return Spline.builder(erosion, amplifier)
            .add(-0.7f, mushroomPeaks, -0.1f)
            .add(-0.5f, mushroomPlateaus.build())
            .add(-0.3f, mushroomPlateaus.build())
            .add(-0.2f, mushroomFlats.build()).build()
        //NOTE: add a mushroom swamp
    }
}