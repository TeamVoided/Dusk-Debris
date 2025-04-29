package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import net.minecraft.registry.RegistryCodecs
import net.minecraft.util.dynamic.RegistryElementCodec
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.init.DuskRegistryKeys

class Spell<SC : SpellSettings, S : SpellType<SC>>(val spellType: S, val config: SC) {

    companion object {
        val CODEC: Codec<Spell<out SpellSettings, out SpellType<out SpellSettings>>> =
            DuskRegistries.SPELL_TYPE.codec.dispatch({ it.spellType }, { it.getCodec() })

        val REGISTRY_CODEC = RegistryElementCodec.of(DuskRegistryKeys.SPELL, CODEC)
        val LIST_CODEC = RegistryCodecs.homogeneousList(DuskRegistryKeys.SPELL, CODEC)
    }
}