package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.data.DuskFogModifiers
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.init.DuskRegistryKeys.FOG_MODIFIER
import org.teamvoided.dusk_debris.world.FogModifier
import java.util.*

object FogModifiers {

    fun bootstrap(c: BootstrapContext<FogModifier>) {
        c.register(DuskFogModifiers.DEFAULT, DuskBiomeTags.FOG_EMPTY)
        c.register(DuskFogModifiers.HUMID, DuskBiomeTags.FOG_HUMID, 0.5, 1.0)
        c.register(DuskFogModifiers.CREEPY, DuskBiomeTags.FOG_CREEPY, 0.2, 1.0)
        c.register(
            DuskFogModifiers.BOREAL_VALLEY,
            DuskBiomeTags.FOG_BOREAL_VALLEY,
            0.2,
            0.8,
            DuskDebris.id("boreal_valley")
        )
    }

    private fun BootstrapContext<FogModifier>.register(
        registryKey: ResourceKey<FogModifier>,
        biomes: TagKey<Biome>,
        modifier: ResourceLocation? = null
    ): Holder.Reference<FogModifier> {
        return this.register(
            registryKey,
            biomes,
            1.0,
            1.0,
            modifier
        )
    }

    private fun BootstrapContext<FogModifier>.register(
        registryKey: ResourceKey<FogModifier>,
        biomes: TagKey<Biome>,
        start: Double,
        end: Double,
        modifier: ResourceLocation? = null
    ): Holder.Reference<FogModifier> {
        return this.register(
            registryKey,
            FogModifier(
                lookup(Registries.BIOME).getOrThrow(biomes),
                10,
                start,
                end,
                modifier
            )
        )
    }

    fun fogFromBiome(registryManager: RegistryAccess, biome: Holder<Biome>): Holder<FogModifier> {
        val registry = registryManager.registryOrThrow(FOG_MODIFIER)
        val mmodifier = registry.holders()
            .filter { it.value().biomes.contains(biome) }.findFirst()
            .or { registry.getHolder(DuskFogModifiers.DEFAULT) }
        Objects.requireNonNull(registry)
        return mmodifier.or { registry.any }.orElseThrow()
    }
}