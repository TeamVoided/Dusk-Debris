package org.teamvoided.dusk_debris.init.worldgen

import net.fabricmc.fabric.api.biome.v1.*
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.carver.ConfiguredCarver
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import java.util.function.Predicate


object DuskBiomeModifications {
    fun init() {
        addFeature(
            "add_normal_blue_nethershroom",
            GenerationStep.Feature.VEGETAL_DECORATION,
            DuskPlacedFeatures.BLUE_NETHERSHROOM_PATCH,
            ConventionalBiomeTags.IS_NETHER,
            DuskBiomeTags.WARPED
        )
        addFeature(
            "add_big_blue_nethershroom",
            GenerationStep.Feature.VEGETAL_DECORATION,
            DuskPlacedFeatures.WARPED_BLUE_NETHERSHROOM_PATCH,
            DuskBiomeTags.WARPED,
        )
        addFeature(
            "add_normal_purple_nethershroom",
            GenerationStep.Feature.VEGETAL_DECORATION,
            DuskPlacedFeatures.PURPLE_NETHERSHROOM_PATCH,
            ConventionalBiomeTags.IS_NETHER,
            DuskBiomeTags.CRIMSON
        )
        addFeature(
            "add_big_purple_nethershroom",
            GenerationStep.Feature.VEGETAL_DECORATION,
            DuskPlacedFeatures.CRIMSON_PURPLE_NETHERSHROOM_PATCH,
            DuskBiomeTags.CRIMSON
        )

//        addFeature(
//            "add_torus",
//            GenerationStep.Feature.LOCAL_MODIFICATIONS,
//            DuskPlacedFeatures.TORUS,
//            DuskBiomeTags.TEST
//        )

//        addFeature(
//            "add_overworld_torus",
//            GenerationStep.Feature.LOCAL_MODIFICATIONS,
//            DuskPlacedFeatures.OVERWORLD_TORUS,
//            BiomeTags.OVERWORLD
//        )

//        addCarver(
//            "add_lake_carvers",
//            DuskConfiguredCarvers.LAKE,
//            BiomeTags.OVERWORLD
//        )
    }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placedFeature: RegistryKey<PlacedFeature>,
        biome: TagKey<Biome>
    ) = BiomeModifications.create(id(id)).add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(biome)) { it ->
        it.generationSettings.addFeature(generationStep, placedFeature)
    }


    private fun addCarver(id: String, placedFeature: RegistryKey<ConfiguredCarver<*>>, biome: TagKey<Biome>) =
        BiomeModifications.create(id(id)).add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(biome)) { it ->
            it.generationSettings.addCarver(GenerationStep.Carver.AIR, placedFeature)
        }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placedFeature: RegistryKey<PlacedFeature>,
        include: TagKey<Biome>,
        exclude: TagKey<Biome>
    ) = BiomeModifications.create(id(id))
        .add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(include).and(tagNo(exclude))) { it ->
            it.generationSettings.addFeature(generationStep, placedFeature)
        }


    private fun tagNo(tag: TagKey<Biome>): Predicate<BiomeSelectionContext> = Predicate { !it.hasTag(tag) }
}