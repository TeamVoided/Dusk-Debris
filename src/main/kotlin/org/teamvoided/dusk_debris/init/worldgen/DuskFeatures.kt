package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.configured_feature.*
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.GlassSpikeFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig

object DuskFeatures {

    val HUGE_BLUE_NETHERSHROOM =
        register("huge_blue_nethershroom", HugeBlueNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))
    val HUGE_PURPLE_NETHERSHROOM =
        register("huge_purple_nethershroom", HugePurpleNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))
    val GLASS_SPIKE = register("glass_spike", GlassSpikeFeature(GlassSpikeFeatureConfig.CODEC))
    val TORUS = register("torus", TorusFeature(TorusFeatureConfig.CODEC))

    val RANDOM_NOISE_SELECTOR = register("random_noise_selector", RandomNoiseFeature(NoiseFeatureConfig.CODEC))

    fun init() {}
    private fun <C : FeatureConfig?, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(Registries.FEATURE, id(name), feature)
}