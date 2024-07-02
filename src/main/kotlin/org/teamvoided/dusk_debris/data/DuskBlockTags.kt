package org.teamvoided.dusk_debris.data

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBlockTags {
    val TEST = create("test")
    val THROWABLE_BLOCK = create("throwable_block")
    val FIREBOMB_DESTROYS = create("firebomb_destroys")
    val BLUNDERBOMB_DESTROYS = create("blunderbomb_destroys")
    val GUNPOWDER_BARREL_DESTROYS = create("gunpowder_barrel_destroys")
    val NETHERSHROOM_PLACEABLE_ON = create("nethershroom_placeable_on")
    val NETHERSHROOM_REPLACEABLE = create("nethershroom_replaceable")
    val NETHERSHROOM_IGNORE = create("nethershroom_ignore")

    fun create(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id(id))
}