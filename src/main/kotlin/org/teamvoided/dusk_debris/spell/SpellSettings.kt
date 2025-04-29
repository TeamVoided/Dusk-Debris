package org.teamvoided.dusk_debris.spell

import java.util.stream.Stream


interface SpellSettings {
    val decoratedFeatures: Stream<Spell<*, *>> get() = Stream.empty()

    companion object {
        object DefaultSpellSettings : SpellSettings

        val DEFAULT: DefaultSpellSettings = DefaultSpellSettings
    }
}
