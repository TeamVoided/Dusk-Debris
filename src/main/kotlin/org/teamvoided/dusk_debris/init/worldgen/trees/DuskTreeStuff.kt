package org.teamvoided.dusk_debris.init.worldgen.trees

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.world.gen.tree.decorator.AttachedToTrunkTreeDecorator
import org.teamvoided.dusk_debris.world.gen.tree.foliage.BirchFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.foliage.CypressFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.foliage.OakFoliagePlacer
import org.teamvoided.dusk_debris.world.gen.tree.root.CypressRootPlacer

object DuskTreeStuff {

    val OAK_FOLIAGE_PLACER = registerFoliagePlacer("oak_foliage_placer", OakFoliagePlacer.CODEC)
    val BIRCH_FOLIAGE_PLACER = registerFoliagePlacer("birch_foliage_placer", BirchFoliagePlacer.CODEC)
    val ATTACHED_TO_TRUNK_DECORATOR = registerDecorator("attached_to_trunk", AttachedToTrunkTreeDecorator.CODEC)

    val CYPRESS_FOLIAGE_PLACER = registerFoliagePlacer("cypress_foliage_placer", CypressFoliagePlacer.CODEC)
    val CYPRESS_ROOT_PLACER = registerRootPlacer("cypress_root_placer", CypressRootPlacer.CODEC)

    fun init() {}

    private fun <P : TrunkPlacer> registerTrunkPlacer(id: String, codec: MapCodec<P>): TrunkPlacerType<P> {
        return Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, DuskDebris.id(id), TrunkPlacerType(codec))
    }

    private fun <P : FoliagePlacer> registerFoliagePlacer(id: String, codec: MapCodec<P>): FoliagePlacerType<P> {
        return Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, DuskDebris.id(id), FoliagePlacerType(codec))
    }

    private fun <P : RootPlacer> registerRootPlacer(id: String, codec: MapCodec<P>): RootPlacerType<P> {
        return Registry.register(BuiltInRegistries.ROOT_PLACER_TYPE, DuskDebris.id(id), RootPlacerType(codec))
    }

    private fun <P : TreeDecorator> registerDecorator(id: String, codec: MapCodec<P>): TreeDecoratorType<P> {
        return Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, DuskDebris.id(id), TreeDecoratorType(codec))
    }
}