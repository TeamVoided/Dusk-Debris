package org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.biome.OverworldBiomes
import net.minecraft.sounds.Musics
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.*
import org.teamvoided.dusk_debris.init.DuskParticles

object HollowKnightBiomeCreators {
    fun BootstrapContext<Biome>.createFogCanyon(): Biome {
        val spawnSettings = MobSpawnSettings.Builder()
        val generationSettings = BiomeGenerationSettings.Builder(
            this.lookup(Registries.PLACED_FEATURE),
            this.lookup(Registries.CONFIGURED_CARVER)
        )
        spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8))
        BiomeDefaultFeatures.commonSpawns(spawnSettings)
        OverworldBiomes.globalOverworldGeneration(generationSettings)
        BiomeDefaultFeatures.addPlainGrass(generationSettings)
        BiomeDefaultFeatures.addDefaultOres(generationSettings)
        BiomeDefaultFeatures.addLushCavesSpecialOres(generationSettings)
        BiomeDefaultFeatures.addDefaultSoftDisks(generationSettings)
        return Biome.BiomeBuilder()
            .temperature(0.25f)
            .downfall(0.6f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .grassColorOverride(0x329270)
                    .foliageColorOverride(0x329270)
                    .waterColor(6254825)
                    .waterFogColor(1836338)
                    .fogColor(0xC196E0)
                    .skyColor(0x774E96)
                    .ambientParticle(AmbientParticleSettings(DuskParticles.PURPLE_BIOME_BUBBLE, 0.00025f))
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_LUSH_CAVES))
                    .build()
            ).mobSpawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }
    fun BootstrapContext<Biome>.createQueensGardens(): Biome {
        val spawnSettings = MobSpawnSettings.Builder()
        val generationSettings = BiomeGenerationSettings.Builder(
            this.lookup(Registries.PLACED_FEATURE),
            this.lookup(Registries.CONFIGURED_CARVER)
        )
        BiomeDefaultFeatures.commonSpawns(spawnSettings)
        OverworldBiomes.globalOverworldGeneration(generationSettings)
        BiomeDefaultFeatures.addPlainGrass(generationSettings)
        BiomeDefaultFeatures.addDefaultOres(generationSettings)
        BiomeDefaultFeatures.addLushCavesSpecialOres(generationSettings)
        BiomeDefaultFeatures.addDefaultSoftDisks(generationSettings)
        return Biome.BiomeBuilder()
            .temperature(0.25f)
            .downfall(0.6f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .grassColorOverride(0x329270)
                    .foliageColorOverride(0x329270)
                    .waterColor(6254825)
                    .waterFogColor(1836338)
                    .fogColor(0x1B9E7D)
                    .skyColor(12308479)
//                    .particleConfig(BiomeParticleConfig(DuskParticles.PURPLE_BIOME_BUBBLE, 0.00025f))
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_LUSH_CAVES))
                    .build()
            ).mobSpawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build()
    }
}