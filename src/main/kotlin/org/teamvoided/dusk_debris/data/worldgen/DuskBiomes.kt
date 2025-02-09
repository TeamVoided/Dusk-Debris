package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBiomes {
    val TEST = create("test")

    val BOREAL_VALLEY = create("boreal_valley")
    val FOG_CANYON = create("fog_canyon")

    val NETHER_TEST = create("nether_test")
    val NETHER_WASTES = create("nether_wastes")
    val CRIMSON_FOREST = create("crimson_forest")
    val CRIMSON_WASTES = create("crimson_wastes")
    val WARPED_FOREST = create("warped_forest")
    val WARPED_WASTES = create("warped_wastes")
    val BASALT_DELTAS = create("basalt_delta")
    val SOUL_SAND_VALLEY = create("soul_sand_valley")

    fun init() {}

    private fun create(id: String): RegistryKey<Biome> = RegistryKey.of(RegistryKeys.BIOME, id(id))
}