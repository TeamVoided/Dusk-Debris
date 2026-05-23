package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.configured_feature.*
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.*
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires.RockFormationFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires.SurfaceFormationFeature

object DuskFeatures {

    val HUGE_BLUE_NETHERSHROOM =
        register("huge_blue_nethershroom", HugeBlueNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))
    val HUGE_PURPLE_NETHERSHROOM =
        register("huge_purple_nethershroom", HugePurpleNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))
    val GLASS_SPIKE = register("glass_spike", GlassSpikeFeature(GlassSpikeFeatureConfig.CODEC))
    val TORUS = register("torus", TorusFeature(TorusFeatureConfig.CODEC))

    val RANDOM_NOISE_SELECTOR = register("random_noise_selector", RandomNoiseFeature(NoiseFeatureConfig.CODEC))

    val SEQUOIA_TREE = register("sequoia_tree", SequoiaTreeFeature(NoneFeatureConfiguration.CODEC))

    val ROCK_SPIRE = register("rock_spire", RockFormationFeature(RockFormationFeatureConfig.CODEC))
    val SURFACE_SPIRE = register("surface_spire", SurfaceFormationFeature(SurfaceFormationFeatureConfig.CODEC))

    val NOISE_SURFACE = register("noise_surface", NoiseSurfaceFeature(NoiseSurfaceFeatureConfig.CODEC))

    val ROCK = register("rock", RockFeature(NoneFeatureConfiguration.CODEC))
    val HUGE_GOLDEN_MUSHROOM = register("huge_golden_mushroom", HugeGoldMushroomFeature(MushroomFeatureConfig.CODEC))

    fun init() {}

    private fun <C : FeatureConfiguration, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(BuiltInRegistries.FEATURE, id(name), feature)
}