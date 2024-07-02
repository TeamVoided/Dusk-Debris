package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id

@Suppress("MemberVisibilityCanBePrivate")
object DuskConfiguredFeatures {
    val HUGE_BLUE_NETHERSHROOM = create("huge_blue_nethershroom")
    val HUGE_PURPLE_NETHERSHROOM = create("huge_purple_nethershroom")


    fun init() {}

    fun create(id: String): RegistryKey<ConfiguredFeature<*, *>> =
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id(id))

}