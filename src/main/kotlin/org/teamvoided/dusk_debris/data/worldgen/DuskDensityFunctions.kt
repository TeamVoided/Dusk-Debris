package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.DensityFunction
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDensityFunctions {
    val EXAMPLE = create("example")

    /**PARAMETER TYPES
     *
     * Base means basic noise, usually is just the vanilla files
     *
     * Router means manipulated for biome threshold noise, if no shaper is present use router for biome noise
     *
     * Shaper means manipulated for terrain shape
     * */
    val TEMPERATURE = create("parameters/base/temperature")
    val HUMIDITY = create("parameters/base/humidity")
    val CONTINENT_ROUTER = router("continentalness")
    val EROSION_ROUTER = router("erosion")
    val RIDGES_ROUTER = router("ridges")

    val DEPTH = create("parameters/depth")
    val OFFSET = create("shapers/offset")
    val JAGGEDNESS = create("shapers/jaggedness")
    val FACTOR = create("shapers/factor")
    val EXTRA_OFFSET = create("shapers/offset_extra")
    val SLOPED_CHEESE = create("shapers/sloped_cheese")
    val OVERWORLD_IDWJ = create("initial_density_without_jaggedness")
    val OVERWORLD_FINAL_DENSITY = create("final_density")


    val GRAND_CANYON_RIDGES_FOLDED = create("plateau/grand_canyon_ridges_folded")
    val PLATEAU_TYPE = create("plateau/type")
    val FLATS_TYPE = create("flats/type")
    val FLATS_ELEV = create("flats/elevation")

    /** idea used from [Jacobsjo message](https://discord.com/channels/738975290583285762/770775163942993930/948013415748751432)**/
    val UR_TYPE = create("cave/underground_rivers/type")
    val UR_CONDITION = create("cave/underground_rivers/picker")
    val UR_DENSITY = create("cave/underground_rivers/density")

    val STONE_TOWERS = create("stone_tower/range")
    val STONE_TOWERS_TOWER = create("stone_tower/tower")
    val STONE_TOWERS_MOUND = create("stone_tower/mound")

    val LAKE_CAVE_CONDITION = create("cave/lake/condition")
    val LAKE_CAVE_CAVERN = create("cave/lake/cavern")
    val LAKE_CAVE_AQUIFER = create("cave/lake/aquifer")
    val LAKE_CAVE_DENSITY = create("cave/lake/density")

    val AQU_BARRIER = create("aquifer/barrier")
    val AQU_FLOODEDNESS = create("aquifer/floodedness")
    val AQU_FLOODEDNESS_LB = create("large_biomes/aquifer/floodedness")
    val AQU_FLUID_SPREAD = create("aquifer/fluid_level_spread")
    val AQU_FLUID_SPREAD_LB = create("large_biomes/aquifer/fluid_level_spread")
    val AQU_LAVA = create("aquifer/lava")


    //* - - - * THE NETHER * - - - *//
    val NETHER_PILLARS = nether("nether_pillars")

    val LAVA_LEVEL = nether("lava_level")

    val SLOPED_CHEESE_NETHER = nether("sloped_cheese")
    val OFFSET_FLOOR_NETHER = nether("offset_floor")
    val OFFSET_CEILING_NETHER = nether("offset_ceiling")
    val OFFSET_NETHER = nether("offset")
    val JAGGEDNESS_NETHER = nether("jaggedness")
    val FACTOR_NETHER = nether("factor")
    val JAGGED_PARAMETER_NETHER = nether("jagged_parameter")

    val TEMPERATURE_NETHER = nether("parameters/temperature")
    val HUMIDITY_NETHER = nether("parameters/humidity")
    val CONTINENTALNESS_NETHER = nether("parameters/continentalness")
    val EROSION_NETHER = nether("parameters/erosion")
    val RIDGES_NETHER = nether("parameters/ridges")
    val RIDGES_FOLDED_NETHER = nether("parameters/ridges_folded")
    val DEPTH_NETHER = nether("parameters/depth")
    val DEPTH_FLOOR_NETHER = nether("parameters/depth_floor")
    val DEPTH_CEILING_NETHER = nether("parameters/depth_ceiling")
    val DROP_CEILING = nether("drop_ceiling")
    val NETHER_FINAL_DENSITY = nether("final_density")

    /*
    val SLOPED_CHEESE_NETHER_LARGE_BIOME = nether("large_biomes/sloped_cheese")
    val OFFSET_FLOOR_NETHER_LARGE_BIOME = nether("large_biomes/offset_floor")
    val OFFSET_CEILING_NETHER_LARGE_BIOME = nether("large_biomes/offset_ceiling")
    val JAGGEDNESS_NETHER_LARGE_BIOME = nether("large_biomes/jaggedness")
    val FACTOR_NETHER_LARGE_BIOME = nether("large_biomes/factor")
    val TEMPERATURE_NETHER_LARGE_BIOME = nether("parameters/large_biomes/temperature")
    val HUMIDITY_NETHER_LARGE_BIOME = nether("parameters/large_biomes/humidity")
    val CONTINENTALNESS_NETHER_LARGE_BIOME = nether("parameters/large_biomes/continentalness")
    val EROSION_NETHER_LARGE_BIOME = nether("parameters/large_biomes/erosion")
    val DROP_CEILING_LARGE_BIOME = nether("large_biomes/drop_ceiling")
    val DEPTH_NETHER_LARGE_BIOME = nether("parameters/large_biomes/depth")


    val SLOPED_CHEESE_NETHER_AMPLIFIED = nether("amplified/sloped_cheese")
    val OFFSET_FLOOR_NETHER_AMPLIFIED = nether("amplified/offset_floor")
    val OFFSET_CEILING_NETHER_AMPLIFIED = nether("amplified/offset_ceiling")
    val JAGGEDNESS_NETHER_AMPLIFIED = nether("amplified/jaggedness")
    val FACTOR_NETHER_AMPLIFIED = nether("amplified/factor")
    val TEMPERATURE_NETHER_AMPLIFIED = nether("parameters/amplified/temperature")
    val HUMIDITY_NETHER_AMPLIFIED = nether("parameters/amplified/humidity")
    val CONTINENTALNESS_NETHER_AMPLIFIED = nether("parameters/amplified/continentalness")
    val EROSION_NETHER_AMPLIFIED = nether("parameters/amplified/erosion")
    val DEPTH_NETHER_AMPLIFIED = nether("parameters/amplified/depth")
    */

    private fun nether(id: String): ResourceKey<DensityFunction> =
        ResourceKey.create(Registries.DENSITY_FUNCTION, id("nether/$id"))

    private fun router(id: String): ResourceKey<DensityFunction> =
        ResourceKey.create(Registries.DENSITY_FUNCTION, id("parameters/router/$id"))

    private fun shaper(id: String): ResourceKey<DensityFunction> =
        ResourceKey.create(Registries.DENSITY_FUNCTION, id("parameters/shaper/$id"))

    private fun create(id: String): ResourceKey<DensityFunction> = ResourceKey.create(Registries.DENSITY_FUNCTION, id(id))
}