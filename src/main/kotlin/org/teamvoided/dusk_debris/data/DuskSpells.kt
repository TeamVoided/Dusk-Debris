package org.teamvoided.dusk_debris.data

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskRegistryKeys
import org.teamvoided.dusk_debris.spell.Spell

object DuskSpells {
    val SPELLS = mutableSetOf<ResourceKey<Spell<*, *>>>()
    val TRANSLATIONS = mutableSetOf<MutableComponent>()


    val VENGEFUL_SPIRIT = create("vengeful_spirit")

    val DESOLATE_DIVE = create("desolate_dive")

    fun spellTranslation(registryKey: ResourceKey<Spell<*, *>>): MutableComponent {
        val path: String = registryKey.location().path
        return Component.translatable("spell." + DuskDebris.MODID + ".$path")
    }

    private fun create(path: String): ResourceKey<Spell<*, *>> {
        val spell = ResourceKey.create(DuskRegistryKeys.SPELL, DuskDebris.id(path))
        SPELLS.add(spell)
        TRANSLATIONS.add(spellTranslation(spell))
        return spell
    }
}