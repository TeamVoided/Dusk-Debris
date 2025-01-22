package org.teamvoided.dusk_debris.data.gen.providers.variants

import net.minecraft.registry.*
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.data.variants.DuskSnifferVariants
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant
import org.teamvoided.dusk_debris.init.DuskRegistries.SNIFFER_VARIANT
import java.util.*

object SnifferVariants {

    val defaultOverlay = texture("default")

    @JvmField
    val DEFAULT = DuskSnifferVariants.DEFAULT

    fun bootstrap(c: BootstrapContext<SnifferVariant>) {
        println("sniffer")
        println(" ")
        println(" ")
        val d = c.registerDefault(DuskSnifferVariants.DEFAULT, BiomeTags.OVERWORLD)
        c.register(DuskSnifferVariants.BRIGHT, DuskBiomeTags.SNIFFER_BRIGHT, Biomes.JUNGLE)
        c.register(DuskSnifferVariants.SWAMP, DuskBiomeTags.SNIFFER_SWAMP, Biomes.SWAMP)
        c.register(DuskSnifferVariants.MANGROVE_SWAMP, DuskBiomeTags.SNIFFER_MANGROVE_SWAMP, Biomes.MANGROVE_SWAMP)
        c.register(DuskSnifferVariants.BADLANDS, DuskBiomeTags.SNIFFER_BADLANDS, Biomes.BADLANDS)
        c.register(DuskSnifferVariants.BIRCH, DuskBiomeTags.SNIFFER_BIRCH, Biomes.OLD_GROWTH_BIRCH_FOREST)
        c.register(DuskSnifferVariants.COLD, DuskBiomeTags.SNIFFER_COLD, Biomes.TAIGA)
        c.register(DuskSnifferVariants.WARM, DuskBiomeTags.SNIFFER_WARM, Biomes.SAVANNA)
        c.register(DuskSnifferVariants.CRIMSON, DuskBiomeTags.SNIFFER_CRIMSON, 0x941818)
        c.register(DuskSnifferVariants.WARPED, DuskBiomeTags.SNIFFER_WARPED, 0x167E86)
        c.register(DuskSnifferVariants.ASHEN, DuskBiomeTags.SNIFFER_ASHEN, 0x7F7F7F)
        c.register(DuskSnifferVariants.PINK, DuskBiomeTags.SNIFFER_PINK, 0xFCCBE7)
        c.register(DuskSnifferVariants.CHERRY, DuskBiomeTags.SNIFFER_CHERRY, Biomes.CHERRY_GROVE)
        c.register(DuskSnifferVariants.SNOW, DuskBiomeTags.SNIFFER_SNOW, Biomes.SNOWY_SLOPES)
        c.register(DuskSnifferVariants.FROZEN, DuskBiomeTags.SNIFFER_FROZEN, 0xFFFFFF)
        c.register(DuskSnifferVariants.DEEP_DARK, DuskBiomeTags.SNIFFER_DEEP_DARK, texture("sculk"))

        println(d.registeredName)
    }

    fun BootstrapContext<SnifferVariant>.registerDefault(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: TagKey<Biome>
    ): Holder.Reference<SnifferVariant> {
        return this.register(registryKey, getRegistryLookup(RegistryKeys.BIOME).getTagOrThrow(biomes))
    }

    fun BootstrapContext<SnifferVariant>.register(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: TagKey<Biome>,
        biomeColor: RegistryKey<Biome>
    ): Holder.Reference<SnifferVariant> {
        return this.register(
            registryKey,
            getRegistryLookup(RegistryKeys.BIOME).getTagOrThrow(biomes),
            null,
            biomeColor.value,
            defaultOverlay
        )
    }

    fun BootstrapContext<SnifferVariant>.register(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: HolderSet<Biome>,
        color: Int
    ): Holder.Reference<SnifferVariant> {
        return this.register(
            registryKey,
            biomes,
            color,
            null,
            defaultOverlay
        )
    }

    fun BootstrapContext<SnifferVariant>.register(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: TagKey<Biome>,
        overlayTexture: Identifier
    ): Holder.Reference<SnifferVariant> {
        return this.register(
            registryKey,
            biomes,
            null,
            null,
            overlayTexture
        )
    }

    private fun BootstrapContext<SnifferVariant>.register(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: TagKey<Biome>,
        color: Int? = null,
        biomeColor: Identifier? = null,
        overlayTexture: Identifier? = null
    ): Holder.Reference<SnifferVariant> {
        return this.register(
            registryKey,
            SnifferVariant(
                getRegistryLookup(RegistryKeys.BIOME).getTagOrThrow(biomes),
                color,
                biomeColor,
                overlayTexture
            )
        )
    }

    private fun BootstrapContext<SnifferVariant>.register(
        registryKey: RegistryKey<SnifferVariant>,
        biomes: HolderSet<Biome>,
        color: Int? = null,
        biomeColor: Identifier? = null,
        overlayTexture: Identifier? = null
    ): Holder.Reference<SnifferVariant> {
        return this.register(registryKey, SnifferVariant(biomes, color, biomeColor, overlayTexture))
    }


    fun texture(name: String): Identifier = texture(null, name)

    fun texture(idenifier: String?, name: String): Identifier {
        val path = "entity/sniffer/$name"
        return if (idenifier != null) id(idenifier, path) else id(path)
    }

    fun fromBiome(registryManager: DynamicRegistryManager, biome: Holder<Biome>): Holder<SnifferVariant> {
        val registry = registryManager.get(SNIFFER_VARIANT)
        val variant = registry.holders()
            .filter { it.value().biomes.contains(biome) }.findAny()
            .or { registry.getHolder(DuskSnifferVariants.DEFAULT) }
        Objects.requireNonNull(registry)
        return variant.or { registry.any }.orElseThrow()
    }
}