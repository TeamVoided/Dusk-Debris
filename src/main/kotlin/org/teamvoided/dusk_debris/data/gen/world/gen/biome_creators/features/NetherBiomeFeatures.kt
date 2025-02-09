package org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.features

import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.carver.ConfiguredCarvers
import net.minecraft.world.gen.feature.DefaultBiomeFeatures
import net.minecraft.world.gen.feature.NetherPlacedFeatures
import net.minecraft.world.gen.feature.OrePlacedFeatures
import net.minecraft.world.gen.feature.VegetationPlacedFeatures

object NetherBiomeFeatures {
    fun addNetherCarvers(generationSettings: GenerationSettings.Builder) {
        generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
//        generationSettings.carver(GenerationStep.Carver.AIR, DuskConfiguredCarvers.NETHER_CANYON)
    }

    fun addDefaultNetherFeatures(
        generationSettings: GenerationSettings.Builder,
        springDouble: Boolean,
        hasMushrooms: Boolean,
        hasNetherMushrooms: Boolean,
        hasSoulFire: Boolean,
    ) {
        if (hasMushrooms) DefaultBiomeFeatures.addDefaultMushrooms(generationSettings)
        if (!springDouble) generationSettings.feature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            NetherPlacedFeatures.SPRING_OPEN
        )
//        generationSettings.feature(GenerationStep.Feature.LAKES, DuskPlacedFeatures.LAKE_LAVA_NETHER)
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_FIRE)
        if (hasSoulFire) generationSettings.feature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            NetherPlacedFeatures.PATCH_SOUL_FIRE
        )
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE_EXTRA)
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE)
        generationSettings.feature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            NetherPlacedFeatures.PATCH_CRIMSON_ROOTS
        )
        if (hasNetherMushrooms) {
            generationSettings.feature(
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                VegetationPlacedFeatures.BROWN_MUSHROOM_NETHER
            )
            generationSettings.feature(
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                VegetationPlacedFeatures.RED_MUSHROOM_NETHER
            )
        }
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_MAGMA)
        if (!springDouble) generationSettings.feature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            NetherPlacedFeatures.SPRING_CLOSED
        )
        else generationSettings.feature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            NetherPlacedFeatures.SPRING_CLOSED_DOUBLE
        )
    }

    fun addNetherWastesFeatures(generationSettings: GenerationSettings.Builder) {
        addDefaultNetherFeatures(generationSettings, false, true, true, true)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addCrimsonFeatures(generationSettings: GenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false, false)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, NetherPlacedFeatures.WEEPING_VINES)
//        generationSettings.feature(
//            GenerationStep.Feature.VEGETAL_DECORATION,
//            if (ancientFlourish) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else if (forest) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else DuskPlacedFeatures.CRIMSON_WASTES_FUNGI
//        )
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, NetherPlacedFeatures.CRIMSON_FOREST_VEGETATION)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addWarpedFeatures(generationSettings: GenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false, true)
//        generationSettings.feature(
//            GenerationStep.Feature.VEGETAL_DECORATION,
//            if (ancientFlourish) DuskPlacedFeatures.WARPED_FOREST_FUNGI else if (forest) DuskPlacedFeatures.WARPED_FOREST_FUNGI else DuskPlacedFeatures.WARPED_WASTES_FUNGI
//        )
        generationSettings.feature(
            GenerationStep.Feature.VEGETAL_DECORATION,
            NetherPlacedFeatures.WARPED_FOREST_VEGETATION
        )
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, NetherPlacedFeatures.NETHER_SPROUTS)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, NetherPlacedFeatures.TWISTING_VINES)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addBasaltDeltaFeatures(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.DELTA)
        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.SMALL_BASALT_COLUMNS)
        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, NetherPlacedFeatures.LARGE_BASALT_COLUMNS)
//        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, DuskPlacedFeatures.BASALT_BLOBS)
//        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, DuskPlacedFeatures.BLACKSTONE_BLOBS)
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_DELTA)
        addDefaultNetherFeatures(generationSettings, true, false, true, true)
        addBasaltMineables(generationSettings)
    }
    fun addBasaltMineables(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_GOLD_DELTAS)
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_QUARTZ_DELTAS)
        DefaultBiomeFeatures.addAncientDebris(generationSettings)
    }

    fun addSoulValleyFeatures(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, NetherPlacedFeatures.BASALT_PILLAR)
        addDefaultNetherFeatures(generationSettings, false, false, false, true)
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_SOUL_SAND)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }
}