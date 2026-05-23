package org.teamvoided.dusk_debris.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import org.teamvoided.dusk_debris.init.DuskRegistryKeys
import java.util.concurrent.CompletableFuture

class DynamicRegistryProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricDynamicRegistryProvider(o, r) {

    override fun getName(): String = "dusk-debris"

    override fun configure(reg: HolderLookup.Provider, e: Entries) {
        e.addAll(reg.lookupOrThrow(Registries.BIOME))
        e.addAll(reg.lookupOrThrow(Registries.PLACED_FEATURE))
        e.addAll(reg.lookupOrThrow(Registries.CONFIGURED_FEATURE))
        e.addAll(reg.lookupOrThrow(Registries.CONFIGURED_CARVER))

        e.addAll(reg.lookupOrThrow(Registries.NOISE))
        e.addAll(reg.lookupOrThrow(Registries.DENSITY_FUNCTION))
        e.addAll(reg.lookupOrThrow(Registries.NOISE_SETTINGS))
        e.addAll(reg.lookupOrThrow(Registries.LEVEL_STEM))

        e.addAll(reg.lookupOrThrow(Registries.TEMPLATE_POOL))
        e.addAll(reg.lookupOrThrow(Registries.STRUCTURE))
        e.addAll(reg.lookupOrThrow(Registries.STRUCTURE_SET))

        e.addAll(reg.lookupOrThrow(Registries.DAMAGE_TYPE))
        e.addAll(reg.lookupOrThrow(Registries.ENCHANTMENT))

        e.addAll(reg.lookupOrThrow(Registries.PAINTING_VARIANT))
        e.addAll(reg.lookupOrThrow(DuskRegistryKeys.SNIFFER_VARIANT))
        e.addAll(reg.lookupOrThrow(DuskRegistryKeys.FOG_MODIFIER))
        e.addAll(reg.lookupOrThrow(DuskRegistryKeys.SPELL))
    }
}
