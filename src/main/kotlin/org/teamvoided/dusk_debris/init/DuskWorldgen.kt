package org.teamvoided.dusk_debris.init

import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes
import org.teamvoided.dusk_debris.init.worldgen.*
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePieceType
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePoolElementType
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructureType
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff

object DuskWorldgen {
    fun init() {
        DuskTreeStuff.init()
        DuskBiomes.init()
        DuskCarvers.init()
        DuskFeatures.init()
        DuskStructureType.init()
        DuskDensityFunction.init()
        DuskSurfaceRules.init()
        DuskStructurePieceType.init()
        DuskStructurePoolElementType.init()
    }
}