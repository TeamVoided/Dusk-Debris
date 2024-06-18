package org.teamvoided.dusk_debris.init

import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.item.BlunderbombItem
import org.teamvoided.dusk_debris.item.FirebombItem


@Suppress("unused", "MemberVisibilityCanBePrivate")
object DuskItems {

    val GUNPOWDER_BARREL = register("gunpowder_barrel", BlockItem(DuskBlocks.GUNPOWDER_BARREL))
    val STRONGHOLD_GUNPOWDER_BARREL = register("stronghold_gunpowder_barrel", BlockItem(DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL, Item.Settings().maxCount(16)))
    val ANCIENT_BLACK_POWDER_BARREL = register("ancient_black_powder_barrel", BlockItem(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL,  Item.Settings().maxCount(1)))
    val BLUNDERBOMB = register("blunderbomb", BlunderbombItem(DuskBlocks.BLUNDERBOMB_BLOCK, Item.Settings().maxCount(16)))
    val FIREBOMB = register("firebomb", FirebombItem(DuskBlocks.FIREBOMB_BLOCK, Item.Settings().maxCount(16)))

    val CYPRESS_LOG = register("cypress_log", BlockItem(DuskBlocks.CYPRESS_LOG))
    val CYPRESS_WOOD = register("cypress_wood", BlockItem(DuskBlocks.CYPRESS_WOOD))
    val STRIPPED_CYPRESS_LOG = register("stripped_cypress_log", BlockItem(DuskBlocks.STRIPPED_CYPRESS_LOG))
    val STRIPPED_CYPRESS_WOOD = register("stripped_cypress_wood", BlockItem(DuskBlocks.STRIPPED_CYPRESS_WOOD))
    val CYPRESS_PLANKS = register("cypress_planks", BlockItem(DuskBlocks.CYPRESS_PLANKS))
    val CYPRESS_STAIRS = register("cypress_stairs", BlockItem(DuskBlocks.CYPRESS_STAIRS))
    val CYPRESS_SLAB = register("cypress_slab", BlockItem(DuskBlocks.CYPRESS_SLAB))
    val CYPRESS_DOOR = register("cypress_door", TallBlockItem(DuskBlocks.CYPRESS_DOOR, Item.Settings()))
    val CYPRESS_TRAPDOOR = register("cypress_trapdoor", BlockItem(DuskBlocks.CYPRESS_TRAPDOOR))
    val CYPRESS_FENCE = register("cypress_fence", BlockItem(DuskBlocks.CYPRESS_FENCE))
    val CYPRESS_FENCE_GATE = register("cypress_fence_gate", BlockItem(DuskBlocks.CYPRESS_FENCE_GATE))
    val CYPRESS_BUTTON = register("cypress_button", BlockItem(DuskBlocks.CYPRESS_BUTTON))
    val CYPRESS_PRESSURE_PLATE = register("cypress_pressure_plate", BlockItem(DuskBlocks.CYPRESS_PRESSURE_PLATE))
    val CYPRESS_SIGN = register("cypress_sign", SignItem((Item.Settings()).maxCount(16), DuskBlocks.CYPRESS_SIGN, DuskBlocks.CYPRESS_WALL_SIGN))
    val CYPRESS_HANGING_SIGN = register("cypress_hanging_sign", HangingSignItem(DuskBlocks.CYPRESS_HANGING_SIGN, DuskBlocks.CYPRESS_WALL_HANGING_SIGN, Item.Settings().maxCount(16)))

    val VOLCANIC_SAND = register("volcanic_sand", BlockItem(DuskBlocks.VOLCANIC_SAND))
    val SUSPICIOUS_VOLCANIC_SAND = register("suspicious_volcanic_sand", BlockItem(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND))
    val VOLCANIC_SANDSTONE = register("volcanic_sandstone", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE))
    val VOLCANIC_SANDSTONE_STAIRS = register("volcanic_sandstone_stairs", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS))
    val VOLCANIC_SANDSTONE_SLAB = register("volcanic_sandstone_slab", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_SLAB))
    val VOLCANIC_SANDSTONE_WALL = register("volcanic_sandstone_wall", BlockItem(DuskBlocks.VOLCANIC_SANDSTONE_WALL))
    val CUT_VOLCANIC_SANDSTONE = register("cut_volcanic_sandstone", BlockItem(DuskBlocks.CUT_VOLCANIC_SANDSTONE))
    val CUT_VOLCANIC_SANDSTONE_SLAB = register("cut_volcanic_sandstone_slab", BlockItem(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB))
    val CHISELED_VOLCANIC_SANDSTONE = register("chiseled_volcanic_sandstone", BlockItem(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE = register("smooth_volcanic_sandstone", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE_STAIRS = register("smooth_volcanic_sandstone_stairs", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS))
    val SMOOTH_VOLCANIC_SANDSTONE_SLAB = register("smooth_volcanic_sandstone_slab", BlockItem(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB))

    val CHARRED_LOG = register("charred_log", BlockItem(DuskBlocks.CHARRED_LOG))
    val CHARRED_WOOD = register("charred_wood", BlockItem(DuskBlocks.CHARRED_WOOD))
    val STRIPPED_CHARRED_LOG = register("stripped_charred_log", BlockItem(DuskBlocks.STRIPPED_CHARRED_LOG))
    val STRIPPED_CHARRED_WOOD = register("stripped_charred_wood", BlockItem(DuskBlocks.STRIPPED_CHARRED_WOOD))
    val CHARRED_PLANKS = register("charred_planks", BlockItem(DuskBlocks.CHARRED_PLANKS))
    val CHARRED_STAIRS = register("charred_stairs", BlockItem(DuskBlocks.CHARRED_STAIRS))
    val CHARRED_SLAB = register("charred_slab", BlockItem(DuskBlocks.CHARRED_SLAB))
    val CHARRED_DOOR = register("charred_door", TallBlockItem(DuskBlocks.CHARRED_DOOR, Item.Settings()))
    val CHARRED_TRAPDOOR = register("charred_trapdoor", BlockItem(DuskBlocks.CHARRED_TRAPDOOR))
    val CHARRED_FENCE = register("charred_fence", BlockItem(DuskBlocks.CHARRED_FENCE))
    val CHARRED_FENCE_GATE = register("charred_fence_gate", BlockItem(DuskBlocks.CHARRED_FENCE_GATE))
    val CHARRED_BUTTON = register("charred_button", BlockItem(DuskBlocks.CHARRED_BUTTON))
    val CHARRED_PRESSURE_PLATE = register("charred_pressure_plate", BlockItem(DuskBlocks.CHARRED_PRESSURE_PLATE))
    val CHARRED_SIGN = register("charred_sign", SignItem((Item.Settings()).maxCount(16), DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN))
    val CHARRED_HANGING_SIGN = register("charred_hanging_sign", HangingSignItem(DuskBlocks.CHARRED_HANGING_SIGN, DuskBlocks.CHARRED_WALL_HANGING_SIGN, Item.Settings().maxCount(16)))


//add void util compat


    fun init() {}

    fun register(id: String, item: Item): Item = Registry.register(Registries.ITEM, id(id), item)

    fun BlockItem(block: Block) = BlockItem(block, Item.Settings())
}