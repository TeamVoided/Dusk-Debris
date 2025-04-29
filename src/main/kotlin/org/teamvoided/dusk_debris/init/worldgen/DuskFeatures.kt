package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
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

    val SEQUOIA_TREE = register("sequoia_tree", SequoiaTreeFeature(DefaultFeatureConfig.CODEC))

    val ROCK_SPIRE = register("rock_spire", RockFormationFeature(RockFormationFeatureConfig.CODEC))
    val SURFACE_SPIRE = register("surface_spire", SurfaceFormationFeature(SurfaceFormationFeatureConfig.CODEC))

    val NOISE_SURFACE = register("noise_surface", NoiseSurfaceFeature(NoiseSurfaceFeatureConfig.CODEC))

    fun init() {}

    private fun <C : FeatureConfig, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(Registries.FEATURE, id(name), feature)
}