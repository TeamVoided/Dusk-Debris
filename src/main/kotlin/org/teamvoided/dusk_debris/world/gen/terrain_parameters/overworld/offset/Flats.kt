package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset.elev

object Flats {
    /** FLATS TYPE NOTES
     *
     * Flats, entirely flat with little variation, min is lower terrain than max
     *
     * Flats and Upper, has a small elevated portion halfway from rivers
     *
     * Raised, cliffs border rivers
     *
     * Raised High, Raised except higher cliffs
     *
     **/
    enum class PlatType(val min: Float, val max: Float = min) {
        Flats(-1f, -0.3f),
        FlatsAndUpper(0f, 0.35f),
        Raised(0.4f, 0.7f),
        RaisedHigh(Raised.max + 0.00001f);
    }

    fun <C, I : ToFloatFunction<C>> createFlats(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val flats1 = flats(50, 77, data)
        val flats2 = flats(45, 63, data)
        val flatsAndUpper = flatsAndUpper(contNumber, data)
        val raised = raised(contNumber, 10, data)
        val raisedHigh = raised(contNumber, 20, data)

        val spline = Spline.builder(data.flatsType, data.amplifier)
            //.add(PlatType.Flats.min, flats1)
            .add(PlatType.Flats.max, flats2)
            //.add(PlatType.FlatsAndUpper.min, flatsAndUpper)
            //.add(PlatType.FlatsAndUpper.max, flatsAndUpper)
            //.add(PlatType.Raised.min, raised)
            //.add(PlatType.Raised.max, raised)
            //.add(PlatType.RaisedHigh.min, raisedHigh)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> flats(
        low: Int,
        high: Int,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val river = -1f to elev(low)
        val shore = -0.4f to elev(high)
        val flatEnd = 0f to shore.second
        val end = 1f to shore.second * 1.5f

        val endSlope = calculateSlope(0f to shore.second, end)

        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(river, 0.2f)
            .add(shore)
            .add(flatEnd)
            .add(end, endSlope)
        return flats.build()
    }

    private fun <C, I : ToFloatFunction<C>> flatsAndUpper(
        contNumber: Float,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val extra = (contNumber * 3).toInt()
        val river = -1f to flatsElev(50, 10, extra, data)
        val shore = -0.4f to flatsElev(62, 7, extra, data)
        val mid = 0.2f to flatsElev(68, 6, extra, data)
        val high = 0.4f to flatsElev(71, 10, extra, data)
        val end = 1f to flatsElev(92, 12, extra, data)

        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(river.first, river.second, 0.2f)
            .add(shore.first, shore.second)
            .add(mid.first, mid.second)
            .add(high.first, high.second)
            .add(end.first, end.second)
        return flats.build()
    }

    private fun <C, I : ToFloatFunction<C>> raised(
        contNumber: Float,
        raise: Int,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val extra = (contNumber * 5).toInt()
        val extraCliff = extra * (raise / 10) + raise

        val river = -1f to elev(45 + extra)
        val cliffBase = -0.7501f to elev(61 + extra)
        val cliffTop = -0.75f to flatsElev(70, 7, extraCliff, data)
        val end = 1f to flatsElev(90, 5, extraCliff, data)

        val slope = calculateSlope(cliffTop.first to 0f, end.first to elev(75)) * 2f

        val flats = Spline.builder(data.ridgesFolded, data.amplifier)
            .add(river)
            .add(cliffBase)
            .add(cliffTop.first, cliffTop.second)
            .add(end.first, end.second, slope)
        return flats.build()
    }

    private fun <C, I : ToFloatFunction<C>> flatsElev(
        average: Int,
        range: Int,
        extra: Int,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val low = -1f to -elev(average + range + extra)
        val center = 0f to elev(average + extra)
        val high = 1f to -low.second

        val slope = calculateSlope(center, high) * 1.25f

        val flats = Spline.builder(data.flatsElev, data.amplifier)
            .add(low, slope)
            .add(center)
            .add(high, slope)
        return flats.build()
    }
}