package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.carver.CarverConfiguration
import net.minecraft.world.level.levelgen.carver.WorldCarver
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.configured_carver.GeodeCarver
import org.teamvoided.dusk_debris.world.gen.configured_carver.LakeCarver
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig

object DuskCarvers {

    val LAKE_CARVER = register("lake_carver", LakeCarver(LakeCarverConfig.CODEC))
    val GEODE_CARVER = register("geode_carver", GeodeCarver(GeodeCarverConfig.CODEC))

    fun init() {}
    private fun <C : CarverConfiguration, F : WorldCarver<C>> register(name: String, carver: F): F =
        Registry.register(BuiltInRegistries.CARVER, id(name), carver)
}