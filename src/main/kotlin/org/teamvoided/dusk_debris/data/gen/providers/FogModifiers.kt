package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.entity.passive.WolfVariant
import net.minecraft.entity.passive.WolfVariants
import net.minecraft.registry.*
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.data.DuskFogModifiers
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.init.DuskRegistries.FOG_MODIFIER
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
        registryKey: RegistryKey<FogModifier>,
        biomes: TagKey<Biome>,
        modifier: Identifier? = null
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
        registryKey: RegistryKey<FogModifier>,
        biomes: TagKey<Biome>,
        start: Double,
        end: Double,
        modifier: Identifier? = null
    ): Holder.Reference<FogModifier> {
        return this.register(
            registryKey,
            FogModifier(
                getRegistryLookup(RegistryKeys.BIOME).getTagOrThrow(biomes),
                10,
                start,
                end,
                modifier
            )
        )
    }

    fun fogFromBiome(registryManager: DynamicRegistryManager, biome: Holder<Biome>): Holder<FogModifier> {
        val registry = registryManager.get(FOG_MODIFIER)
        val mmodifier = registry.holders()
            .filter { it.value().biomes.contains(biome) }.findFirst()
            .or { registry.getHolder(DuskFogModifiers.DEFAULT) }
        Objects.requireNonNull(registry)
        return mmodifier.or { registry.any }.orElseThrow()
    }
}