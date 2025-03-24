package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.StructureFeature
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructures {
    val TEST = create("test")

    val ANCIENT_STRUCTURES = create("ancient_structures")

    fun init() {}

    private fun create(id: String): RegistryKey<StructureFeature> = RegistryKey.of(RegistryKeys.STRUCTURE_FEATURE, id(id))
}