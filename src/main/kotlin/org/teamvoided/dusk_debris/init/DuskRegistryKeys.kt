package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.world.FogModifier

object DuskRegistryKeys {
    @JvmField
    val SNIFFER_VARIANT: ResourceKey<Registry<SnifferVariant>> = createRegistryKey("sniffer_variant")
    val FOG_MODIFIER: ResourceKey<Registry<FogModifier>> = createRegistryKey("fog_modifier")
    val SPELL_TYPE: ResourceKey<Registry<SpellType<*>>> = createRegistryKey("spell_type")
    val SPELL: ResourceKey<Registry<Spell<*, *>>> = createRegistryKey("spell")

    fun init() {
        DynamicRegistries.registerSynced(SNIFFER_VARIANT, SnifferVariant.CODEC)
        DynamicRegistries.registerSynced(FOG_MODIFIER, FogModifier.CODEC)
        DynamicRegistries.registerSynced(SPELL, Spell.CODEC)
    }

    private fun <T> createRegistryKey(id: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(id))
}