package org.teamvoided.dusk_debris.init

import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.VengefulSpiritSpell
import org.teamvoided.dusk_debris.spell.config.SpellConfig
import org.teamvoided.dusk_debris.spell.config.VengefulSpiritConfig

object DuskSpells {

    val VENGEFUL_SPIRIT = register("vengeful_spirit", VengefulSpiritSpell(VengefulSpiritConfig.CODEC))

    fun init() {}

    private fun <C : SpellConfig, S : Spell<C>> register(name: String, spell: S): S =
        Registry.register(DuskRegistries.SPELL, id(name), spell)
}