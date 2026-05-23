package org.teamvoided.dusk_debris.data

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.util.curse
import org.teamvoided.dusk_debris.util.particle
import org.teamvoided.dusk_debris.util.treasure

object DuskEnchantments {
    val ENCHANTMENTS = mutableSetOf<ResourceKey<Enchantment>>()
    val CURSES = mutableSetOf<ResourceKey<Enchantment>>()
    val TREASURE = mutableSetOf<ResourceKey<Enchantment>>()
    val ENCHANTMENT_PARTICLE = mutableSetOf<ResourceKey<Enchantment>>()

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

    private fun create(id: String): ResourceKey<Enchantment> {
        val enchantment = ResourceKey.create(Registries.ENCHANTMENT, DuskDebris.id(id))
        ENCHANTMENTS.add(enchantment)
        return enchantment
    }
}