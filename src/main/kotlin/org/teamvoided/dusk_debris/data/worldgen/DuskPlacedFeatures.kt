package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.id

@Suppress("MemberVisibilityCanBePrivate")
object DuskPlacedFeatures {

    val OAK_CHECHED = create("tree/oak_checked")


    val CYPRESS = create("swamp/cypress")
    val TALL_CYPRESS = create("swamp/tall_cypress")
    val TREES_SWAMP = create("swamp/trees_swamp")
    val TREES_SWAMP_EXTRA = create("swamp/trees_swamp_extra")


    val HUGE_BLUE_NETHERSHROOM = create("nether/huge_blue_nethershroom")
    val WARPED_BLUE_NETHERSHROOM_PATCH = create("nether/warped_blue_nethershroom_patch")
    val BLUE_NETHERSHROOM_PATCH = create("nether/blue_nethershroom_patch")

    val HUGE_PURPLE_NETHERSHROOM = create("nether/huge_purple_nethershroom")
    val CRIMSON_PURPLE_NETHERSHROOM_PATCH = create("nether/crimson_purple_nethershroom_patch")
    val PURPLE_NETHERSHROOM_PATCH = create("nether/purple_nethershroom_patch")

    val BLACKSTONE_STRIPS = create("nether/blackstone_strips")


    val TORUS = create("torus")
    val OVERWORLD_TORUS = create("overworld_torus")

    val ROCK_SPIRE = create("spire/rocky")
    val LARGE_ROCK_SPIRE = create("spire/large/rocky")
    val GRASS_SPIRE = create("spire/grass")
    val LARGE_GRASS_SPIRE = create("spire/large/grass")

    val BOREAL_VALLEY_VEGETATION = create("boreal_valley/boreal_valley_vegetation")

    private fun create(id: String): ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, id(id))
}