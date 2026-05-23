package org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.biome.OverworldBiomes
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements
import net.minecraft.data.worldgen.placement.NetherPlacements
import net.minecraft.sounds.Musics
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.levelgen.GenerationStep
import org.teamvoided.dusk_debris.data.gen.world.gen.biome.creators.features.NetherBiomeFeatures
import org.teamvoided.dusk_debris.data.worldgen.DuskPlacedFeatures
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.LOCAL_MODIFICATIONS as lm2
import net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_DECORATION as ud7

object NetherBiomeCreators {
    fun BootstrapContext<Biome>.createNetherWastes(): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val spawnSettings = MobSpawnSettings.Builder()
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 4, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 15, 4, 4))
            .addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
        NetherBiomeFeatures.addNetherWastesFeatures(generationSettings)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(3344392)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createCrimsonForest(ancientFlourish: Boolean = false, forest: Boolean = true): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val spawnSettings = MobSpawnSettings.Builder()
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 9, 3, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 5, 3, 4))
            .addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
        NetherBiomeFeatures.addCrimsonFeatures(generationSettings, ancientFlourish, forest)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(3343107)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientParticle(AmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, 0.025f))
                    .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createWarpedForest(ancientFlourish: Boolean = false, forest: Boolean = true): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val spawnSettings = MobSpawnSettings.Builder()
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4))
            .addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
            .addMobCharge(EntityType.ENDERMAN, 1.0, 0.12).build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
        NetherBiomeFeatures.addWarpedFeatures(generationSettings, ancientFlourish, forest)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1705242)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientParticle(AmbientParticleSettings(ParticleTypes.WARPED_SPORE, 0.01428f))
                    .ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createBasaltDeltas(): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val spawnSettings = MobSpawnSettings.Builder()
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.GHAST, 40, 1, 1))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 100, 2, 5))
            .addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        NetherBiomeFeatures.addBasaltDeltaFeatures(generationSettings)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(6840176)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientParticle(AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.118093334f))
                    .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createSoulSandValley(): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val mass = 0.7
        val gravityLimit = 0.15
        val spawnSettings = MobSpawnSettings.Builder()
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 5, 5))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4))
            .addSpawn(MobCategory.MONSTER, MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4))
            .addSpawn(MobCategory.CREATURE, MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
            .addMobCharge(EntityType.SKELETON, mass, gravityLimit)
            .addMobCharge(EntityType.GHAST, mass, gravityLimit)
            .addMobCharge(EntityType.ENDERMAN, mass, gravityLimit)
            .addMobCharge(EntityType.STRIDER, mass, gravityLimit).build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
        NetherBiomeFeatures.addSoulValleyFeatures(generationSettings)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1787717)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientParticle(AmbientParticleSettings(ParticleTypes.ASH, 0.00625f))
                    .ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createNetherTest(): Biome {
        val features = this.lookup(Registries.PLACED_FEATURE)
        val carvers = this.lookup(Registries.CONFIGURED_CARVER)
        val spawnSettings = MobSpawnSettings.Builder().build()
        val generationSettings = BiomeGenerationSettings.Builder(features, carvers)
        generationSettings.addFeature(ud7, NetherPlacements.PATCH_FIRE)
        generationSettings.addFeature(ud7, NetherPlacements.PATCH_SOUL_FIRE)
        generationSettings.addFeature(ud7, NetherPlacements.GLOWSTONE_EXTRA)
        generationSettings.addFeature(ud7, NetherPlacements.GLOWSTONE)
        generationSettings.addFeature(ud7, NetherPlacements.PATCH_CRIMSON_ROOTS)
        generationSettings.addFeature(lm2, DuskPlacedFeatures.BLACKSTONE_STRIPS)
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .specialEffects(
                BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1787717)
                    .skyColor(OverworldBiomes.calculateSkyColor(2.0f))
                    .ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                    .ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0))
                    .ambientAdditionsSound(AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111))
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build()
            ).mobSpawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }
}