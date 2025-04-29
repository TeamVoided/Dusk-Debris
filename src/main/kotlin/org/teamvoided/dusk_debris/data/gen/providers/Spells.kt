package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.registry.*
import org.teamvoided.dusk_debris.data.DuskSpells
import org.teamvoided.dusk_debris.init.DuskSpellTypes
import org.teamvoided.dusk_debris.spell.GenericSpellSettings
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellSettings
import org.teamvoided.dusk_debris.spell.SpellType

object Spells {

    fun bootstrap(c: BootstrapContext<Spell<*, *>>) {
        c.register(DuskSpells.VENGEFUL_SPIRIT, DuskSpellTypes.VENGEFUL_SPIRIT, GenericSpellSettings(5, 5))
    }


    private fun <SS : SpellSettings, S : SpellType<SS>> BootstrapContext<Spell<*, *>>.register(
        registryKey: RegistryKey<Spell<*, *>>,
        spellType: S,
        spellSettings: SS
    ): Any = this.register(registryKey, Spell(spellType, spellSettings))
}