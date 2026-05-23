package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructureSets
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructures

object StructureSetCreator {
    fun bootstrap(c: BootstrapContext<StructureSet>) {
        val structures: HolderGetter<Structure> = c.lookup(Registries.STRUCTURE)
        val biomes: HolderGetter<Biome> = c.lookup(Registries.BIOME)

        //c.register(
        //    DuskStructureSets.TEST,
        //    DuskStructures.TEST,
        //    6,
        //    3,
        //    1234567890
        //)
        c.register(
            DuskStructureSets.ANCIENT_RUINS,
            DuskStructures.ANCIENT_STRUCTURES,
            4,
            2,
            10006666
        )
    }

    fun BootstrapContext<StructureSet>.register(
        key: ResourceKey<StructureSet>,
        structure: ResourceKey<Structure>,
        spacing: Int,
        seperation: Int,
        salt: Int
    ): Holder.Reference<StructureSet> {
        val structures: HolderGetter<Structure> = this.lookup(Registries.STRUCTURE)
        return this.register(
            key,
            StructureSet(
                structures.getOrThrow(structure),
                RandomSpreadStructurePlacement(
                    spacing,
                    seperation,
                    RandomSpreadType.LINEAR,
                    salt
                )
            )
        )
    }

    fun BootstrapContext<StructureSet>.register(
        key: ResourceKey<StructureSet>,
        vararg structure: Pair<ResourceKey<Structure>, Int>,
        spacing: Int,
        seperation: Int,
        salt: Int
    ): Holder.Reference<StructureSet> {
        val structures: HolderGetter<Structure> = this.lookup(Registries.STRUCTURE)

        val list: List<StructureSet.StructureSelectionEntry> = listOf()
//        structure.forEach {
//            list.addLast(StructureSet.StructureSelectionEntry(structures.getHolderOrThrow(it.first), it.second))
//        }
        return this.register(
            key,
            StructureSet(
                list,
                RandomSpreadStructurePlacement(
                    spacing,
                    seperation,
                    RandomSpreadType.LINEAR,
                    salt
                )
            )
        )
    }
}