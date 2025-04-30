package org.teamvoided.dusk_debris.init

import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.spell.type.DesolateDiveSpell
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings
import org.teamvoided.dusk_debris.spell.SpellSettings
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.spell.type.VengefulSpiritSpell

object DuskSpellTypes {
    val VENGEFUL_SPIRIT = register("vengeful_spirit", VengefulSpiritSpell(GenericSpellSettings.CODEC))
    val DESOLATE_DIVE = register("desolate_dive", DesolateDiveSpell(GenericSpellSettings.CODEC))

    fun init() {}

    private fun <SS : SpellSettings, S : SpellType<SS>> register(name: String, spell: S): S =
        Registry.register(DuskRegistries.SPELL_TYPE, id(name), spell)
}