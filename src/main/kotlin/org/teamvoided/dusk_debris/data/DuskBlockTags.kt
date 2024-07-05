package org.teamvoided.dusk_debris.data

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskBlockTags {
    val TEST = create("","")

    val RIBBON_BLOCK = create("ribbon_block")
    val THROWABLE_BOMB_BLOCK = create("throwable_bomb_block")
    val GUNPOWDER_BARRELS = create("gunpowder_barrels")
    val FIREBOMB_DESTROYS = create("firebomb_destroys")
    val BLUNDERBOMB_DESTROYS = create("blunderbomb_destroys")
    val GUNPOWDER_BARREL_DESTROYS = create("gunpowder_barrel_destroys")
    val NETHERSHROOM_GROWABLE_ON = create("nethershroom_growable_on")
    val NETHERSHROOM_REPLACEABLE = create("nethershroom_replaceable")
    val NETHERSHROOM_IGNORE = create("nethershroom_ignore")
    val GUNPOWDER_CONNECTS_TO = create("gunpowder_connects_to")


    fun create(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK,
        DuskDebris.id(modId, path)
    )

}