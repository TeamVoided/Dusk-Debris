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
    val THROWABLE_BLOCK_LIST = listOf(
        DuskBlocks.BLUNDERBOMB_BLOCK,
        DuskBlocks.FIREBOMB_BLOCK,
        DuskBlocks.POCKETPOISON_BLOCK,
        DuskBlocks.BLINDBOMB_BLOCK,
        DuskBlocks.SMOKEBOMB_BLOCK
    )
    val RIBBON_BLOCKS_LIST = listOf(
        DuskBlocks.LIGHT_BLUE_RIBBON
    )
    val PAPER_BLOCKS_LIST = listOf(
        DuskBlocks.PAPER_BLOCK
    )
}