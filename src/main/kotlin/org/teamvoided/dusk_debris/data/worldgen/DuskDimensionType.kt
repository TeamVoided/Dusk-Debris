package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.dimension.DimensionType
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDimensionType {
    val OVERWORLD = create("minecraft", "overworld")

    private fun create(ns: String, id: String): RegistryKey<DimensionType> =
        RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id(ns, id))

    private fun create(id: String): RegistryKey<DimensionType> =
        RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id(id))

}
