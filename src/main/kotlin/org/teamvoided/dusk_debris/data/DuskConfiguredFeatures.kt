package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id

@Suppress("MemberVisibilityCanBePrivate")
object DuskConfiguredFeatures {

    val DISK_MUD = create("disk_mud")
    val SWAMP_CYPRESS = create("swamp/swamp_cyprus")
    val TALL_SWAMP_CYPRESS = create("swamp/tall_swamp_cypress")
    val TREES_SWAMP = create("swamp/trees_swamp")

    val BLUE_NETHERSHROOM = create("nether/blue_nethershroom")
    val HUGE_BLUE_NETHERSHROOM = create("nether/huge_blue_nethershroom")
    val LARGE_BLUE_NETHERSHROOM_PATCH = create("nether/large_blue_nethershroom_patch")
    val BLUE_NETHERSHROOM_PATCH = create("nether/blue_nethershroom_patch")
    val PURPLE_NETHERSHROOM = create("nether/purple_nethershroom")
    val HUGE_PURPLE_NETHERSHROOM = create("nether/huge_purple_nethershroom")
    val LARGE_PURPLE_NETHERSHROOM_PATCH = create("nether/large_purple_nethershroom_patch")
    val PURPLE_NETHERSHROOM_PATCH = create("nether/purple_nethershroom_patch")

    val GLASS_SPIKE = create("glass_spike")
    val COBBLESTONE_TORUS = create("cobblestone_torus")
    val STONE_TORUS = create("stone_torus")
    val OVERWORLD_TORUS = create("overworld_torus")
    val TORUS = create("torus")

    val BOREAL_VALLEY_VEGETATION = create("boreal_valley/boreal_valley_vegetation")
    val SEQUOIA_TREE = create("boreal_valley/sequoia_tree")

    fun create(id: String): RegistryKey<ConfiguredFeature<*, *>> =
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id(id))

}