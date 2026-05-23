package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDimensionType {
    val OVERWORLD = create("minecraft", "overworld")

    private fun create(ns: String, id: String): ResourceKey<DimensionType> =
        ResourceKey.create(Registries.DIMENSION_TYPE, id(ns, id))

    private fun create(id: String): ResourceKey<DimensionType> =
        ResourceKey.create(Registries.DIMENSION_TYPE, id(id))

}
