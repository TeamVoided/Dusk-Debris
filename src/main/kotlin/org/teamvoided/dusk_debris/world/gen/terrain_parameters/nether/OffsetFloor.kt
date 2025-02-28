package org.teamvoided.dusk_debris.world.gen.terrain_parameters.nether

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.NetherTerrainParametersCreator.nFloor

object OffsetFloor {

    fun <C, I : ToFloatFunction<C>> createWarpedIsland(
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val warpedIsland = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, nFloor(24), 0.2f)
            .add(-0.5f, nFloor(38), 0.4f)
            .add(0.25f, nFloor(64), 0.4f)
            .add(0.8f, nFloor(128), 0.4f)
        return warpedIsland.build()
    }

    fun <C, I : ToFloatFunction<C>> createMountain(
        valley: Int,
        peak: Int,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val valley1 = nFloor(valley)
        val peak1 = nFloor(peak)

        val slope = calculateSlope(peak1, valley1, 1f, -1f)

        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(-1f, valley1, slope)
            .add(1f, peak1, slope)
        return spline.build()
    }

    fun <C, I : ToFloatFunction<C>> createPlateau(
        mult: Float,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val point1 = -1f to nFloor(16)
        val point2 = -.5f to nFloor(100) * mult
//        val point3 = 0f to nFloor(100)*mult
        val point4 = .5f to nFloor(100) * mult
        val point5 = 1f to nFloor(128) * mult

        val slopeValley = calculateSlope(point1, point4)
        val slopePeak = calculateSlope(point2, point5)

        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(point1, slopeValley)
            .add(point2)
//            .add(point3)
            .add(point4)
            .add(point5, slopePeak)
        return spline.build()
    }

    fun <C, I : ToFloatFunction<C>> createWall(ridgesFolded: I, amplifier: ToFloatFunction<Float>): Spline<C, I> {
        val point1 = -1f to nFloor(16)
        val point2 = -0.75f to nFloor(128)
        val slope = calculateSlope(point1, point2)
        val spline = Spline.builder(ridgesFolded, amplifier)
            .add(point1, slope)
            .add(point2, slope * 2)
        return spline.build()
    }

    fun <C, I : ToFloatFunction<C>> createShelfs(
        mult: Float,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val riverbed = -1f to nFloor(24)
        val shelf1Min = -0.8f to nFloor(42)
        val shelf1Max = -0.25f to nFloor(48) * mult
        val shelf2Min = -0.2f to nFloor(78) * mult
        val shelf2Max = 0.45f to nFloor(90) * mult
        val shelf3Min = 0.5f to nFloor(128) * mult
        val shelf3Max = 1f to nFloor(144) * mult

        val riverbedDer = calculateSlope(riverbed, shelf1Min)
        val shelf1MinDer = calculateSlope(riverbed, shelf1Max)
        val shelf1MaxDer = calculateSlope(shelf1Min, shelf1Max)
        val shelf2MinDer = calculateSlope(shelf1Max, shelf2Max)
        val shelf2MaxDer = calculateSlope(shelf2Min, shelf2Max)
        val shelf3MinDer = calculateSlope(shelf2Max, shelf3Max)
        val shelf3MaxDer = calculateSlope(shelf3Min, shelf3Max)


        val shelf = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, riverbedDer)
            .add(shelf1Min, shelf1MinDer)
            .add(shelf1Max, shelf1MaxDer)
            .add(shelf2Min, shelf2MinDer)
            .add(shelf2Max, shelf2MaxDer)
            .add(shelf3Min, shelf3MinDer)
            .add(shelf3Max, shelf3MaxDer)
        return shelf.build()
    }

    fun <C, I : ToFloatFunction<C>> createFlats(
        mult: Float,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val riverbed = -1f to nFloor(24)
        val riverbank = -0.8f to nFloor(32)
        val elev1 = 0f to nFloor(48) * mult
        val elev2 = 0.5f to nFloor(64) * mult
        val elev3 = 1f to nFloor(80) * mult

        val riverbedDer = calculateSlope(riverbed, riverbank)
        val riverbankDer = calculateSlope(riverbed, elev1)
        val elev1Der = calculateSlope(riverbank, elev1)
        val elev2Der = calculateSlope(elev1, elev2)
        val elev3Der = calculateSlope(elev2, elev3)

        val flats = Spline.builder(ridgesFolded, amplifier)
            .add(riverbed, riverbedDer)
            .add(riverbank, riverbankDer)
            .add(elev1, elev1Der)
            .add(elev2, elev2Der)
            .add(elev3, elev3Der)
        return flats.build()
    }

    fun <C, I : ToFloatFunction<C>> createAlternate(
        positive: Spline<C, I>,
        negative: Spline<C, I>,
        ridges: I
    ): Spline<C, I> {
        val spline = Spline.builder(ridges)
            .add(-0.05f, negative)
            .add(0.05f, positive)
        return spline.build()
    }


    fun calculateSlope(value1: Float, value2: Float, point1: Float, point2: Float): Float {
        return (value2 - value1) / (point2 - point1)
    }

    fun calculateSlope(pair1: Pair<Float, Float>, pair2: Pair<Float, Float>): Float {
        return (pair2.second - pair1.second) / (pair2.first - pair1.first)
    }
}