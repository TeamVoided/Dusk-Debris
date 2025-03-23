package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.structure.StructureSet
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructureSets {
    val TEST = create("test")

    val CAVE_FOSSILS = create("cave_fossils")
    val ANCIENT_RUINS = create("ancient_ruins")

    fun init() {}

    private fun create(id: String): RegistryKey<StructureSet> = RegistryKey.of(RegistryKeys.STRUCTURE_SET, id(id))
}