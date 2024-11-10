package org.teamvoided.dusk_debris.init.worldgen

import net.fabricmc.fabric.api.biome.v1.*
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.data.DuskPlacedFeatures
import java.util.function.Predicate


object DuskBiomeModifications {
    fun init() {
        addFeature(
            "add_normal_blue_nethershroom",
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            ConventionalBiomeTags.IS_NETHER,
            DuskBiomeTags.WARPED
        )
        addFeature(
            "add_big_blue_nethershroom",
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            DuskBiomeTags.WARPED
        )
        addFeature(
            "add_normal_purple_nethershroom",
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            ConventionalBiomeTags.IS_NETHER,
            DuskBiomeTags.CRIMSON
        )
        addFeature(
            "add_big_purple_nethershroom",
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            DuskBiomeTags.CRIMSON
        )

        addFeature(
            "add_torus",
            GenerationStep.Feature.LOCAL_MODIFICATIONS,
            DuskPlacedFeatures.TORUS,
            DuskBiomeTags.TEST
        )
    }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placedFeature: RegistryKey<PlacedFeature>,
        biome: TagKey<Biome>
    ) {
        BiomeModifications.create(id(id)).add(
            ModificationPhase.ADDITIONS, BiomeSelectors.tag(biome)
        ) { context: BiomeModificationContext ->
            context.generationSettings.addFeature(generationStep, placedFeature)
        }
    }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placedFeature: RegistryKey<PlacedFeature>,
        include: TagKey<Biome>,
        exclude: TagKey<Biome>
    ) {
        BiomeModifications.create(id(id)).add(
            ModificationPhase.ADDITIONS, BiomeSelectors.tag(include).and(tagNo(exclude))
        ) { context: BiomeModificationContext ->
            context.generationSettings.addFeature(generationStep, placedFeature)
        }
    }

    fun tagNo(tag: TagKey<Biome>): Predicate<BiomeSelectionContext> {
        return Predicate { context: BiomeSelectionContext -> !context.hasTag(tag) }
    }

    private fun addFeature(
        id: String,
        placedFeature: RegistryKey<PlacedFeature>,
        biome: TagKey<Biome>
    ) {
        addFeature(id, GenerationStep.Feature.VEGETAL_DECORATION, placedFeature, biome)
    }

    private fun addFeature(
        id: String,
        placedFeature: RegistryKey<PlacedFeature>,
        include: TagKey<Biome>,
        exclude: TagKey<Biome>
    ) {
        addFeature(id, GenerationStep.Feature.VEGETAL_DECORATION, placedFeature, include, exclude)
    }
}