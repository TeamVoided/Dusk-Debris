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
import org.teamvoided.dusk_debris.data.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskBiomes

object BiomeCreator {
    fun boostrap(context: BootstrapContext<Biome>) {
        context.register(DuskBiomes.FREEZING_WOODS, createFreezingForest(context))
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
            DuskPlacedFeatures.FREEZING_WOODS_VEGETATION
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