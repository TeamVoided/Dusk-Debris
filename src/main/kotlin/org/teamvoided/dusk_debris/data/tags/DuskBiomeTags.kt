package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBiomeTags {
    val TEST = create("test")

    @JvmStatic
    val WORLDNOISE_WATER = create("worldnoise_water")

    //make theese ones generic
    val CRIMSON = create("crimson")
    val WARPED = create("warped")
    val ASHEN = create("ashen")
    val SOUL_VALLEY = create("soul_valley")

    val FOG_EMPTY = create("fog/empty")
    val FOG_HUMID = create("fog/humid")
    val FOG_CREEPY = create("fog/creepy")
    val FOG_BOREAL_VALLEY = create("fog/special/boreal_valley")

    val SNIFFER_BRIGHT = create("sniffer/bright")
    val SNIFFER_SWAMP = create("sniffer/swamp")
    val SNIFFER_MANGROVE_SWAMP = create("sniffer/mangrove_swamp")
    val SNIFFER_BADLANDS = create("sniffer/badlands")
    val SNIFFER_BIRCH = create("sniffer/birch")
    val SNIFFER_COLD = create("sniffer/cold")
    val SNIFFER_WARM = create("sniffer/warm")
    val SNIFFER_CRIMSON = create("sniffer/crimson")
    val SNIFFER_WARPED = create("sniffer/warped")
    val SNIFFER_ASHEN = create("sniffer/ashen")
    val SNIFFER_PINK = create("sniffer/pink")
    val SNIFFER_CHERRY = create("sniffer/cherry")
    val SNIFFER_SNOW = create("sniffer/snow")
    val SNIFFER_FROZEN = create("sniffer/frozen")
    val SNIFFER_DEEP_DARK = create("sniffer/deep_dark")

    fun create(id: String): TagKey<Biome> = TagKey.create(Registries.BIOME, id(id))
}