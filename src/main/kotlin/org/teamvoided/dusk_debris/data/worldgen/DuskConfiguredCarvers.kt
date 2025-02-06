package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.carver.ConfiguredCarver
import net.minecraft.world.gen.feature.*
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskConfiguredCarvers {

    val LAKE = create("lake")
    val LAVA_LAKE = create("lava_lake")

    private fun create(id: String): RegistryKey<ConfiguredCarver<*>> =
        RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, id(id))

}