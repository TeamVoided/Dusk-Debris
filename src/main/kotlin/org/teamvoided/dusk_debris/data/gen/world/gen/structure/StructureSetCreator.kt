package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import net.minecraft.registry.*
import net.minecraft.structure.RandomSpreadStructurePlacement
import net.minecraft.structure.RandomSpreadType
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.structure.StructureSet
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructureSets
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructures

object StructureSetCreator {
    fun bootstrap(c: BootstrapContext<StructureSet>) {
        val structures: HolderProvider<StructureFeature> = c.getRegistryLookup(RegistryKeys.STRUCTURE_FEATURE)
        val biomes: HolderProvider<Biome> = c.getRegistryLookup(RegistryKeys.BIOME)
        c.register(
            DuskStructureSets.CAVE_FOSSILS,
            DuskStructures.CAVE_FOSSIL,
            2,
            1,
            10091212
        )
        c.register(
            DuskStructureSets.ANCIENT_RUINS,
            DuskStructures.ANCIENT_STRUCTURES,
            4,
            2,
            10006666
        )
    }

    fun BootstrapContext<StructureSet>.register(
        key: RegistryKey<StructureSet>,
        structure: RegistryKey<StructureFeature>,
        spacing: Int,
        seperation: Int,
        salt: Int
    ): Holder.Reference<StructureSet> {
        val structures: HolderProvider<StructureFeature> = this.getRegistryLookup(RegistryKeys.STRUCTURE_FEATURE)
        return this.register(
            key,
            StructureSet(
                structures.getHolderOrThrow(structure),
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
        key: RegistryKey<StructureSet>,
        vararg structure: Pair<RegistryKey<StructureFeature>, Int>,
        spacing: Int,
        seperation: Int,
        salt: Int
    ): Holder.Reference<StructureSet> {
        val structures: HolderProvider<StructureFeature> = this.getRegistryLookup(RegistryKeys.STRUCTURE_FEATURE)

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