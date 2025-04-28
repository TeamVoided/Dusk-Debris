package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant
import org.teamvoided.dusk_debris.spell.DesolateDiveSpell
import org.teamvoided.dusk_debris.spell.VengefulSpiritSpell
import org.teamvoided.dusk_debris.world.FogModifier

object DuskRegistries {
    fun init() {
        DynamicRegistries.registerSynced(SNIFFER_VARIANT, SnifferVariant.CODEC)
        DynamicRegistries.registerSynced(FOG_MODIFIER, FogModifier.CODEC)
        DynamicRegistries.registerSynced(VENGEFUL_SPIRIT, VengefulSpiritSpell.CODEC)
        DynamicRegistries.registerSynced(DESOLATE_DIVE, DesolateDiveSpell.CODEC)
    }


    @JvmField
    val SNIFFER_VARIANT: RegistryKey<Registry<SnifferVariant>> = createRegistryKey("sniffer_variant")
    val FOG_MODIFIER: RegistryKey<Registry<FogModifier>> = createRegistryKey("fog_modifier")
    val VENGEFUL_SPIRIT: RegistryKey<Registry<VengefulSpiritSpell>> = createRegistryKey("spell/vengeful_spirit")
    val DESOLATE_DIVE: RegistryKey<Registry<DesolateDiveSpell>> = createRegistryKey("spell/desolate_dive")

    private fun <T> createRegistryKey(id: String): RegistryKey<Registry<T>> = RegistryKey.ofRegistry(id(id))
}