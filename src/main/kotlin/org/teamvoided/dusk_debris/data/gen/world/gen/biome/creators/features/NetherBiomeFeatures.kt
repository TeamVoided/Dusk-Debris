package org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.features

import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.Carvers
import net.minecraft.data.worldgen.placement.NetherPlacements
import net.minecraft.data.worldgen.placement.OrePlacements
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.levelgen.GenerationStep
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.LOCAL_MODIFICATIONS as lm2
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.SURFACE_STRUCTURES as ss4
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_DECORATION as ud7
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION as vd9

object NetherBiomeFeatures {
    fun addNetherCarvers(generationSettings: BiomeGenerationSettings.Builder) {
        generationSettings.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
//        generationSettings.carver(GenerationStep.Carver.AIR, DuskConfiguredCarvers.NETHER_CANYON)
    }

    fun addDefaultNetherFeatures(
        generationSettings: BiomeGenerationSettings.Builder,
        springDouble: Boolean,
        hasMushrooms: Boolean,
        hasNetherMushrooms: Boolean,
        hasSoulFire: Boolean = true,
    ) {
        if (hasMushrooms) BiomeDefaultFeatures.addDefaultMushrooms(generationSettings)
        if (!springDouble) generationSettings.addFeature(ud7, NetherPlacements.SPRING_OPEN)
//        generationSettings.feature(l1, DuskPlacedFeatures.LAKE_LAVA_NETHER)
        generationSettings.addFeature(ud7, NetherPlacements.PATCH_FIRE)
        if (hasSoulFire) generationSettings.addFeature(ud7, NetherPlacements.PATCH_SOUL_FIRE)
        generationSettings.addFeature(ud7, NetherPlacements.GLOWSTONE_EXTRA)
        generationSettings.addFeature(ud7, NetherPlacements.GLOWSTONE)
        generationSettings.addFeature(ud7, NetherPlacements.PATCH_CRIMSON_ROOTS)
        if (hasNetherMushrooms) {
            generationSettings.addFeature(ud7, VegetationPlacements.BROWN_MUSHROOM_NETHER)
            generationSettings.addFeature(ud7, VegetationPlacements.RED_MUSHROOM_NETHER)
        }
        generationSettings.addFeature(ud7, OrePlacements.ORE_MAGMA)
        if (!springDouble) generationSettings.addFeature(ud7, NetherPlacements.SPRING_CLOSED)
        else generationSettings.addFeature(ud7, NetherPlacements.SPRING_CLOSED_DOUBLE)
        generationSettings.addFeature(lm2, DuskPlacedFeatures.BLACKSTONE_STRIPS)
    }

    fun addNetherWastesFeatures(generationSettings: BiomeGenerationSettings.Builder) {
        addDefaultNetherFeatures(generationSettings, false, true, true)
        BiomeDefaultFeatures.addNetherDefaultOres(generationSettings)
    }

    fun addCrimsonFeatures(generationSettings: BiomeGenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false, false)
        generationSettings.addFeature(vd9, NetherPlacements.WEEPING_VINES)
//        generationSettings.feature(
//            vd9,
//            if (ancientFlourish) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else if (forest) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else DuskPlacedFeatures.CRIMSON_WASTES_FUNGI
//        )
        generationSettings.addFeature(vd9, NetherPlacements.CRIMSON_FOREST_VEGETATION)
        BiomeDefaultFeatures.addNetherDefaultOres(generationSettings)
    }

    fun addWarpedFeatures(generationSettings: BiomeGenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false)
//        generationSettings.feature(
//            vd9,
//            if (ancientFlourish) DuskPlacedFeatures.WARPED_FOREST_FUNGI else if (forest) DuskPlacedFeatures.WARPED_FOREST_FUNGI else DuskPlacedFeatures.WARPED_WASTES_FUNGI
//        )
        generationSettings.addFeature(vd9, NetherPlacements.WARPED_FOREST_VEGETATION)
        generationSettings.addFeature(vd9, NetherPlacements.NETHER_SPROUTS)
        generationSettings.addFeature(vd9, NetherPlacements.TWISTING_VINES)
        BiomeDefaultFeatures.addNetherDefaultOres(generationSettings)
    }

    fun addBasaltDeltaFeatures(generationSettings: BiomeGenerationSettings.Builder) {
        generationSettings.addFeature(ss4, NetherPlacements.DELTA)
        generationSettings.addFeature(ss4, NetherPlacements.SMALL_BASALT_COLUMNS)
        generationSettings.addFeature(ss4, NetherPlacements.LARGE_BASALT_COLUMNS)
//        generationSettings.feature(ud7, DuskPlacedFeatures.BASALT_BLOBS)
//        generationSettings.feature(ud7, DuskPlacedFeatures.BLACKSTONE_BLOBS)
        generationSettings.addFeature(ud7, NetherPlacements.SPRING_DELTA)
        addDefaultNetherFeatures(generationSettings, true, false, true)
        addBasaltMineables(generationSettings)
    }

    fun addBasaltMineables(generationSettings: BiomeGenerationSettings.Builder) {
        generationSettings.addFeature(ud7, OrePlacements.ORE_GOLD_DELTAS)
        generationSettings.addFeature(ud7, OrePlacements.ORE_QUARTZ_DELTAS)
        BiomeDefaultFeatures.addAncientDebris(generationSettings)
    }

    fun addSoulValleyFeatures(generationSettings: BiomeGenerationSettings.Builder) {
        generationSettings.addFeature(lm2, NetherPlacements.BASALT_PILLAR)
        addDefaultNetherFeatures(generationSettings, false, false, false)
        generationSettings.addFeature(ud7, OrePlacements.ORE_SOUL_SAND)
        BiomeDefaultFeatures.addNetherDefaultOres(generationSettings)
    }
}