package org.teamvoided.dusk_debris.init

import org.teamvoided.dusk_debris.init.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures

object DuskWorldgen {
    fun init() {
//        DuskBiomes.init()
        DuskConfiguredFeatures.init()
//        DuskPlacedFeatures.init()
        DuskFeatures.init()
    }
}
