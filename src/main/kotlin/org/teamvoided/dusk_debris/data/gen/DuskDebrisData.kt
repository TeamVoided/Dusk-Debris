package org.teamvoided.dusk_debris.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.data.gen.providers.DamageTypeProvider
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider
import org.teamvoided.dusk_debris.data.gen.providers.english_translation.EnglishTranslationProvider
import org.teamvoided.dusk_debris.data.gen.providers.FogModifiers
import org.teamvoided.dusk_debris.data.gen.providers.Spells
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
        gen.add(RegistryKeys.BIOME, BiomeCreator::boostrap)
        gen.add(RegistryKeys.CONFIGURED_CARVER, ConfiguredCarverCreator::bootstrap)
        gen.add(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatureCreator::bootstrap)
        gen.add(RegistryKeys.PLACED_FEATURE, PlacedFeatureCreator::bootstrap)

        gen.add(RegistryKeys.NOISE_PARAMETERS, NoiseCreator::bootstrap)
        gen.add(RegistryKeys.DENSITY_FUNCTION, DensityFunctionCreator::bootstrap)
        gen.add(RegistryKeys.CHUNK_GENERATOR_SETTINGS, NoiseSettingsGenerator::bootstrap)
        gen.add(RegistryKeys.DIMENSION, DimensionCreator::bootstrap)


        gen.add(RegistryKeys.STRUCTURE_POOL, StructurePoolCreator::bootstrap)
        gen.add(RegistryKeys.STRUCTURE_FEATURE, StructureCreator::bootstrap)
        gen.add(RegistryKeys.STRUCTURE_SET, StructureSetCreator::bootstrap)

        gen.add(RegistryKeys.DAMAGE_TYPE, DamageTypeProvider::bootstrap)
        gen.add(RegistryKeys.ENCHANTMENT, EnchantmentsProvider::bootstrap)

        gen.add(RegistryKeys.PAINTING_VARIANT, PaintingVariants::bootstrap)
        gen.add(DuskRegistryKeys.SNIFFER_VARIANT, SnifferVariants::bootstrap)
        gen.add(DuskRegistryKeys.FOG_MODIFIER, FogModifiers::bootstrap)
        gen.add(DuskRegistryKeys.SPELL, Spells::bootstrap)
        println("End build registry")
    }
}
