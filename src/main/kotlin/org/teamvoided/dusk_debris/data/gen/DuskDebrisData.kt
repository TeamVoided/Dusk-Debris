package org.teamvoided.dusk_debris.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.data.gen.providers.DamageTypeProvider
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider
import org.teamvoided.dusk_debris.data.gen.providers.FogModifiers
import org.teamvoided.dusk_debris.data.gen.providers.Spells
import org.teamvoided.dusk_debris.data.gen.providers.english_translation.EnglishTranslationProvider
import org.teamvoided.dusk_debris.data.gen.providers.loot_table.EntityLootTableProvider
import org.teamvoided.dusk_debris.data.gen.providers.models.ModelProvider
import org.teamvoided.dusk_debris.data.gen.providers.variants.PaintingVariants
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.data.gen.tags.*
import org.teamvoided.dusk_debris.data.gen.world.gen.*
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.BiomeCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.structure.StructureCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.structure.StructurePoolCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.structure.StructureSetCreator
import org.teamvoided.dusk_debris.init.DuskRegistryKeys

@Suppress("unused")
class DuskDebrisData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()
        pack.addProvider(::DynamicRegistryProvider)
        val blockTags = pack.addProvider(::BlockTagsProvider)
        pack.addProvider(::FluidTagsProvider)
        pack.addProvider { o, r -> ItemTagsProvider(o, r, blockTags) }
        pack.addProvider(::BiomeTagsProvider)
        pack.addProvider(::EntityTypeTagsProvider)
        pack.addProvider(::DamageTypeTagsProvider)
        pack.addProvider(::EnchantmentTagsProvider)
        pack.addProvider(::PaintingVariantTagsProvider)
        pack.addProvider(::ModelProvider)
        pack.addProvider(::EnglishTranslationProvider)
//        pack.addProvider(::RecipesProvider)
//        pack.addProvider(::BlockLootTableProvider)
        pack.addProvider(::EntityLootTableProvider)
        println("Goodbye from Datagen")
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        println("Start build registry")
        gen.add(Registries.BIOME, BiomeCreator::boostrap)
        gen.add(Registries.CONFIGURED_CARVER, ConfiguredCarverCreator::bootstrap)
        gen.add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureCreator::bootstrap)
        gen.add(Registries.PLACED_FEATURE, PlacedFeatureCreator::bootstrap)

        gen.add(Registries.NOISE, NoiseCreator::bootstrap)
        gen.add(Registries.DENSITY_FUNCTION, DensityFunctionCreator::bootstrap)
        gen.add(Registries.NOISE_SETTINGS, NoiseSettingsGenerator::bootstrap)
        gen.add(Registries.LEVEL_STEM, DimensionCreator::bootstrap)


        gen.add(Registries.TEMPLATE_POOL, StructurePoolCreator::bootstrap)
        gen.add(Registries.STRUCTURE, StructureCreator::bootstrap)
        gen.add(Registries.STRUCTURE_SET, StructureSetCreator::bootstrap)

        gen.add(Registries.DAMAGE_TYPE, DamageTypeProvider::bootstrap)
        gen.add(Registries.ENCHANTMENT, EnchantmentsProvider::bootstrap)

        gen.add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap)
        gen.add(DuskRegistryKeys.SNIFFER_VARIANT, SnifferVariants::bootstrap)
        gen.add(DuskRegistryKeys.FOG_MODIFIER, FogModifiers::bootstrap)
        gen.add(DuskRegistryKeys.SPELL, Spells::bootstrap)
        println("End build registry")
    }
}
