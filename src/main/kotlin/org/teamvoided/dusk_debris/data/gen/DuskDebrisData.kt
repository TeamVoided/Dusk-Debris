package org.teamvoided.dusk_debris.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.data.gen.providers.*
import org.teamvoided.dusk_debris.data.gen.providers.loot_table.EntityLootTableProvider
import org.teamvoided.dusk_debris.data.gen.providers.variants.PaintingVariants
import org.teamvoided.dusk_debris.data.gen.tags.*
import org.teamvoided.dusk_debris.data.gen.world.gen.BiomeCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.ConfiguredFeatureCreator
import org.teamvoided.dusk_debris.data.gen.world.gen.PlacedFeatureCreator
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.init.DuskRegistries

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
        gen.add(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatureCreator::bootstrap)
        gen.add(RegistryKeys.PLACED_FEATURE, PlacedFeatureCreator::bootstrap)

        gen.add(RegistryKeys.DAMAGE_TYPE, DamageTypeProvider::bootstrap)
        gen.add(RegistryKeys.ENCHANTMENT, EnchantmentsProvider::bootstrap)

        gen.add(RegistryKeys.PAINTING_VARIANT, PaintingVariants::bootstrap)
        gen.add(DuskRegistries.SNIFFER_VARIANT, SnifferVariants::bootstrap)
        println("End build registry")
    }
}
