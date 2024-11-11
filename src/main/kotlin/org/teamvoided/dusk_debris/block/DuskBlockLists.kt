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
    val VESSEL_BLOCK_LIST = listOf(
        DuskBlocks.GOLDEN_VESSEL,
        DuskBlocks.DROWNED_VESSEL,
        DuskBlocks.PURE_VESSEL,
        DuskBlocks.DARKENED_VESSEL
    )
    val CHALICE_BLOCK_LIST = listOf(
        DuskBlocks.GILDED_CHALICE,
        DuskBlocks.TARNISHED_CHALICE,
        DuskBlocks.SILVERED_CHALICE,
        DuskBlocks.BRONZED_CHALICE
    )
    val RELIC_BLOCK_LIST = listOf(
        DuskBlocks.LAPIS_RELIC
    )
    val CROWN_BLOCK_LIST = listOf(
        DuskBlocks.GOLDEN_RUBY_CROWN,
        DuskBlocks.GOLDEN_SAPPHIRE_CROWN,
        DuskBlocks.GOLDEN_QUARTZ_CROWN
    )
    val PAPER_BLOCKS_LIST = listOf(
        DuskBlocks.PAPER_BLOCK
    )

    val copperFans = listOf(
        (DuskBlocks.COPPER_FAN to DuskBlocks.WAXED_COPPER_FAN),
        (DuskBlocks.EXPOSED_COPPER_FAN to DuskBlocks.WAXED_EXPOSED_COPPER_FAN),
        (DuskBlocks.WEATHERED_COPPER_FAN to DuskBlocks.WAXED_WEATHERED_COPPER_FAN),
        (DuskBlocks.OXIDIZED_COPPER_FAN to DuskBlocks.WAXED_OXIDIZED_COPPER_FAN)
    )
}