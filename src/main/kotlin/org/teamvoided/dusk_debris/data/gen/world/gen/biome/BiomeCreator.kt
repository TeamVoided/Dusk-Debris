package org.teamvoided.dusk_debris.data.gen.world.gen.biome

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.biome.OverworldBiomes
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.sounds.Musics
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.levelgen.GenerationStep
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.HollowKnightBiomeCreators.createFogCanyon
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createBasaltDeltas
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createCrimsonForest
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createNetherTest
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createNetherWastes
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createSoulSandValley
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.NetherBiomeCreators.createWarpedForest
import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures

object BiomeCreator {
    fun boostrap(c: BootstrapContext<Biome>) {
        c.register(DuskBiomes.TEST, createTest(c))

        c.register(DuskBiomes.BOREAL_VALLEY, c.createFreezingForest())

        c.register(DuskBiomes.FOG_CANYON, c.createFogCanyon())

        c.register(DuskBiomes.NETHER_TEST, c.createNetherTest())
        c.register(DuskBiomes.NETHER_WASTES, c.createNetherWastes())
        c.register(DuskBiomes.CRIMSON_FOREST, c.createCrimsonForest())
        c.register(DuskBiomes.CRIMSON_WASTES, c.createCrimsonForest(false, false))
        c.register(DuskBiomes.WARPED_FOREST, c.createWarpedForest())
        c.register(DuskBiomes.WARPED_WASTES, c.createWarpedForest(false, false))
        c.register(DuskBiomes.BASALT_DELTAS, c.createBasaltDeltas())
        c.register(DuskBiomes.SOUL_SAND_VALLEY, c.createSoulSandValley())
    }

    fun BootstrapContext<Biome>.createFreezingForest(): Biome {
        val spawnSettings = MobSpawnSettings.Builder()
        val generationSettings = BiomeGenerationSettings.Builder(
            this.lookup(Registries.PLACED_FEATURE),
            this.lookup(Registries.CONFIGURED_CARVER)
        )

        BiomeDefaultFeatures.farmAnimals(spawnSettings)
        spawnSettings.addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4))
        spawnSettings.addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3))
        spawnSettings.addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4))
        BiomeDefaultFeatures.commonSpawns(spawnSettings)

        OverworldBiomes.globalOverworldGeneration(generationSettings)
        BiomeDefaultFeatures.addMossyStoneBlock(generationSettings)
        BiomeDefaultFeatures.addFerns(generationSettings)
        BiomeDefaultFeatures.addDefaultOres(generationSettings)
        BiomeDefaultFeatures.addDefaultSoftDisks(generationSettings)
        generationSettings.addFeature(
            GenerationStep.Decoration.VEGETAL_DECORATION,
            DuskPlacedFeatures.BOREAL_VALLEY_VEGETATION
        )
        BiomeDefaultFeatures.addDefaultFlowers(generationSettings)
        BiomeDefaultFeatures.addGiantTaigaVegetation(generationSettings)
        BiomeDefaultFeatures.addDefaultMushrooms(generationSettings)
        BiomeDefaultFeatures.addDefaultExtraVegetation(generationSettings)
        BiomeDefaultFeatures.addCommonBerryBushes(generationSettings)
        val musicSound = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA)
        return Biome.BiomeBuilder().temperature(-0.25f).downfall(0.6f).specialEffects(
            BiomeSpecialEffects.Builder()
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(12638463)
                .skyColor(OverworldBiomes.calculateSkyColor(-0.25f))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(musicSound)
                .build()
        ).mobSpawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }

//    0xC196E0

    fun createTest(c: BootstrapContext<Biome>): Biome {
        val spawnSettings = MobSpawnSettings.Builder()
        val generationSettings = BiomeGenerationSettings.Builder(
            c.lookup(Registries.PLACED_FEATURE),
            c.lookup(Registries.CONFIGURED_CARVER)
        )

        generationSettings.addCarver(GenerationStep.Carving.AIR, DuskConfiguredCarvers.AMETHYST_GEODE)

        BiomeDefaultFeatures.addDefaultSoftDisks(generationSettings)
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS)

        BiomeDefaultFeatures.addDefaultMushrooms(generationSettings)
        BiomeDefaultFeatures.addDefaultExtraVegetation(generationSettings)

        return OverworldBiomes.biome(
            true,
            0.8f,
            0.4f,
            spawnSettings,
            generationSettings,
            null
        )
    }

    /*Generation Steps Reference:
      RAW_GENERATION
      LAKES
      LOCAL_MODIFICATIONS
      UNDERGROUND_STRUCTURES
      SURFACE_STRUCTURES
      STRONGHOLDS
      UNDERGROUND_ORES
      UNDERGROUND_DECORATION
      FLUID_SPRINGS
      VEGETAL_DECORATION
      TOP_LAYER_MODIFICATION
     */

}