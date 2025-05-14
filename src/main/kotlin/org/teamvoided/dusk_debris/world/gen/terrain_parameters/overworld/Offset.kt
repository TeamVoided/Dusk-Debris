package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainParametersCreator.EROS

object Offset {
    const val SEA_LEVEL = 63
    private fun elev(inputY: Int): Float = (inputY - SEA_LEVEL) / 128f
    private fun addSL(inputY: Int): Float = inputY / 128f

    fun <C, I : ToFloatFunction<C>> offsetBeach(
        erosion: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, elev(48))
            .add(-0.4f, 0f)
        return spline.build()
    }

    fun <C, I : ToFloatFunction<C>> offsetCont(
        contNumber: Float, //increase this number the further inland you go, from 0 to 1 (outland to inland)
        erosion: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val spline = Spline.builder(erosion, amplifier)
            .add(0f, createCanyon(1.5f - 0.5f * contNumber, ridgesFolded, amplifier))
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> createCanyon(
        contNumber: Float,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val carve = (-contNumber / 1.5f) + 0.5f

        val riverbed = -1f to elev(48)
        val riverbank = -0.8f to elev(80)
        val plateau1 = carve - 0.01f to elev(128)
        val plateau2 = carve to plateau1.second
        val final = 1f to elev(150)

        val cliffBaseSlope = calculateSlope(riverbed, plateau2)
        val plateauSlope = calculateSlope(plateau2, final)

        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed)
            .add(riverbank, cliffBaseSlope)
            .add(plateau1, cliffBaseSlope)
            .add(plateau2, plateauSlope)
            .add(final, plateauSlope)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> createMountain(
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

    fun <C, I : ToFloatFunction<C>> ocean(
        depthMult: Float,
        erosion: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {

        val trenchBottom = -1f to addSL(-100) * depthMult
        val trenchTop = 0.5f to addSL(-60) * depthMult
        val trenches = Spline.builder(ridgesFolded, amplifier)
            .add(trenchBottom)
            .add(-0.4f, addSL(-70) * depthMult, calculateSlope(trenchBottom, trenchTop))
            .add(trenchTop).build()
        val moderate = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, addSL(-80) * depthMult)
            .add(0f, addSL(-60) * depthMult)
            .add(1f, addSL(-50) * depthMult).build()
        val flats = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, addSL(-60) * depthMult)
            .add(-0.4f, addSL(-45) * depthMult)
            .add(1f, addSL(-30) * depthMult).build()


        val ocean = Spline.builder(erosion, amplifier)
            .add(EROS[0], trenches)
            .add(EROS[4], moderate)
            .add(EROS.last(), flats)
        return ocean.build()
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