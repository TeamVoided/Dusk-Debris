package org.teamvoided.dusk_debris.spell.config

import org.teamvoided.dusk_debris.spell.Spell
import java.util.stream.Stream

interface SpellConfig {
    val DEFAULT: DefaultSpellConfig
        get() = DefaultSpellConfig.INSTANCE

    fun getSpellJargon(): Stream<Spell<*>> {
        return Stream.empty()
    }
}