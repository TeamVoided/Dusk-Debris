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
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.HollowKnightBiomeCreators.createFogCanyon
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createBasaltDeltas
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createCrimsonForest
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createNetherTest
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createNetherWastes
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createSoulSandValley
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.NetherBiomeCreators.createWarpedForest
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredCarvers
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes

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
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            this.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
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