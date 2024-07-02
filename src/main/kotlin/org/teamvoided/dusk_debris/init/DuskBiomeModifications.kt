package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.init.worldgen.DuskPlacedFeatures


object DuskBiomeModifications {
    fun init() {
        addFeature(
            "add_normal_blue_nethershroom",
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            Biomes.CRIMSON_FOREST
        )
        addFeature(
            "add_big_blue_nethershroom",
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            Biomes.WARPED_FOREST
        )
        addFeature(
            "add_normal_purple_nethershroom",
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            Biomes.CRIMSON_FOREST
        )
        addFeature(
            "add_big_purple_nethershroom",
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            Biomes.WARPED_FOREST
        )
    }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placedFeature: RegistryKey<PlacedFeature>,
        biome: RegistryKey<Biome>
    ) {
        BiomeModifications.create(id(id)).add(
            ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(biome)
        ) { context: BiomeModificationContext ->
            context.generationSettings.addFeature(generationStep, placedFeature)
        }
    }

    private fun addFeature(
        id: String,
        placedFeature: RegistryKey<PlacedFeature>,
        biome: RegistryKey<Biome>
    ) {
        addFeature(id, GenerationStep.Feature.VEGETAL_DECORATION, placedFeature, biome)
    }
}