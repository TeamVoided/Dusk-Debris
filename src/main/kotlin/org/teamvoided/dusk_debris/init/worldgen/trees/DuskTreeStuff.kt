package org.teamvoided.dusk_debris.init.worldgen.trees

import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.foliage.FoliagePlacerType
import net.minecraft.world.gen.root.RootPlacer
import net.minecraft.world.gen.root.RootPlacerType
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.root.CypressRootPlacer

object DuskTreeStuff {

    val CYPRESS_FOLIAGE_PLACER = registerFoliagePlacer("cypress_foliage_placer", CypressFoliagePlacer.CODEC)
    val CYPRESS_ROOT_PLACER = registerRootPlacer("cypress_root_placer", CypressRootPlacer.CODEC)

    fun init() {}

    private fun <P : TrunkPlacer> registerTrunkPlacer(id: String, codec: MapCodec<P>): TrunkPlacerType<P> {
        return Registry.register(Registries.TRUNK_PLACER_TYPE, DuskDebris.id(id), TrunkPlacerType(codec))
    }

    private fun <P : FoliagePlacer> registerFoliagePlacer(id: String, codec: MapCodec<P>): FoliagePlacerType<P> {
        return Registry.register(Registries.FOLIAGE_PLACER_TYPE, DuskDebris.id(id), FoliagePlacerType(codec))
    }

    private fun <P : RootPlacer> registerRootPlacer(id: String, codec: MapCodec<P>): RootPlacerType<P> {
        return Registry.register(Registries.ROOT_PLACER_TYPE, DuskDebris.id(id), RootPlacerType(codec))
    }
}