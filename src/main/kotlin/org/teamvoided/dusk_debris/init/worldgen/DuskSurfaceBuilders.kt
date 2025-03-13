package org.teamvoided.dusk_debris.init.worldgen

import net.minecraft.registry.tag.BiomeTags
import org.teamvoided.dusk_debris.world.gen.surface_builders.AmethystCave
import org.teamvoided.reef.api.events.CustomSurfaceBuilder

object DuskSurfaceBuilders {


    fun init() {
        CustomSurfaceBuilder.POST_RULES.register { random, defaultBlock, seaLevel, biome, chunk, blockColumn, x, z ->
            AmethystCave.createAmethystCave(random, seaLevel, biome, chunk, blockColumn, x, z, BiomeTags.OVERWORLD)
        }
    }
}
