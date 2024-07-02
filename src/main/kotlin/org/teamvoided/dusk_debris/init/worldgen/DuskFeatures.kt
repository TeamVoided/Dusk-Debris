package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import org.teamvoided.dusk_debris.DuskDebris.id
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig
import net.minecraft.world.gen.feature.HugeRedMushroomFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.HugeNethershroomFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.HugeBlueNethershroomFeature
import org.teamvoided.dusk_debris.world.gen.configured_feature.HugePurpleNethershroomFeature

object DuskFeatures {

    val HUGE_BLUE_NETHERSHROOM = register("huge_blue_nethershroom", HugeBlueNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))
    val HUGE_PURPLE_NETHERSHROOM = register("huge_purple_nethershroom", HugePurpleNethershroomFeature(HugeNethershroomFeatureConfig.CODEC))


    fun init() {}
    private fun <C : FeatureConfig?, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(Registries.FEATURE, id(name), feature)
}