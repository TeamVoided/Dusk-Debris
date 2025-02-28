package org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.features

import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.GenerationStep.Feature.LOCAL_MODIFICATIONS as lm2
import net.minecraft.world.gen.GenerationStep.Feature.SURFACE_STRUCTURES as ss4
import net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_DECORATION as ud7
import net.minecraft.world.gen.GenerationStep.Feature.VEGETAL_DECORATION as vd9
import net.minecraft.world.gen.carver.ConfiguredCarvers
import net.minecraft.world.gen.feature.DefaultBiomeFeatures
import net.minecraft.world.gen.feature.NetherPlacedFeatures
import net.minecraft.world.gen.feature.OrePlacedFeatures
import net.minecraft.world.gen.feature.VegetationPlacedFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures

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
        hasSoulFire: Boolean = true,
    ) {
        if (hasMushrooms) DefaultBiomeFeatures.addDefaultMushrooms(generationSettings)
        if (!springDouble) generationSettings.feature(ud7, NetherPlacedFeatures.SPRING_OPEN)
//        generationSettings.feature(l1, DuskPlacedFeatures.LAKE_LAVA_NETHER)
        generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_FIRE)
        if (hasSoulFire) generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_SOUL_FIRE)
        generationSettings.feature(ud7, NetherPlacedFeatures.GLOWSTONE_EXTRA)
        generationSettings.feature(ud7, NetherPlacedFeatures.GLOWSTONE)
        generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_CRIMSON_ROOTS)
        if (hasNetherMushrooms) {
            generationSettings.feature(ud7, VegetationPlacedFeatures.BROWN_MUSHROOM_NETHER)
            generationSettings.feature(ud7, VegetationPlacedFeatures.RED_MUSHROOM_NETHER)
        }
        generationSettings.feature(ud7, OrePlacedFeatures.ORE_MAGMA)
        if (!springDouble) generationSettings.feature(ud7, NetherPlacedFeatures.SPRING_CLOSED)
        else generationSettings.feature(ud7, NetherPlacedFeatures.SPRING_CLOSED_DOUBLE)
        generationSettings.feature(lm2, DuskPlacedFeatures.BLACKSTONE_STRIPS)
    }

    fun addNetherWastesFeatures(generationSettings: GenerationSettings.Builder) {
        addDefaultNetherFeatures(generationSettings, false, true, true)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addCrimsonFeatures(generationSettings: GenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false, false)
        generationSettings.feature(vd9, NetherPlacedFeatures.WEEPING_VINES)
//        generationSettings.feature(
//            vd9,
//            if (ancientFlourish) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else if (forest) DuskPlacedFeatures.CRIMSON_FOREST_FUNGI else DuskPlacedFeatures.CRIMSON_WASTES_FUNGI
//        )
        generationSettings.feature(vd9, NetherPlacedFeatures.CRIMSON_FOREST_VEGETATION)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addWarpedFeatures(generationSettings: GenerationSettings.Builder, ancientFlourish: Boolean, forest: Boolean) {
        addDefaultNetherFeatures(generationSettings, false, true, false)
//        generationSettings.feature(
//            vd9,
//            if (ancientFlourish) DuskPlacedFeatures.WARPED_FOREST_FUNGI else if (forest) DuskPlacedFeatures.WARPED_FOREST_FUNGI else DuskPlacedFeatures.WARPED_WASTES_FUNGI
//        )
        generationSettings.feature(vd9, NetherPlacedFeatures.WARPED_FOREST_VEGETATION)
        generationSettings.feature(vd9, NetherPlacedFeatures.NETHER_SPROUTS)
        generationSettings.feature(vd9, NetherPlacedFeatures.TWISTING_VINES)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }

    fun addBasaltDeltaFeatures(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(ss4, NetherPlacedFeatures.DELTA)
        generationSettings.feature(ss4, NetherPlacedFeatures.SMALL_BASALT_COLUMNS)
        generationSettings.feature(ss4, NetherPlacedFeatures.LARGE_BASALT_COLUMNS)
//        generationSettings.feature(ud7, DuskPlacedFeatures.BASALT_BLOBS)
//        generationSettings.feature(ud7, DuskPlacedFeatures.BLACKSTONE_BLOBS)
        generationSettings.feature(ud7, NetherPlacedFeatures.SPRING_DELTA)
        addDefaultNetherFeatures(generationSettings, true, false, true)
        addBasaltMineables(generationSettings)
    }

    fun addBasaltMineables(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(ud7, OrePlacedFeatures.ORE_GOLD_DELTAS)
        generationSettings.feature(ud7, OrePlacedFeatures.ORE_QUARTZ_DELTAS)
        DefaultBiomeFeatures.addAncientDebris(generationSettings)
    }

    fun addSoulValleyFeatures(generationSettings: GenerationSettings.Builder) {
        generationSettings.feature(lm2, NetherPlacedFeatures.BASALT_PILLAR)
        addDefaultNetherFeatures(generationSettings, false, false, false)
        generationSettings.feature(ud7, OrePlacedFeatures.ORE_SOUL_SAND)
        DefaultBiomeFeatures.addNetherMineables(generationSettings)
    }
}