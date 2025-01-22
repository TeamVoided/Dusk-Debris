package org.teamvoided.dusk_debris.data.variants

import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant
import org.teamvoided.dusk_debris.init.DuskRegistries

object DuskSnifferVariants {
    val DEFAULT = create("default")
    val BRIGHT = create("bright")                    //jungles and mushroom island
    val SWAMP = create("swamp")                      //swamp
    val MANGROVE_SWAMP = create("mangrove_swamp")    //mangrove_swamp
    val BADLANDS = create("badlands")                //badlands
    val BIRCH = create("birch")                      //birch forests
    val COLD = create("cold")                        //taigas & hills
    val WARM = create("warm")                        //savanna, desert, nether waste
    val CRIMSON = create("crimson")                  //crimson forest
    val WARPED = create("warped")                    //warped forest
    val ASHEN = create("ashen")                      //basalt delta
    val PINK = create("pink")                        //cherry grove? (pink)
    val CHERRY = create("cherry")                    //cherry grove? (biome tint)
    val SNOW = create("snow")                        //snowy? (white)
    val FROZEN = create("frozen")                    //snowy? (biome tint)
    val DEEP_DARK = create("deep_dark")              //deep dark

    fun create(path: String): RegistryKey<SnifferVariant> = RegistryKey.of(DuskRegistries.SNIFFER_VARIANT, id(path))
}