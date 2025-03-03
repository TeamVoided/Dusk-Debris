package org.teamvoided.dusk_debris.init

import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskDensityFunction
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskSurfaceRules
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

object DuskWorldgen {
    fun init() {
        DuskTreeStuff.init()
        DuskBiomes.init()
        DuskCarvers.init()
        DuskFeatures.init()
        DuskDensityFunction.init()
        DuskSurfaceRules.init()
    }
}