package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskRegistryKeys
import org.teamvoided.dusk_debris.spell.Spell

object DuskSpells {
    val VENGEFUL_SPIRIT = create("vengeful_spirit")

    val DESOLATE_DIVE = create("desolate_dive")

    fun create(path: String): RegistryKey<Spell<*, *>> =
        RegistryKey.of(DuskRegistryKeys.SPELL, DuskDebris.id(path))
}