package org.teamvoided.dusk_debris.init.worldgen

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionOptions
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

        addCarver(
            "add_amethyst_geode_carver",
            DuskConfiguredCarvers.AMETHYST_GEODE,
            BiomeSelectors.foundInOverworld()
                .and(tagNo(ConventionalBiomeTags.IS_DEEP_OCEAN))
        )

        //addFeature(
        //    "add_large_rock_spire",
        //    GenerationStep.Feature.LOCAL_MODIFICATIONS,
        //    DuskPlacedFeatures.LARGE_ROCK_SPIRE,
        //    BiomeSelectors.foundInOverworld()
        //        .and(tagNo(ConventionalBiomeTags.IS_RIVER))
        //)

    }

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placed: RegistryKey<PlacedFeature>,
        biome: TagKey<Biome>
    ) = addFeature(id, generationStep, placed, tag(biome))

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placed: RegistryKey<PlacedFeature>,
        include: TagKey<Biome>,
        exclude: TagKey<Biome>
    ) = addFeature(id, generationStep, placed, tag(include).and(tagNo(exclude)))

    private fun addFeature(
        id: String,
        generationStep: GenerationStep.Feature,
        placed: RegistryKey<PlacedFeature>,
        biome: Predicate<BiomeSelectionContext>
    ) = BiomeModifications.create(id(id)).add(ModificationPhase.ADDITIONS, biome) { it ->
        it.generationSettings.addFeature(generationStep, placed)
    }

    private fun addCarver(
        id: String,
        placed: RegistryKey<ConfiguredCarver<*>>,
        biome: TagKey<Biome>,
    ) = addCarver(id, placed, BiomeSelectors.tag(biome))

    private fun addCarver(
        id: String,
        placed: RegistryKey<ConfiguredCarver<*>>,
        include: TagKey<Biome>,
        exclude: TagKey<Biome>
    ) = addCarver(id, placed, BiomeSelectors.tag(include).and(tagNo(exclude)))


    private fun addCarver(
        id: String,
        placed: RegistryKey<ConfiguredCarver<*>>,
        biome: Predicate<BiomeSelectionContext>
    ) = BiomeModifications.create(id(id)).add(ModificationPhase.ADDITIONS, biome) { it ->
        it.generationSettings.addCarver(GenerationStep.Carver.AIR, placed)
    }

    private fun tagNo(tag: TagKey<Biome>): Predicate<BiomeSelectionContext> = Predicate { !it.hasTag(tag) }
    private fun tag(tag: TagKey<Biome>): Predicate<BiomeSelectionContext> = Predicate { it.hasTag(tag) }

    private fun notFoundInTheEnd(): Predicate<BiomeSelectionContext> =
        Predicate { !it.canGenerateIn(DimensionOptions.END) }

}