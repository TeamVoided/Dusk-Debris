package org.teamvoided.dusk_debris.data

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBlockTags {
    val TEST = create("test")

    fun create(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id(id))
}