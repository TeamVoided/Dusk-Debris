package org.teamvoided.dusk_debris.data.worldgen.structure

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskStructurePools {
    val NETHER_FOSSILS = create("nether_fossils")
    val TEST = create("test")
    val TEST_UP = create("test_up")
    val TEST_UP_FALL = create("test_up_fall")

    fun init() {}

    private fun create(id: String): ResourceKey<StructureTemplatePool> = ResourceKey.create(Registries.TEMPLATE_POOL, id(id))
}