package org.teamvoided.dusk_debris.block

import org.teamvoided.dusk_debris.init.DuskBlocks

object DuskBlockLists {
    val blockFamily = listOf(
        DuskBlockFamilies.VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.CUT_VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.SMOOTH_VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.CYPRESS_FAMILY,
        DuskBlockFamilies.CHARRED_FAMILY
    )
    val THROWABLE_BOMB_BLOCK_LIST = listOf(
        DuskBlocks.BLUNDERBOMB_BLOCK,
        DuskBlocks.FIREBOMB_BLOCK,
        DuskBlocks.BONECALLER_BLOCK,
        DuskBlocks.BONECHILLER_BLOCK,
        DuskBlocks.BONEBOGGER_BLOCK,
        DuskBlocks.BONEWITHER_BLOCK,
        DuskBlocks.POCKETPOISON_BLOCK,
        DuskBlocks.BLINDBOMB_BLOCK,
        DuskBlocks.SMOKEBOMB_BLOCK
    )
    val GUNPOWDER_BARREL_BLOCK_LIST = listOf(
        DuskBlocks.GUNPOWDER_BARREL,
        DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL,
        DuskBlocks.ANCIENT_BLACK_POWDER_BARREL
    )
    val RIBBON_BLOCKS_LIST = listOf(
        DuskBlocks.LIGHT_BLUE_RIBBON
    )
    val VESSEL_BLOCK_LIST = listOf(
        DuskBlocks.GILDED_VESSEL
    )
//    val CHALICE_BLOCK_LIST = listOf()
    val DECORATIVE_WHATEVER_BLOCK_LIST = VESSEL_BLOCK_LIST
    val PAPER_BLOCKS_LIST = listOf(
        DuskBlocks.PAPER_BLOCK
    )
}