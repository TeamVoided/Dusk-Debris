package org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.offset

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.util.world_helper.calculateSlope
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.overworld.Offset

object Mountains {
    fun <C, I : ToFloatFunction<C>> mountain(
        riverbed: Int,
        peak: Int,
        valley: Boolean,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val mountainRiverbed = -1f to Offset.elev(riverbed)
        val mountainPeak = 1f to Offset.elev(peak)
        val mountainSlope = calculateSlope(mountainRiverbed, mountainPeak)

        val mountain = Spline.builder(data.ridgesFolded, data.amplifier)
        if (valley) {
            mountain.add(mountainRiverbed)
            mountain.add(0f, (mountainRiverbed.second + mountainPeak.second) / 2f, mountainSlope)
        } else {
            mountain.add(mountainRiverbed, mountainSlope)
        }

        mountain.add(mountainPeak, mountainSlope)
        return mountain.build()
    }
    fun <C, I : ToFloatFunction<C>> swampMountain(
        riverbed: Int,
        peak: Int,
        valley: Boolean,
        data: OverworldTerrainCreator.TerrainParametersData<C, I>
    ): Spline<C, I> {
        val mountainRiverbed = -1f to Offset.elev(riverbed)
        val mountainPeak = 1f to Offset.elev(peak)
        val mountainSlope = calculateSlope(mountainRiverbed, mountainPeak)

        val mountain = Spline.builder(data.ridgesFolded, data.amplifier)
        if (valley) {
            mountain.add(mountainRiverbed)
            mountain.add(0f, (mountainRiverbed.second + mountainPeak.second) / 2f, mountainSlope)
        } else {
            mountain.add(mountainRiverbed, mountainSlope)
        }

        mountain.add(mountainPeak, mountainSlope)
        return mountain.build()
    }

}