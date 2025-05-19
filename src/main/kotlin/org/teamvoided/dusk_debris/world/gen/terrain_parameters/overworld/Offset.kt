package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator.TerrainParametersData
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator.Eros
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Mountains
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Plateaus.createPlateaus
import kotlin.math.max

object Offset {
    const val SEA_LEVEL = 63
    fun elev(inputY: Number): Float = addSL(inputY.toFloat() - SEA_LEVEL)
    private fun addSL(inputY: Number): Float = inputY.toFloat() / 128f


    fun <C, I : ToFloatFunction<C>> offsetEros(
        contNumber: Float, //increase this number the further inland you go, from 0 to 1 (shoreline to inland)
        data: TerrainParametersData<C, I>,
    ): Spline<C, I> {
        val erosion = Spline.builder(data.erosion, data.amplifier)
            .add(Eros.TallMountain.f, Mountains.mountain(108, 256, true, data))
            .add(Eros.Mountain.f, Mountains.mountain(50, 256, false, data))
            .add(Eros.Plateau1.f, createPlateaus(contNumber, data))
            .add(Eros.Plateau2.f, createPlateaus(contNumber, data))
        if (contNumber > 0.5f) {
            erosion
                .add(Eros.FlatsHigh.f, flatsAndUpper(57, 87, 99, data))
                .add(Eros.FlatsMed.f, flatsAndUpper(52, 75, 85, data))
                .add(Eros.FlatsLow.f, flatsAndUpper(50, 63, 70, data))
        } else {
            erosion
                .add(Eros.FlatsHigh.f, flats(55, 87, data))
                .add(Eros.FlatsMed.f, flats(50, 75, data))
                .add(Eros.FlatsLow.f, flats(48, 63, data))
        }
        return erosion.build()
    }

    fun <C, I : ToFloatFunction<C>> offsetBeach(data: TerrainParametersData<C, I>): Spline<C, I> {
        val spline = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-1f, elev(48))
            .add(-0.4f, 0f)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> flats(
        riverbed: Int,
        bank: Int,
        data: TerrainParametersData<C, I>
    ): Spline<C, I> {
        val river = -1f to elev(riverbed)
        val shore = -0.4f to elev(bank)
        val end = 1f to shore.second * 1.25f

        val riverSlope = calculateSlope(river, shore) * 1.5f

        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(river, riverSlope)
            .add(shore)
            .add(end)
        return flats.build()
    }

    private fun <C, I : ToFloatFunction<C>> flatsAndUpper(
        riverbed: Int,
        bank: Int,
        middle: Int,
        data: TerrainParametersData<C, I>
    ): Spline<C, I> {
        val river = -1f to elev(riverbed)
        val shore = -0.4f to elev(bank)
        val mid = 0f to shore.second * 1.1f
        val high = 0.4f to elev(middle)
        val end = 0.4f to high.second * 1.1f

        val riverSlope = calculateSlope(river, shore) * 1.25f

        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(river, riverSlope)
            .add(shore)
            .add(mid)
            .add(high)
            .add(end)
        return flats.build()
    }

    fun <C, I : ToFloatFunction<C>> ocean(
        depthMult: Float,
        data: TerrainParametersData<C, I>
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
            .add(Eros.Mountain.f, trenches)
            .add(Eros.FlatsHigh.f, moderate)
            .add(Eros.FlatsLow.f, flats)
        return ocean.build()
    }
}