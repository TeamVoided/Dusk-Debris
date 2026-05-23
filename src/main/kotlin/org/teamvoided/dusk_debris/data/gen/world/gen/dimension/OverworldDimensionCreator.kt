package org.teamvoided.dusk_debris.data.gen.world.gen.dimension

import org.teamvoided.dusk_debris.util.world_helper.mult
import org.teamvoided.dusk_debris.util.world_helper.range
import org.teamvoided.dusk_debris.util.world_helper.zeroRange

object OverworldDimensionCreator {

    // - - - depth - - - //
    val dTop = zeroRange
    val dCave = range(0.2f, 0.9f)
    val dBot = range(1)
    val dCaveDeep = range(1.1f)
    val dSurface = listOf(dTop, dBot)

    // - - - continentalness - - - //
    val cMushIsle = range(-1.5f, -1.05f)
    val cDeepOcean = range(-1.05f, -0.45f)
    val cOcean = range(-0.45f, -0.11f)
    val cBeach = range(-0.1f)
    val cOutland = range(0.1f, 0.2f)
    val cMidland = range(0.2f, 0.4f)
    val cInland = range(0.4f, 1)
    val cRiver = range(cOutland.min, cInland.max)

    // - - - erosion - - - //
    val eMount = range(-1, -0.78f)
    val eSwamp = range(0.8f, 1)

    // - - - temperature - - - //
    val tFroz = range(-1, -0.45f)
    val tCold = range(-0.45f, -0.15f)
    val tMild = range(-0.15f, 0.2f)
    val tWarm = range(0.2f, 0.55f)
    val tHot = range(0.55f, 1)

    // - - - humidity - - - //
    val hFroz = range(-1, -0.35f)
    val hCold = range(-0.35f, -0.1f)
    val hMild = range(-0.1f, 0.1f)
    val hWarm = range(0.1f, 0.3f)
    val hOldGrowth = range(0.3f, 1)
    val hLushCave = range(0.7f, 1)

    // - - - weirdness - - - // min-max is -1 1
    val wValley = range(-0.05f, 0.05f)
    val wLowP = range(0.05f, 0.2f)
    val wMedP = range(0.2f, 0.4f)
    val wHighP = range(0.4f, 0.8f)
    val wPeakP = range(0.8f, 1)
    val wLowN = wLowP.mult(-1f)
    val wMedN = wMedP.mult(-1f)
    val wHighN = wHighP.mult(-1f)
    val wPeakN = wPeakP.mult(-1f)
    val wShatter = range(1, 2)
    val wHills = range(-2, -1)
}