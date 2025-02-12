package org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators

import net.minecraft.client.sound.MusicType
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BiomeMoodSound
import net.minecraft.sound.SoundEvents
import net.minecraft.world.biome.*
import net.minecraft.world.gen.feature.DefaultBiomeFeatures
import org.teamvoided.dusk_debris.init.DuskParticles

object HollowKnightBiomeCreators {
    fun BootstrapContext<Biome>.createFogCanyon(): Biome {
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            this.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        )
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, SpawnSettings.SpawnEntry(EntityType.TROPICAL_FISH, 25, 8, 8))
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings)
        OverworldBiomeCreator.addBasicFeatures(generationSettings)
        DefaultBiomeFeatures.addPlainsTallGrass(generationSettings)
        DefaultBiomeFeatures.addDefaultOres(generationSettings)
        DefaultBiomeFeatures.addClayOre(generationSettings)
        DefaultBiomeFeatures.addDefaultDisks(generationSettings)
        return Biome.Builder()
            .temperature(0.25f)
            .downfall(0.6f)
            .effects(
                BiomeEffects.Builder()
                    .grassColor(0x329270)
                    .foliageColor(0x329270)
                    .waterColor(6254825)
                    .waterFogColor(1836338)
                    .fogColor(0xC196E0)
                    .skyColor(0x774E96)
                    .particleConfig(BiomeParticleConfig(DuskParticles.PURPLE_BIOME_BUBBLE, 0.00025f))
                    .moodSound(BiomeMoodSound.CAVE)
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES))
                    .build()
            ).spawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }
    fun BootstrapContext<Biome>.createQueensGardens(): Biome {
        val spawnSettings = SpawnSettings.Builder()
        val generationSettings = GenerationSettings.Builder(
            this.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
            this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        )
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings)
        OverworldBiomeCreator.addBasicFeatures(generationSettings)
        DefaultBiomeFeatures.addPlainsTallGrass(generationSettings)
        DefaultBiomeFeatures.addDefaultOres(generationSettings)
        DefaultBiomeFeatures.addClayOre(generationSettings)
        DefaultBiomeFeatures.addDefaultDisks(generationSettings)
        return Biome.Builder()
            .temperature(0.25f)
            .downfall(0.6f)
            .effects(
                BiomeEffects.Builder()
                    .grassColor(0x329270)
                    .foliageColor(0x329270)
                    .waterColor(6254825)
                    .waterFogColor(1836338)
                    .fogColor(0x1B9E7D)
                    .skyColor(12308479)
//                    .particleConfig(BiomeParticleConfig(DuskParticles.PURPLE_BIOME_BUBBLE, 0.00025f))
                    .moodSound(BiomeMoodSound.CAVE)
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES))
                    .build()
            ).spawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }
}