package org.teamvoided.dusk_debris.item

import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems

object DuskItemLists {
    val GUNPOWDER_BARREL_ITEM_LIST = listOf(
        DuskBlocks.GUNPOWDER_BARREL,
        DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL,
        DuskBlocks.ANCIENT_BLACK_POWDER_BARREL
    )
    val SPAWN_EGGS_ITEM_LIST = listOf(
        DuskItems.GLOOM_SPAWN_EGG
    )
    val OCEAN_METALS_ITEM_LIST = listOf(
        DuskBlocks.TREACHEROUS_GOLD_BLOCK.asItem(),
        DuskItems.TREACHEROUS_GOLD_COINS,
        DuskItems.TREACHEROUS_ASSORTED_GOLD_COINS,
        DuskBlocks.GILDED_CHALICE.asItem(),
        DuskBlocks.GOLDEN_VESSEL.asItem(),
        DuskBlocks.LAPIS_RELIC.asItem(),
        DuskBlocks.GOLDEN_RUBY_CROWN.asItem(),
        DuskBlocks.GOLDEN_SAPPHIRE_CROWN.asItem(),
        DuskBlocks.GOLDEN_QUARTZ_CROWN.asItem(),

        DuskBlocks.TARNISHED_GOLD_BLOCK.asItem(),
        DuskItems.TARNISHED_GOLD_COINS,
        DuskItems.TARNISHED_ASSORTED_GOLD_COINS,
        DuskBlocks.TARNISHED_CHALICE.asItem(),
        DuskBlocks.DROWNED_VESSEL.asItem(),

        DuskBlocks.LOST_SILVER_BLOCK.asItem(),
        DuskItems.LOST_SILVER_COINS,
        DuskItems.LOST_ASSORTED_SILVER_COINS,
        DuskBlocks.SILVERED_CHALICE.asItem(),
        DuskBlocks.PURE_VESSEL.asItem(),

        DuskBlocks.SUNKEN_BRONZE_BLOCK.asItem(),
        DuskItems.SUNKEN_ASSORTED_BRONZE_COINS,
        DuskItems.SUNKEN_BRONZE_COINS,
        DuskBlocks.BRONZED_CHALICE.asItem(),
        DuskBlocks.DARKENED_VESSEL.asItem(),

        DuskBlocks.LEGENDARY_CRYSTAL_CROWN.asItem()
    )
}