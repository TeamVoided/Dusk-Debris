package org.teamvoided.dusk_debris.data

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris

object DuskEnchantments {
    val BREAKING = create("curse/breaking")
    val CURSE_OF_THE_FUNNY = create("curse/funny")
    val LEAD_FEET = create("curse/lead_feet")
    val LIGHTNING_ROD = create("curse/lightning_rod")
    val MENDLESS = create("curse/mendless")
    val MIDAS = create("curse/midas")
    val NETHERS_FLAME = create("curse/nethers_flame")
    val SUNBURN = create("curse/sunburn")

    val CURSE_OF_RA = create("particle/curse_of_the_pyramid")
    val CURSE_OF_RA_RED = create("particle/curse_of_the_red_pyramid")
    val PARTICLE_OMINOUS = create("particle/ominous")
    val PARTICLE_REDSTONE = create("particle/redstone")
    val PARTICLE_TRIAL_DETECTION = create("particle/trial_detection")
    val PARTICLE_TRIAL_DETECTION_OMINOUS = create("particle/trial_detection_ominous")

    val INCINERATOR = create("incinerator")
    val SONIC_BURST = create("sonic_burst")

    val IMPALING = create("minecraft/enchantment/impaling")
    val WIND_BURST = create("minecraft/enchantment/wind_burst")

    fun create(id: String): RegistryKey<Enchantment> =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, DuskDebris.id(id))
}