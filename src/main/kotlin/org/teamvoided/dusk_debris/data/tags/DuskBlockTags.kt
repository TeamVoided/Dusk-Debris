package org.teamvoided.dusk_debris.data.tags

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskBlockTags {
    val TEST = create("", "")

    val RIBBON_BLOCK = create("ribbon_block")
    val THROWABLE_BOMB_BLOCK = create("throwable_bomb_block")
    val GUNPOWDER_BARRELS = create("gunpowder_barrels")
    val FIREBOMB_DESTROYS = create("firebomb_destroys")
    val BLUNDERBOMB_DESTROYS = create("blunderbomb_destroys")
    val GEYSER_PERSISTANT = create("geyser_persistant")
    val GUNPOWDER_BARREL_DESTROYS = create("gunpowder_barrel_destroys")
    val NETHERSHROOM_GROWABLE_ON = create("nethershroom_growable_on")
    val NETHERSHROOM_REPLACEABLE = create("nethershroom_replaceable")
    val NETHERSHROOM_IGNORE = create("nethershroom_ignore")
    val GUNPOWDER_CONNECTS_TO = create("gunpowder_connects_to")

    val MIDAS_DEEPSLATE_GOLD_ORE = create("enchantment/curse/deepslate_gold_ore")
    val MIDAS_GOLD_ORE = create("enchantment/curse/gold_ore")
    val MIDAS_NETHER_GOLD_ORE = create("enchantment/curse/nether_gold_ore")
    val MIDAS_RAW_GOLD_BLOCK = create("enchantment/curse/raw_gold_block")
    val MIDAS_GOLD_BLOCK = create("enchantment/curse/gold_block")
    val MIDAS_GOLD_PRESSURE_PLATE = create("enchantment/curse/gold_pressure_plate")
    val MIDAS_GILDED_BLACKSTONE = create("enchantment/curse/gilded_blackstone")

    val CONVENTIONAL_DEEPSLATE_ORES = create("c", "deepslate_ores")
    val CONVENTIONAL_STONE_ORES = create("c", "stone_ores")
    val CONVENTIONAL_NETHERRACK_ORES = create("c", "netherrack_ores")
    val CONVENTIONAL_RAW_ORE_BLOCKS = create("c", "raw_ore_blocks")


    fun create(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Block> = TagKey.of(
        RegistryKeys.BLOCK,
        DuskDebris.id(modId, path)
    )

}