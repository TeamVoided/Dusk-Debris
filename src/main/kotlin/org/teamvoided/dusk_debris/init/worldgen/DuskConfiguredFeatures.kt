package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id

@Suppress("MemberVisibilityCanBePrivate")
object DuskConfiguredFeatures {
    val BLUE_NETHERSHROOM = create("blue_nethershroom")
    val HUGE_BLUE_NETHERSHROOM = create("huge_blue_nethershroom")
    val LARGE_BLUE_NETHERSHROOM_PATCH = create("large_blue_nethershroom_patch")
    val BLUE_NETHERSHROOM_PATCH = create("blue_nethershroom_patch")
    val PURPLE_NETHERSHROOM = create("purple_nethershroom")
    val HUGE_PURPLE_NETHERSHROOM = create("huge_purple_nethershroom")
    val LARGE_PURPLE_NETHERSHROOM_PATCH = create("large_purple_nethershroom_patch")
    val PURPLE_NETHERSHROOM_PATCH = create("purple_nethershroom_patch")


    fun init() {}

    fun create(id: String): RegistryKey<ConfiguredFeature<*, *>> =
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id(id))

}