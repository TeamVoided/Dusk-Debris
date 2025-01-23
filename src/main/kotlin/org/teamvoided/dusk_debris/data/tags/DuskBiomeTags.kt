package org.teamvoided.dusk_debris.data.tags

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
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

    val FOG_START_0 = create("fog/start/0")
    val FOG_START_20 = create("fog/start/20")
    val FOG_START_50 = create("fog/start/50")
    val FOG_START_80 = create("fog/start/80")

    val FOG_END_0 = create("fog/end/0")
    val FOG_END_20 = create("fog/end/20")
    val FOG_END_50 = create("fog/end/50")
    val FOG_END_80 = create("fog/end/80")

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

    fun create(id: String): TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, id(id))
}