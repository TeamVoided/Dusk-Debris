package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.LevelStem
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskDimension {
    val NETHER = create("nether")
    val OVERWORLD = create("overworld")

    private fun create(id: String): ResourceKey<LevelStem> =
        ResourceKey.create(Registries.LEVEL_STEM, id(id))

}
