package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.dimension.DimensionOptions
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDimension {
    val NETHER = create("nether")

    fun create(id: String): RegistryKey<DimensionOptions> =
        RegistryKey.of(RegistryKeys.DIMENSION, id(id))

}
