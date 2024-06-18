package org.teamvoided.dusk_debris.data.gen.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import java.util.concurrent.CompletableFuture

class RegistryProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String {
        return "theAvera"
    }

    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        entries.addAll(registries.getLookupOrThrow(RegistryKeys.SOUND_EVENT))
    }
}