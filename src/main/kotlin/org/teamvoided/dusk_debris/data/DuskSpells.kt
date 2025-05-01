package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskRegistryKeys
import org.teamvoided.dusk_debris.spell.Spell

object DuskSpells {
    val SPELLS = mutableSetOf<RegistryKey<Spell<*, *>>>()
    val TRANSLATIONS = mutableSetOf<MutableText>()


    val VENGEFUL_SPIRIT = create("vengeful_spirit")

    val DESOLATE_DIVE = create("desolate_dive")

    fun spellTranslation(registryKey: RegistryKey<Spell<*, *>>): MutableText {
        val path: String = registryKey.value.path
        return Text.translatable("spell." + DuskDebris.MODID + ".$path")
    }

    private fun create(path: String): RegistryKey<Spell<*, *>> {
        val spell = RegistryKey.of(DuskRegistryKeys.SPELL, DuskDebris.id(path))
        SPELLS.add(spell)
        TRANSLATIONS.add(spellTranslation(spell))
        return spell
    }
}