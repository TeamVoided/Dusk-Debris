package org.teamvoided.dusk_debris.data

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBiomeTags {
    val TEST = create("test")

    fun create(id: String): TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, id(id))
}