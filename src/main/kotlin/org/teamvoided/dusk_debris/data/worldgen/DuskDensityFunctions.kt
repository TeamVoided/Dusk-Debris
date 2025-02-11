package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDensityFunctions {
    val EXAMPLE = create("example")

    val NETHER_PILLARS = create("nether_pillars")

    val LAVA_LEVEL = create("lava_level")


    val SLOPED_CHEESE_NETHER = create("sloped_cheese")
    val OFFSET_FLOOR_NETHER = create("offset_floor")
    val OFFSET_CEILING_NETHER = create("offset_ceiling")
    val OFFSET_NETHER = create("offset")
    val JAGGEDNESS_NETHER = create("jaggedness")
    val FACTOR_NETHER = create("factor")
    val JAGGED_PARAMETER_NETHER = create("jagged_parameter")

    val TEMPERATURE_NETHER = create("parameters/temperature")
    val HUMIDITY_NETHER = create("parameters/humidity")
    val CONTINENTALNESS_NETHER = create("parameters/continentalness")
    val EROSION_NETHER = create("parameters/erosion")
    val RIDGES_NETHER = create("parameters/ridges")
    val RIDGES_FOLDED_NETHER = create("parameters/ridges_folded")
    val DEPTH_NETHER = create("parameters/depth")
    val DEPTH_FLOOR_NETHER = create("parameters/depth_floor")
    val DEPTH_CEILING_NETHER = create("parameters/depth_ceiling")
    val DROP_CEILING = create("drop_ceiling")
    val NETHER_FINAL_DENSITY = create("final_density")

    /*
    val SLOPED_CHEESE_NETHER_LARGE_BIOME = create("large_biomes/sloped_cheese")
    val OFFSET_FLOOR_NETHER_LARGE_BIOME = create("large_biomes/offset_floor")
    val OFFSET_CEILING_NETHER_LARGE_BIOME = create("large_biomes/offset_ceiling")
    val JAGGEDNESS_NETHER_LARGE_BIOME = create("large_biomes/jaggedness")
    val FACTOR_NETHER_LARGE_BIOME = create("large_biomes/factor")
    val TEMPERATURE_NETHER_LARGE_BIOME = create("parameters/large_biomes/temperature")
    val HUMIDITY_NETHER_LARGE_BIOME = create("parameters/large_biomes/humidity")
    val CONTINENTALNESS_NETHER_LARGE_BIOME = create("parameters/large_biomes/continentalness")
    val EROSION_NETHER_LARGE_BIOME = create("parameters/large_biomes/erosion")
    val DROP_CEILING_LARGE_BIOME = create("large_biomes/drop_ceiling")
    val DEPTH_NETHER_LARGE_BIOME = create("parameters/large_biomes/depth")


    val SLOPED_CHEESE_NETHER_AMPLIFIED = create("amplified/sloped_cheese")
    val OFFSET_FLOOR_NETHER_AMPLIFIED = create("amplified/offset_floor")
    val OFFSET_CEILING_NETHER_AMPLIFIED = create("amplified/offset_ceiling")
    val JAGGEDNESS_NETHER_AMPLIFIED = create("amplified/jaggedness")
    val FACTOR_NETHER_AMPLIFIED = create("amplified/factor")
    val TEMPERATURE_NETHER_AMPLIFIED = create("parameters/amplified/temperature")
    val HUMIDITY_NETHER_AMPLIFIED = create("parameters/amplified/humidity")
    val CONTINENTALNESS_NETHER_AMPLIFIED = create("parameters/amplified/continentalness")
    val EROSION_NETHER_AMPLIFIED = create("parameters/amplified/erosion")
    val DEPTH_NETHER_AMPLIFIED = create("parameters/amplified/depth")
    */

    fun create(id: String): RegistryKey<DensityFunction> = RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, id(id))
}