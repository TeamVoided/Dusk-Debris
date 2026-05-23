package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskConfiguredCarvers {

    val LAKE = create("lake")
    val LAVA_LAKE = create("lava_lake")
    val AMETHYST_GEODE = create("amethyst_geode")

    private fun create(id: String): ResourceKey<ConfiguredWorldCarver<*>> =
        ResourceKey.create(Registries.CONFIGURED_CARVER, id(id))

}