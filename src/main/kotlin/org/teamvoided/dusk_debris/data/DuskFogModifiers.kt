package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.world.FogModifier

object DuskFogModifiers {
    val DEFAULT = create("default")
    val HUMID = create("humid")
    val CREEPY = create("creepy")


    val BOREAL_VALLEY = create("boreal_valley")

    fun create(path: String): RegistryKey<FogModifier> =
        RegistryKey.of(DuskRegistries.FOG_MODIFIER, DuskDebris.id(path))
}