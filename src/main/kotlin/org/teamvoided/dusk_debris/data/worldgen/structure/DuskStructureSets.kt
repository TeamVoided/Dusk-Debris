package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.structure.StructureSet
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructureSets {
    val TEST = create("test")

    val CAVE_FOSSILS = create("cave_fossils")
    val ANCIENT_RUINS = create("ancient_ruins")

    fun init() {}

    private fun create(id: String): ResourceKey<StructureSet> = ResourceKey.create(Registries.STRUCTURE_SET, id(id))
}