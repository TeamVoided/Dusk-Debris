package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePools
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.structure.StructureSet
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructurePools {
    val NETHER_FOSSILS = create("nether_fossils")

    fun init() {}

    private fun create(id: String): RegistryKey<StructurePool> = RegistryKey.of(RegistryKeys.STRUCTURE_POOL, id(id))
}