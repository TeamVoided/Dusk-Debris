package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskNoiseParametersKeys {
    val EXAMPLE = create("example")

    //    val LAVA_TUBE = create("lava_tube")
    val LAVA_LEVEL = create("lava_level")

    val TEMPERATURE_NETHER = create("parameters/temperature")
    val VEGETATION_NETHER = create("parameters/humidity")
    val CONTINENTALNESS_NETHER = create("parameters/continentalness")
    val EROSION_NETHER = create("parameters/erosion")
    val DROP_CEILING = create("drop_ceiling")
    val RIDGE_NETHER = create("parameters/ridge")

    /*
    val TEMPERATURE_LARGE_NETHER = create("parameters/large_biomes/temperature")
    val VEGETATION_LARGE_NETHER = create("parameters/large_biomes/humidity")
    val CONTINENTALNESS_LARGE_NETHER = create("parameters/large_biomes/continentalness")
    val EROSION_LARGE_NETHER = create("parameters/large_biomes/erosion")
    val DROP_CEILING_LARGE = create("large_biomes/drop_ceiling")
     */

    fun create(id: String): RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> =
        RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, id(id))
}
