package org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators

import net.minecraft.client.sound.MusicType
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BiomeAdditionsSound
import net.minecraft.sound.BiomeMoodSound
import net.minecraft.sound.SoundEvents
import net.minecraft.world.biome.*
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_DECORATION as ud7
import net.minecraft.world.gen.feature.MiscPlacedFeatures
import net.minecraft.world.gen.feature.NetherPlacedFeatures
import org.teamvoided.dusk_debris.data.gen.world.gen.biome_creators.features.NetherBiomeFeatures

object NetherBiomeCreators {
    fun BootstrapContext<Biome>.createNetherWastes(): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val spawnSettings = SpawnSettings.Builder()
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.GHAST, 50, 4, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 2, 4, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.PIGLIN, 15, 4, 4))
            .spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_LAVA)
        NetherBiomeFeatures.addNetherWastesFeatures(generationSettings)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(3344392)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .loopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_NETHER_WASTES)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createCrimsonForest(ancientFlourish: Boolean = false, forest: Boolean = true): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val spawnSettings = SpawnSettings.Builder()
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.HOGLIN, 9, 3, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.PIGLIN, 5, 3, 4))
            .spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_LAVA)
        NetherBiomeFeatures.addCrimsonFeatures(generationSettings, ancientFlourish, forest)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(3343107)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .particleConfig(BiomeParticleConfig(ParticleTypes.CRIMSON_SPORE, 0.025f))
                    .loopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createWarpedForest(ancientFlourish: Boolean = false, forest: Boolean = true): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val spawnSettings = SpawnSettings.Builder()
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4))
            .spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2))
            .spawnCost(EntityType.ENDERMAN, 1.0, 0.12).build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_LAVA)
        NetherBiomeFeatures.addWarpedFeatures(generationSettings, ancientFlourish, forest)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1705242)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .particleConfig(BiomeParticleConfig(ParticleTypes.WARPED_SPORE, 0.01428f))
                    .loopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_WARPED_FOREST)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createBasaltDeltas(): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val spawnSettings = SpawnSettings.Builder()
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.GHAST, 40, 1, 1))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 100, 2, 5))
            .spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2)).build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        NetherBiomeFeatures.addBasaltDeltaFeatures(generationSettings)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(6840176)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .particleConfig(BiomeParticleConfig(ParticleTypes.WHITE_ASH, 0.118093334f))
                    .loopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_BASALT_DELTAS)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createSoulSandValley(): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val mass = 0.7
        val gravityLimit = 0.15
        val spawnSettings = SpawnSettings.Builder()
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.SKELETON, 20, 5, 5))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.GHAST, 50, 4, 4))
            .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4))
            .spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2))
            .spawnCost(EntityType.SKELETON, mass, gravityLimit)
            .spawnCost(EntityType.GHAST, mass, gravityLimit)
            .spawnCost(EntityType.ENDERMAN, mass, gravityLimit)
            .spawnCost(EntityType.STRIDER, mass, gravityLimit).build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        NetherBiomeFeatures.addNetherCarvers(generationSettings)
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_LAVA)
        NetherBiomeFeatures.addSoulValleyFeatures(generationSettings)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1787717)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .particleConfig(BiomeParticleConfig(ParticleTypes.ASH, 0.00625f))
                    .loopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }

    fun BootstrapContext<Biome>.createNetherTest(): Biome {
        val features = this.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val carvers = this.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER)
        val spawnSettings = SpawnSettings.Builder().build()
        val generationSettings = GenerationSettings.Builder(features, carvers)
        generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_FIRE)
        generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_SOUL_FIRE)
        generationSettings.feature(ud7, NetherPlacedFeatures.GLOWSTONE_EXTRA)
        generationSettings.feature(ud7, NetherPlacedFeatures.GLOWSTONE)
        generationSettings.feature(ud7, NetherPlacedFeatures.PATCH_CRIMSON_ROOTS)
        return Biome.Builder()
            .hasPrecipitation(false)
            .temperature(2.0f)
            .downfall(0.0f)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(1787717)
                    .skyColor(OverworldBiomeCreator.getSkyColor(2.0f))
                    .loopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                    .moodSound(BiomeMoodSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0))
                    .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111))
                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY)).build()
            ).spawnSettings(spawnSettings).generationSettings(generationSettings.build()).build()
    }
}