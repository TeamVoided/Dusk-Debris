package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant

object DuskRegistries {
    fun init() {
        DynamicRegistries.registerSynced(SNIFFER_VARIANT, SnifferVariant.CODEC)
    }

    val SNIFFER_VARIANT: RegistryKey<Registry<SnifferVariant>> = createRegistryKey("sniffer_variant")

    private fun <T> createRegistryKey(id: String): RegistryKey<Registry<T>> {
        return RegistryKey.ofRegistry(id(id))
    }

}