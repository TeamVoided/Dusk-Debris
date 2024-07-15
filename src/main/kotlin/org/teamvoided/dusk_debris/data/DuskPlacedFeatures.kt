package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id

@Suppress("MemberVisibilityCanBePrivate")
object DuskPlacedFeatures {

    val CYPRESS= create("cypress")
    val TALL_CYPRESS = create("tall_cypress")
    val TREES_SWAMP = create("trees_swamp")
    val TREES_SWAMP_EXTRA = create("trees_swamp_extra")


    val HUGE_BLUE_NETHERSHROOM = create("huge_blue_nethershroom")
    val WARPED_BLUE_NETHERSHROOM_PATCH = create("warped_blue_nethershroom_patch")
    val BLUE_NETHERSHROOM_PATCH = create("blue_nethershroom_patch")

    val HUGE_PURPLE_NETHERSHROOM = create("huge_purple_nethershroom")
    val CRIMSON_PURPLE_NETHERSHROOM_PATCH = create("crimson_purple_nethershroom_patch")
    val PURPLE_NETHERSHROOM_PATCH = create("purple_nethershroom_patch")

    fun create(id: String): RegistryKey<PlacedFeature> =
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, id(id))
}