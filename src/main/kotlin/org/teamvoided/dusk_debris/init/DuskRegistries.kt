package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant
import org.teamvoided.dusk_debris.world.FogModifier

object DuskRegistries {
    fun init() {
        DynamicRegistries.registerSynced(SNIFFER_VARIANT, SnifferVariant.CODEC)
        DynamicRegistries.registerSynced(FOG_MODIFIER, FogModifier.CODEC)
    }

    @JvmField
    val SNIFFER_VARIANT: RegistryKey<Registry<SnifferVariant>> = createRegistryKey("sniffer_variant")
    val FOG_MODIFIER: RegistryKey<Registry<FogModifier>> = createRegistryKey("fog_modifier")

    private fun <T> createRegistryKey(id: String): RegistryKey<Registry<T>> = RegistryKey.ofRegistry(id(id))
}