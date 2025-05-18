package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskNoiseParametersKeys {
    val EXAMPLE = create("example")

    val CONTINENTAL_WEIRDNESS = create("parameters/continental_weirdness")

    val GRAND_CANYON = create("plateau/grand_canyon")
    val PLATEAU_TYPE = create("plateau/type")
    val UR_TYPE = create("underground_river/type")
    val UR_HEIGHT = create("underground_river/height")


    //val LAVA_TUBE = create("lava_tube")
    val LAVA_LEVEL = create("nether/lava_level")

    val TEMPERATURE_NETHER = create("nether/parameters/temperature")
    val VEGETATION_NETHER = create("nether/parameters/humidity")
    val CONTINENTALNESS_NETHER = create("nether/parameters/continentalness")
    val EROSION_NETHER = create("nether/parameters/erosion")
    val DROP_CEILING = create("nether/drop_ceiling")
    val RIDGE_NETHER = create("nether/parameters/ridge")

    /*
    val TEMPERATURE_LARGE_NETHER = create("parameters/large_biomes/temperature")
    val VEGETATION_LARGE_NETHER = create("parameters/large_biomes/humidity")
    val CONTINENTALNESS_LARGE_NETHER = create("parameters/large_biomes/continentalness")
    val EROSION_LARGE_NETHER = create("parameters/large_biomes/erosion")
    val DROP_CEILING_LARGE = create("large_biomes/drop_ceiling")
     */


    private  fun create(id: String): RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> =
        RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, id(id))
}
