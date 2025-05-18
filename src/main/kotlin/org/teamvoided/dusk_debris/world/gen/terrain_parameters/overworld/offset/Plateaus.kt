package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset

object Plateaus {
    /** PLATEAU TYPE NOTES (see DuskDensityFunctions.class for descriptions)
     *
     * layered canyon, -0.75 -1, two plateaus, one that starts at the Eroded point, and one half as high at the default plateay, layeredCanyon()
     *
     * eroded canyon, -0.6 -0.7, plateau but the start is halfway inland (no change on riverbank, create a slope), canyon()
     *
     * canyon, -0.3 -0.5, just a plateau with different RF input, plateau()
     *
     * plateau, 0.5 -0.2, just a plateau, plateau()
     *
     * cave river, 1 0.55, a plateau with no river, plateauCave()
     *
     **/
    enum class PlatType(val min: Float, val max: Float = min) {
        Layered(-0.8f),
        Eroded(-0.6f, -0.45f),
        Canyon(-0.3f, -0.2f),
        Plateau(0f, 0.5f),
        Cave(0.7f);
    }

    fun <C, I : ToFloatFunction<C>> createPlateaus(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val cave = plateau(contNumber, data, true)
        val plateau = plateau(contNumber, data)
        val canyon = plateau(contNumber, data, false, data.grandCanyonRF)
        val eroded = canyon(contNumber, data)
        val layered = canyon(contNumber, data)

        val spline = Spline.builder(data.plateauType, data.amplifier)
            .add(PlatType.Layered.max, layered)
            .add(PlatType.Eroded.min, eroded)
            .add(PlatType.Eroded.max, eroded)
            .add(PlatType.Canyon.min, canyon)
            .add(PlatType.Canyon.max, canyon)
            .add(PlatType.Plateau.min, plateau)
            .add(PlatType.Plateau.max, plateau)
            .add(PlatType.Cave.min, cave)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> plateau(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>,
        depress: Boolean = false,
        rf: I = data.ridgesFolded
    ): Spline<C, I> {
        val riverbed = -1f to (Offset.elev(if (depress) 30 + contNumber * 8 else 80 + contNumber * 16))
        val plateau = -0.4f to Offset.elev(108 + contNumber * 20)
        val plateauEnd = 0.4f to plateau.second
        val final = 1f to Offset.elev(120 + contNumber * 20)

        val riverbedSlope = calculateSlope(riverbed, plateauEnd)
        val finalSlope = calculateSlope(plateauEnd, final)

        val spline = Spline.builder(rf, data.amplifier)
            .add(riverbed, riverbedSlope)
            .add(plateau)
            .add(plateauEnd)
            .add(final, finalSlope)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> canyon(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val carve = if (contNumber > 0.5f) 1f else 0f

        val riverbed = -1f to Offset.elev(30 + carve * 8)
        val riverbank = -0.75f to Offset.elev(62 + carve * 5)
        val plateau1Start = -0.2f to Offset.elev(80 + carve * 20)
        val plateau1End = 0.4f to plateau1Start.second
        val plateau2Start = 0.6f to Offset.elev(108 + carve * 20)
        val plateau2End = 1f to plateau2Start.second

        val riverbedSlope = calculateSlope(riverbed, plateau1End)


        val spline = Spline.builder(data.grandCanyonRF, data.amplifier)
            .add(riverbed, riverbedSlope)
            .add(riverbank)
            .add(plateau1Start)
            .add(plateau1End)
            .add(plateau2Start)
            .add(plateau2End)
        return spline.build()
    }
}