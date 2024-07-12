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
    val GUNPOWDER_BARREL_BLOCK_LIST = listOf(
        DuskBlocks.GUNPOWDER_BARREL,
        DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL,
        DuskBlocks.ANCIENT_BLACK_POWDER_BARREL
    )
    val THROWABLE_BOMB_BLOCK_LIST = listOf(
        DuskBlocks.BLUNDERBOMB_BLOCK,
        DuskBlocks.FIREBOMB_BLOCK,
        DuskBlocks.BONECALLER_BLOCK,
        DuskBlocks.BONECHILLER_BLOCK,
        DuskBlocks.BOGCALLER_BLOCK,
        DuskBlocks.BONEWITHER_BLOCK,
        DuskBlocks.SHADECALLER_BLOCK,
        DuskBlocks.POCKETPOISON_BLOCK,
        DuskBlocks.BLINDBOMB_BLOCK,
        DuskBlocks.SMOKEBOMB_BLOCK
    )
    val RIBBON_BLOCKS_LIST = listOf(
        DuskBlocks.LIGHT_BLUE_RIBBON
    )
    val COIN_STACK_BLOCK_LIST = listOf(
        DuskBlocks.TREACHEROUS_GOLD_COIN_STACK,
        DuskBlocks.TARNISHED_GOLD_COIN_STACK,
        DuskBlocks.LOST_SILVER_COIN_STACK,
        DuskBlocks.SUNKEN_BRONZE_COIN_STACK
    )
    val COIN_PILE_BLOCK_LIST = listOf(
        DuskBlocks.TREACHEROUS_GOLD_COIN_PILE,
        DuskBlocks.TARNISHED_GOLD_COIN_PILE,
        DuskBlocks.LOST_SILVER_COIN_PILE,
        DuskBlocks.SUNKEN_BRONZE_COIN_PILE
    )
    val VESSEL_BLOCK_LIST = listOf(
        DuskBlocks.GOLDEN_VESSEL
    )
    val CHALICE_BLOCK_LIST = listOf(
        DuskBlocks.GILDED_CHALICE,
        DuskBlocks.SILVERED_CHALICE
    )
    val RELIC_BLOCK_LIST = listOf(
        DuskBlocks.LAPIS_RELIC
    )
    val CROWN_BLOCK_LIST = listOf(
        DuskBlocks.GOLDEN_RUBY_CROWN,
        DuskBlocks.GOLDEN_SAPPHIRE_CROWN,
        DuskBlocks.GOLDEN_QUARTZ_CROWN
    )

    val DECORATIVE_WHATEVER_BLOCK_LIST =
        COIN_STACK_BLOCK_LIST +
                COIN_PILE_BLOCK_LIST +
                VESSEL_BLOCK_LIST +
                RELIC_BLOCK_LIST +
                CHALICE_BLOCK_LIST +
                CROWN_BLOCK_LIST +
                DuskBlocks.LEGENDARY_CRYSTAL_CROWN
    val PAPER_BLOCKS_LIST = listOf(
        DuskBlocks.PAPER_BLOCK
    )
    val CUTOUT_BLOCKS = listOf(
        DuskBlocks.GUNPOWDER,
        DuskBlocks.BLUE_NETHERSHROOM,
        DuskBlocks.PURPLE_NETHERSHROOM,
        DuskBlocks.CYPRESS_LEAVES,
        DuskBlocks.CYPRESS_DOOR,
        DuskBlocks.CYPRESS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    ) + THROWABLE_BOMB_BLOCK_LIST +
            RIBBON_BLOCKS_LIST +
            DECORATIVE_WHATEVER_BLOCK_LIST
//    val translucentBlock = listOf()
}