package org.teamvoided.dusk_debris.init

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.foliage.FoliagePlacerType
import org.teamvoided.dusk_debris.world.gen.root.CypressRootPlacer
import net.minecraft.world.gen.root.RootPlacer
import net.minecraft.world.gen.root.RootPlacerType
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes
import org.teamvoided.dusk_debris.init.worldgen.DuskCarvers
import org.teamvoided.dusk_debris.init.worldgen.DuskDensityFunction
import org.teamvoided.dusk_debris.init.worldgen.DuskFeatures
import org.teamvoided.dusk_debris.init.worldgen.DuskSurfaceRules
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff
import org.teamvoided.dusk_debris.world.gen.foliage.CypressFoliagePlacer

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