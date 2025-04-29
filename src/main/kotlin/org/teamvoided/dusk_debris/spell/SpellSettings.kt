package org.teamvoided.dusk_debris.spell

import java.util.stream.Stream


interface SpellSettings {
    val spellSettings: Stream<Spell<*, *>> get() = Stream.empty()

    companion object {
        object DefaultSpellSettings : SpellSettings

        val DEFAULT: DefaultSpellSettings = DefaultSpellSettings
    }
}
