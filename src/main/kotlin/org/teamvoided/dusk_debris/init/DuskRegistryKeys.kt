package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.world.FogModifier

object DuskRegistryKeys {
    @JvmField
    val SNIFFER_VARIANT: RegistryKey<Registry<SnifferVariant>> = createRegistryKey("sniffer_variant")
    val FOG_MODIFIER: RegistryKey<Registry<FogModifier>> = createRegistryKey("fog_modifier")
    val SPELL_TYPE: RegistryKey<Registry<SpellType<*>>> = createRegistryKey("spell_type")
    val SPELL: RegistryKey<Registry<Spell<*, *>>> = createRegistryKey("spell")

    fun init() {
        DynamicRegistries.registerSynced(SNIFFER_VARIANT, SnifferVariant.CODEC)
        DynamicRegistries.registerSynced(FOG_MODIFIER, FogModifier.CODEC)
        DynamicRegistries.registerSynced(SPELL, Spell.CODEC)
    }

    private fun <T> createRegistryKey(id: String): RegistryKey<Registry<T>> = RegistryKey.ofRegistry(id(id))
}