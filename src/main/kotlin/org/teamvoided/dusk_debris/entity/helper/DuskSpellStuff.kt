package org.teamvoided.dusk_debris.entity.helper

import org.teamvoided.dusk_debris.spell.Spell

interface DuskSpellStuff {
    fun setSpell(spell: Spell?)
    fun getSpell(): Spell?
    fun getSpellTicksLeft(): Int
    fun setSpellTicksLeft(spellTicksLeft: Int)
}