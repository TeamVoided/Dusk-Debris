package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.structure.RandomSpreadStructurePlacement
import net.minecraft.structure.RandomSpreadType
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.structure.BuiltInStructureSets
import net.minecraft.world.gen.structure.BuiltInStructures
import net.minecraft.world.gen.structure.StructureSet
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructureSets
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructures
import java.util.*

object StructureSetCreator {
    fun bootstrap(c: BootstrapContext<StructureSet>) {
        val structures: HolderProvider<StructureFeature> = c.getRegistryLookup(RegistryKeys.STRUCTURE_FEATURE)
        val biomes: HolderProvider<Biome> = c.getRegistryLookup(RegistryKeys.BIOME)
        c.register(
            DuskStructureSets.CAVE_FOSSILS,
            StructureSet(
                structures.getHolderOrThrow(DuskStructures.CAVE_FOSSIL),
                RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 20083232)
            )
        )


    }
}