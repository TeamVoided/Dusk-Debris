package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskNoiseParametersKeys {
    val EXAMPLE = create("example")

    val CONTINENTAL_WEIRDNESS = create("parameters/continental_weirdness")

    val PLATEAU_TYPE = create("plateau/type")
    val GRAND_CANYON = create("plateau/grand_canyon")

    val FLATS_TYPE = create("flats/type")
    val FLATS_ELEV = create("flats/elev")

    val UR_TYPE = create("underground_river/type")
    val UR_HEIGHT = create("underground_river/height")

    val STONE_TOWERS = create("stone_towers/towers")
    val STONE_TOWERS_HEIGHT = create("stone_towers/height")
    val STONE_TOWERS_OFFSET = create("stone_towers/offset")


    //* - - - * THE NETHER * - - - *//
    //val LAVA_TUBE = create("lava_tube")
    val LAVA_LEVEL = nether("lava_level")

    val TEMPERATURE_NETHER = nether("parameters/temperature")
    val VEGETATION_NETHER = nether("parameters/humidity")
    val CONTINENTALNESS_NETHER = nether("parameters/continentalness")
    val EROSION_NETHER = nether("parameters/erosion")
    val DROP_CEILING = nether("drop_ceiling")
    val RIDGE_NETHER = nether("parameters/ridge")

    /*
    val TEMPERATURE_LARGE_NETHER = nether("parameters/large_biomes/temperature")
    val VEGETATION_LARGE_NETHER = nether("parameters/large_biomes/humidity")
    val CONTINENTALNESS_LARGE_NETHER = nether("parameters/large_biomes/continentalness")
    val EROSION_LARGE_NETHER = nether("parameters/large_biomes/erosion")
    val DROP_CEILING_LARGE = nether("large_biomes/drop_ceiling")
     */


    private  fun create(id: String): RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> =
        RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, id(id))
    private  fun nether(id: String): RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> =
        RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, id("nether/$id"))
}
