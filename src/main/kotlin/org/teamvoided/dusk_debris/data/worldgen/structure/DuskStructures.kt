package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.structure.Structure
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructures {
    val TEST = create("test")

    val ANCIENT_STRUCTURES = create("ancient_structures")

    fun init() {}

    private fun create(id: String): ResourceKey<Structure> = ResourceKey.create(Registries.STRUCTURE, id(id))
}