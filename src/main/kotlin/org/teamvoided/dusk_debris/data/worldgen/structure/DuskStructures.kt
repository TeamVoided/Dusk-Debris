package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.structure.Structure
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.structure.StructureSet
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructures {
    val TEST = create("test")

    val CAVE_FOSSIL = create("cave_fossil")

    fun init() {}

    private fun create(id: String): RegistryKey<StructureFeature> = RegistryKey.of(RegistryKeys.STRUCTURE_FEATURE, id(id))
}