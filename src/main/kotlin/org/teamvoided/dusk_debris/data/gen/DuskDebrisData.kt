package org.teamvoided.dusk_debris.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.data.DuskSoundEvents
import org.teamvoided.dusk_debris.data.gen.providers.EnglishTranslationProvider
import org.teamvoided.dusk_debris.data.gen.providers.ModelProvider
import org.teamvoided.dusk_debris.data.gen.providers.RegistryProvider
import org.teamvoided.dusk_debris.data.gen.tags.BiomeTagsProvider
import org.teamvoided.dusk_debris.data.gen.tags.BlockTagsProvider
import org.teamvoided.dusk_debris.data.gen.tags.EntityTypeTagsProvider
import org.teamvoided.dusk_debris.data.gen.tags.ItemTagsProvider

@Suppress("unused")
class DuskDebrisData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()
//        pack.addProvider(::DuskAutumnsWorldGenerator)
        pack.addProvider(::BiomeTagsProvider)
        pack.addProvider(::BlockTagsProvider)
        pack.addProvider(::ItemTagsProvider)
        pack.addProvider(::EntityTypeTagsProvider)
        pack.addProvider(::ModelProvider)
        pack.addProvider(::EnglishTranslationProvider)
//        pack.addProvider(::RecipesProvider)
//        pack.addProvider(::BlockLootTableProvider)
//        pack.addProvider(::EntityLootTableProvider)
        pack.addProvider(::RegistryProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(DuskSoundEvents.registryKey, DuskSoundEvents::bootstrap)
//        gen.add(RegistryKeys.BIOME, DuskBiomes::boostrap)
//        gen.add(RegistryKeys.CONFIGURED_FEATURE, DuskConfiguredFeature::bootstrapConfiguredFeatures)
//        gen.add(RegistryKeys.PLACED_FEATURE, DuskPlacedFeature::bootstrapPlacedFeatures)
    }
}
