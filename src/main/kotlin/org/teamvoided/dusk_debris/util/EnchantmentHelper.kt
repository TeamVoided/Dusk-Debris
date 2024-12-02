package org.teamvoided.dusk_debris.util

import net.minecraft.enchantment.Enchantment
import org.teamvoided.dusk_debris.data.gen.providers.Enchantments


fun Enchantment.curse(): Enchantment {
    Enchantments.CURSES.add(this)
    return this
}

fun Enchantment.treasure(): Enchantment {
    Enchantments.TREASURE.add(this)
    return this
}

