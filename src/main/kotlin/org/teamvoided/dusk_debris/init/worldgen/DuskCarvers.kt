package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.carver.Carver
import net.minecraft.world.gen.carver.CarverConfig
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.world.gen.configured_carver.LakeCarver
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig

object DuskCarvers {

    val LAKE_CARVER = register("lake_carver", LakeCarver(LakeCarverConfig.CODEC))

    fun init() {}
    private fun <C : CarverConfig, F : Carver<C>> register(name: String, carver: F): F =
        Registry.register(Registries.CARVER, id(name), carver)
}