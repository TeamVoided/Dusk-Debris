package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.registry.*
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.data.DuskSpells
import org.teamvoided.dusk_debris.init.DuskSpellTypes
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellSettings
import org.teamvoided.dusk_debris.spell.SpellType

object Spells {

    fun bootstrap(c: BootstrapContext<Spell<*, *>>) {
        c.registerSpirit(DuskSpells.VENGEFUL_SPIRIT, 40, 8)
        c.registerDive(DuskSpells.DESOLATE_DIVE, 10, 68)
    }


    private fun BootstrapContext<Spell<*, *>>.registerSpirit(
        registryKey: RegistryKey<Spell<*, *>>,
        priority: Int,
        cooldown: Int
    ): Holder.Reference<Spell<*, *>> {
        return this.register(
            registryKey,
            DuskSpellTypes.VENGEFUL_SPIRIT,
            GenericSpellSettings(DuskSpells.spellTranslation(registryKey).copy(), priority, cooldown)
        )
    }

    private fun BootstrapContext<Spell<*, *>>.registerDive(
        registryKey: RegistryKey<Spell<*, *>>,
        priority: Int,
        cooldown: Int
    ): Holder.Reference<Spell<*, *>> {
        return this.register(
            registryKey,
            DuskSpellTypes.DESOLATE_DIVE,
            GenericSpellSettings(DuskSpells.spellTranslation(registryKey).copy(), priority, cooldown)
        )
    }

    private fun <SS : SpellSettings, S : SpellType<SS>> BootstrapContext<Spell<*, *>>.register(
        registryKey: RegistryKey<Spell<*, *>>,
        spellType: S,
        spellSettings: SS
    ): Holder.Reference<Spell<*, *>> = this.register(registryKey, Spell(spellType, spellSettings))
}