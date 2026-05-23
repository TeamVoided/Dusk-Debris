package org.teamvoided.dusk_debris.world.gen.terrain_parameters

import net.minecraft.util.CubicSpline
import net.minecraft.util.ToFloatFunction
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.nether.OffsetFloor

object NetherTerrainParametersCreator {
    private var NO_TRANSFORM: ToFloatFunction<Float> = ToFloatFunction.IDENTITY

    //continentalness values
    private const val WARPED_ISLAND = -1.1f
    private const val LAVA_OCEAN_DEEP = -1.02f
    private const val LAVA_OCEAN = -0.25f
    private const val SHORELINE = -0.15f
    private const val OUTLAND = -0.1f
    private const val INLAND = 0.25f
    private const val INLAND_EXTREME = 1f
    private const val HIGHEST_LEVEL = 256
    private const val LOWEST_LEVEL = 0
    private const val ROOF_OFFSET = HIGHEST_LEVEL - 32
    private const val SEA_OFFSET = LOWEST_LEVEL + 32

    //if above sea level, terrain height gets multiplied by 2, otherwise, return the same
    private var OFFSET_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { if (it < 0f) it else it * 2f }

    //does a funky if continentalness is greater than -0.1
    private var FACTOR_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { 1.25f - (6.25f / (it + 5.0f)) }

    //terrain jaggedness gets multiplied by 2, but in most cases, remains 0
    private var JAGGEDNESS_AMPLIFIED: ToFloatFunction<Float> =
        ToFloatFunction.createUnlimited { it * 2.0f }

    fun <C, I : ToFloatFunction<C>> offsetFloorSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        ridges: I,
        amplified: Boolean = false
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM

        val warpedIsland = OffsetFloor.createWarpedIsland(ridgesFolded, amplifiedTransformer)
        val shoreline = offsetFloor(0.7f, erosion, ridges, ridgesFolded, amplifiedTransformer)
        val outlandSpline = offsetFloor(0.8f, erosion, ridges, ridgesFolded, amplifiedTransformer)
        val midlandSpline = offsetFloor(1f, erosion, ridges, ridgesFolded, amplifiedTransformer)
        val inlandSpline = offsetFloor(1.2f, erosion, ridges, ridgesFolded, amplifiedTransformer)


        //OFFSET CONTINENTALNESS
        return CubicSpline.builder(continents, amplifiedTransformer)
//            .add(WARPED_ISLAND, nFloor(180))
//            .add(LAVA_OCEAN_DEEP, nFloor(-18))
//            .add(LAVA_OCEAN, nFloor(24))
//            .add(SHORELINE, shoreline)
//            .add(OUTLAND, outlandSpline)
            .add(INLAND, midlandSpline)
//            .add(INLAND_EXTREME, inlandSpline)
            .build()
    }

    fun <C, I : ToFloatFunction<C>> offsetCeilingSpline(
        continents: I,
        erosion: I,
        ridgesFolded: I,
        dropCeiling: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) OFFSET_AMPLIFIED else NO_TRANSFORM
        return CubicSpline.builder(dropCeiling, amplifiedTransformer)
            .add(-1f, nCeil(224))
//            .add(0f, nCeil(180))
//            .add(1f, nCeil(0))
            .build()
    }

    //method_42054
    fun <C, I : ToFloatFunction<C>> factorSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) FACTOR_AMPLIFIED else NO_TRANSFORM
        val jaggedErosion = CubicSpline.builder(erosion, amplifiedTransformer)
            .add(0f, 3f)
        val jaggedRidgesFolded = CubicSpline.builder(ridgesFolded, amplifiedTransformer)
            .add(-0.8f, 6f)
            .add(-0.7f, jaggedErosion.build())
        return jaggedRidgesFolded.build()
    }

    fun <C, I : ToFloatFunction<C>> jaggednessSpline(
        continents: I,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        jaggedness: I,
        amplified: Boolean
    ): CubicSpline<C, I> {
        val amplifiedTransformer = if (amplified) JAGGEDNESS_AMPLIFIED else NO_TRANSFORM
        val spline = CubicSpline.builder(jaggedness, amplifiedTransformer)
            .add(-1f, 0f)//-0.07f)
//            .add(0f, 0f)
        return spline.build()
    }

    private fun <C, I : ToFloatFunction<C>> offsetFloor(
        mult: Float,
        erosion: I,
        ridges: I,
        ridgesFolded: I,
        amplifier: ToFloatFunction<Float>
    ): CubicSpline<C, I> {
        val tallMountain = OffsetFloor.createMountain(0, 300, mult, ridgesFolded, amplifier)
        val mountain = OffsetFloor.createMountain(16, 200, mult, ridgesFolded, amplifier)
        val plateauTall = OffsetFloor.createPlateau(0, 144, mult, ridgesFolded, amplifier)
        val plateau = OffsetFloor.createPlateau(16, 128, mult, ridgesFolded, amplifier)
        val shelf = OffsetFloor.createShelfs(mult, ridgesFolded, amplifier)
        val flatsWithPoint = OffsetFloor.createFlatsWithPoint(mult, ridgesFolded, amplifier)
        val flats = OffsetFloor.createFlats(mult, ridgesFolded, amplifier)
        val spline = CubicSpline.builder(erosion, amplifier)
            .add(-1f, tallMountain)
            .add(-.75f, mountain)
            .add(-.4f, plateauTall)
            .add(-.1f, plateau)
            .add(0f, shelf)
            .add(.1f, shelf)
            .add(.75f, flats)
        return spline.build()
    }

    fun nFloor(inputY: Int): Float {
        val output = ((inputY - SEA_OFFSET).toFloat() / HIGHEST_LEVEL)
//        val output = ((inputY - SEA_OFFSET - 1f) / HIGHEST_LEVEL)
        return output//(round(10000f * output)) / 10000f
    }

    fun nCeil(inputY: Int): Float {
        val output = ((inputY - ROOF_OFFSET).toFloat() / HIGHEST_LEVEL)
        return output
    }
}