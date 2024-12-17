package org.teamvoided.dusk_debris.data

import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.util.*

object DuskEnchantments {
    val ENCHANTMENTS = mutableSetOf<RegistryKey<Enchantment>>()
    val CURSES = mutableSetOf<RegistryKey<Enchantment>>()
    val TREASURE = mutableSetOf<RegistryKey<Enchantment>>()
    val ENCHANTMENT_PARTICLE = mutableSetOf<RegistryKey<Enchantment>>()

    val BREAKING = create("curse/breaking").curse()
    val CURSE_OF_THE_FUNNY = create("curse/funny").curse()
    val LIGHTNING_ROD = create("curse/lightning_rod").curse()
    val MENDLESS = create("curse/mendless").curse()
    val MIDAS = create("curse/midas").curse()
//    val NETHERS_FLAME = create("curse/nethers_flame").curse()
//    val SUNBURN = create("curse/sunburn").curse()

    val CURSE_OF_RA = create("particle/curse_of_the_pyramid").particle().treasure().curse()
    val CURSE_OF_RA_RED = create("particle/curse_of_the_red_pyramid").particle().treasure().curse()
    val PARTICLE_TRIAL = create("particle/trial").particle().treasure()
    val PARTICLE_TRIAL_OMINOUS = create("particle/trial_ominous").particle().treasure()
    val PARTICLE_REDSTONE = create("particle/redstone").particle().treasure()
    val PARTICLE_TRIAL_DETECTION = create("particle/trial_detection").particle().treasure()
    val PARTICLE_TRIAL_DETECTION_OMINOUS = create("particle/trial_detection_ominous").particle().treasure()

//    val INCINERATOR = create("incinerator")
    val SONIC_BURST = create("sonic_burst").treasure()

    val IMPALING = create("minecraft/enchantment/impaling")
//    val WIND_BURST = create("minecraft/enchantment/wind_burst")

    fun create(id: String): RegistryKey<Enchantment> {
        val enchantment = RegistryKey.of(RegistryKeys.ENCHANTMENT, DuskDebris.id(id))
        ENCHANTMENTS.add(enchantment)
        return enchantment
    }
}