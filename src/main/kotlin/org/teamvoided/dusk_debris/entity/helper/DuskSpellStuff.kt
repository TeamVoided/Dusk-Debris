package org.teamvoided.dusk_debris.entity.helper

import org.teamvoided.dusk_debris.spell.AbstractSpell

interface DuskSpellStuff {
    fun setSpell(spell: AbstractSpell?)
    fun getSpell(): AbstractSpell?
    fun getSpellTicksLeft(): Int
    fun setSpellTicksLeft(spellTicksLeft: Int)
}