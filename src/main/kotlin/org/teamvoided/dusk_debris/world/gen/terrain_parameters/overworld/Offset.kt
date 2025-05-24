package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator.TerrainParametersData
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator.Eros
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Flats.createFlats
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Mountains
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset.Plateaus.createPlateaus

object Offset {
    const val SEA_LEVEL = 63
    fun elev(inputY: Number): Float = addSL(inputY.toFloat() - SEA_LEVEL)
    private fun addSL(inputY: Number): Float = inputY.toFloat() / 128f


    fun <C, I : ToFloatFunction<C>> offsetEros(
        contNumber: Float, //increase this number the further inland you go, from 0 to 1 (shoreline to inland)
        data: TerrainParametersData<C, I>,
    ): Spline<C, I> {
        val plateau = createPlateaus(contNumber, data)
        val flats = createFlats(contNumber, data)
        val erosion = Spline.builder(data.erosion, data.amplifier)
            .add(Eros.TallMountain.f, Mountains.mountain(108, 256, true, data))
            .add(Eros.Mountain.f, Mountains.mountain(50, 256, false, data))
            .add(Eros.Plateau1.f, plateau)
            .add(Eros.Plateau2.f, plateau)
            .add(Eros.Flats1.f, flats)
            .add(Eros.Flats2.f, flats)
        return erosion.build()
    }

    fun <C, I : ToFloatFunction<C>> offsetBeach(data: TerrainParametersData<C, I>): Spline<C, I> {
        val spline = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(-1f, elev(48))
            .add(-0.4f, 0f)
        return spline.build()
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
            .add(Eros.Flats1.f, moderate)
            .add(Eros.Flats2.f, flats)
        return ocean.build()
    }
}