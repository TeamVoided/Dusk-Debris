package org.teamvoided.dusk_debris.data

import net.minecraft.resources.ResourceKey
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.init.DuskRegistryKeys
import org.teamvoided.dusk_debris.world.FogModifier

object DuskFogModifiers {
    val DEFAULT = create("default")
    val HUMID = create("humid")
    val CREEPY = create("creepy")


    val BOREAL_VALLEY = create("boreal_valley")

    private fun create(path: String): ResourceKey<FogModifier> =
        ResourceKey.create(DuskRegistryKeys.FOG_MODIFIER, DuskDebris.id(path))
}