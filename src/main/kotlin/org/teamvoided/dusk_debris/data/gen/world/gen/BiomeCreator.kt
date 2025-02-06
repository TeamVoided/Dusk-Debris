package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.client.sound.MusicType
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BiomeMoodSound
import net.minecraft.sound.SoundEvents
import net.minecraft.world.biome.*
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.DefaultBiomeFeatures
import net.minecraft.world.gen.feature.VegetationPlacedFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.worldgen.DuskBiomes

object BiomeCreator {
    fun boostrap(context: BootstrapContext<Biome>) {
        context.register(DuskBiomes.BOREAL_VALLEY, createFreezingForest(context))
        context.register(DuskBiomes.FOG_CANYON, createFogCanyon(context))
        context.register(DuskBiomes.TEST, createTest(context))
    }

    fun createFreezingForest(c: BootstrapContext<Biome>): Biome {
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            c.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            c.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        )

        DefaultBiomeFeatures.addFarmAnimals(spawnSettings)
        spawnSettings.spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.WOLF, 8, 4, 4))
        spawnSettings.spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3))
        spawnSettings.spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.FOX, 8, 2, 4))
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings)

        OverworldBiomeCreator.addBasicFeatures(generationSettings)
        DefaultBiomeFeatures.addMossyRocks(generationSettings)
        DefaultBiomeFeatures.addLargeFerns(generationSettings)
        DefaultBiomeFeatures.addDefaultOres(generationSettings)
        DefaultBiomeFeatures.addDefaultDisks(generationSettings)
        generationSettings.feature(
            GenerationStep.Feature.VEGETAL_DECORATION,
            DuskPlacedFeatures.BOREAL_VALLEY_VEGETATION
        )
        DefaultBiomeFeatures.addDefaultFlowers(generationSettings)
        DefaultBiomeFeatures.addGiantTaigaGrass(generationSettings)
        DefaultBiomeFeatures.addDefaultMushrooms(generationSettings)
        DefaultBiomeFeatures.addDefaultVegetation(generationSettings)
        DefaultBiomeFeatures.addCommonBerries(generationSettings)
        val musicSound = MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_OLD_GROWTH_TAIGA)
        return Biome.Builder().temperature(-0.25f).downfall(0.6f).effects(
            BiomeEffects.Builder()
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(12638463)
                .skyColor(OverworldBiomeCreator.getSkyColor(-0.25f))
                .moodSound(BiomeMoodSound.CAVE)
                .music(musicSound)
                .build()
        ).spawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }

//    0xC196E0


    fun createFogCanyon(c: BootstrapContext<Biome>): Biome {
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            c.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            c.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        )
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, SpawnSettings.SpawnEntry(EntityType.TROPICAL_FISH, 25, 8, 8))
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings)
        OverworldBiomeCreator.addBasicFeatures(generationSettings)
        DefaultBiomeFeatures.addPlainsTallGrass(generationSettings)
        DefaultBiomeFeatures.addDefaultOres(generationSettings)
        DefaultBiomeFeatures.addClayOre(generationSettings)
        DefaultBiomeFeatures.addDefaultDisks(generationSettings)
        DefaultBiomeFeatures.addLushCavesDecoration(generationSettings)
        val musicSound = MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES)
        return Biome.Builder()
            .temperature(0.25f)
            .downfall(0.6f)
            .effects(
                BiomeEffects.Builder()
                    .grassColor(0x316B9E)
                    .foliageColor(0x307096)
                    .waterColor(6254825)
                    .waterFogColor(1836338)
                    .fogColor(0xC196E0)
                    .skyColor(0x774E96)
                    .particleConfig(BiomeParticleConfig(DuskParticles.PURPLE_BIOME_BUBBLE, 0.00025f))
                    .moodSound(BiomeMoodSound.CAVE)
                    .music(musicSound)
                    .build()
            ).spawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }

    fun createTest(c: BootstrapContext<Biome>): Biome {
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            c.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            c.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        )

        generationSettings.carver(GenerationStep.Carver.AIR, DuskConfiguredCarvers.LAKE)
        generationSettings.carver(GenerationStep.Carver.AIR, DuskConfiguredCarvers.LAVA_LAKE)

        DefaultBiomeFeatures.addDefaultDisks(generationSettings)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_PLAINS)

        DefaultBiomeFeatures.addDefaultMushrooms(generationSettings)
        DefaultBiomeFeatures.addDefaultVegetation(generationSettings)

        return OverworldBiomeCreator.create(
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