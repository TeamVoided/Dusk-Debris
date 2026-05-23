package org.teamvoided.dusk_debris.block

import net.minecraft.data.BlockFamilies
import net.minecraft.data.BlockFamily
import org.teamvoided.dusk_debris.init.DuskBlocks

object DuskBlockFamilies {
    val CUT_BRONZE_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.CUT_BRONZE)
            .stairs(DuskBlocks.CUT_BRONZE_STAIRS)
            .slab(DuskBlocks.CUT_BRONZE_SLAB)
            .family

    val VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.VOLCANIC_SANDSTONE_SLAB)
            .wall(DuskBlocks.VOLCANIC_SANDSTONE_WALL)
            .chiseled(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE)
            .cut(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .dontGenerateRecipe()
            .family
    val CUT_VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .slab(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB)
            .family
    val SMOOTH_VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB)
            .family

    val CYPRESS_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.CYPRESS_PLANKS)
            .stairs(DuskBlocks.CYPRESS_STAIRS)
            .slab(DuskBlocks.CYPRESS_SLAB)
            .fence(DuskBlocks.CYPRESS_FENCE)
            .door(DuskBlocks.CYPRESS_DOOR)
            .trapdoor(DuskBlocks.CYPRESS_TRAPDOOR)
            .fenceGate(DuskBlocks.CYPRESS_FENCE_GATE)
            .button(DuskBlocks.CYPRESS_BUTTON)
            .pressurePlate(DuskBlocks.CYPRESS_PRESSURE_PLATE)
            .sign(DuskBlocks.CYPRESS_SIGN, DuskBlocks.CYPRESS_WALL_SIGN)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .family

    val SEQUOIA_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.SEQUOIA_PLANKS)
            .stairs(DuskBlocks.SEQUOIA_STAIRS)
            .slab(DuskBlocks.SEQUOIA_SLAB)
            .fence(DuskBlocks.SEQUOIA_FENCE)
            .door(DuskBlocks.SEQUOIA_DOOR)
            .trapdoor(DuskBlocks.SEQUOIA_TRAPDOOR)
            .fenceGate(DuskBlocks.SEQUOIA_FENCE_GATE)
            .button(DuskBlocks.SEQUOIA_BUTTON)
            .pressurePlate(DuskBlocks.SEQUOIA_PRESSURE_PLATE)
            .sign(DuskBlocks.SEQUOIA_SIGN, DuskBlocks.SEQUOIA_WALL_SIGN)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .family

    val CHARRED_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.CHARRED_PLANKS)
            .stairs(DuskBlocks.CHARRED_STAIRS)
            .slab(DuskBlocks.CHARRED_SLAB)
            .fence(DuskBlocks.CHARRED_FENCE)
            .door(DuskBlocks.CHARRED_DOOR)
            .trapdoor(DuskBlocks.CHARRED_TRAPDOOR)
            .fenceGate(DuskBlocks.CHARRED_FENCE_GATE)
            .button(DuskBlocks.CHARRED_BUTTON)
            .pressurePlate(DuskBlocks.CHARRED_PRESSURE_PLATE)
            .sign(DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .family

    private val GALLERY_MAPLE_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.GALLERY_MAPLE_PLANKS)
            .stairs(DuskBlocks.GALLERY_MAPLE_STAIRS)
            .slab(DuskBlocks.GALLERY_MAPLE_SLAB)
            .fence(DuskBlocks.GALLERY_MAPLE_FENCE)
            .fenceGate(DuskBlocks.GALLERY_MAPLE_FENCE_GATE)
            .door(DuskBlocks.GALLERY_MAPLE_DOOR)
            .trapdoor(DuskBlocks.GALLERY_MAPLE_TRAPDOOR)
            .button(DuskBlocks.GALLERY_MAPLE_BUTTON)
            .pressurePlate(DuskBlocks.GALLERY_MAPLE_PRESSURE_PLATE)
            .sign(DuskBlocks.GALLERY_MAPLE_SIGN, DuskBlocks.GALLERY_MAPLE_WALL_SIGN)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_gallery_planks")
            .family
    private val BONEWOOD_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.BONEWOOD_PLANKS)
            .stairs(DuskBlocks.BONEWOOD_STAIRS)
            .slab(DuskBlocks.BONEWOOD_SLAB)
            .fence(DuskBlocks.BONEWOOD_FENCE)
            .fenceGate(DuskBlocks.BONEWOOD_FENCE_GATE)
            .door(DuskBlocks.BONEWOOD_DOOR)
            .trapdoor(DuskBlocks.BONEWOOD_TRAPDOOR)
//            .button(DnDWoodBlocks.BONEWOOD_BUTTON)
//            .pressurePlate(DnDWoodBlocks.BONEWOOD_PRESSURE_PLATE)
//            .sign(DnDWoodBlocks.BONEWOOD_SIGN, DnDWoodBlocks.BONEWOOD_WALL_SIGN)
            .recipeGroupPrefix("bonewood")
            .recipeUnlockedBy("has_bonewood")
            .family
    private val WITHERING_BONEWOOD_FAMILY: BlockFamily =
        BlockFamilies.familyBuilder(DuskBlocks.WITHERING_BONEWOOD_PLANKS)
            .stairs(DuskBlocks.WITHERING_BONEWOOD_STAIRS)
            .slab(DuskBlocks.WITHERING_BONEWOOD_SLAB)
            .fence(DuskBlocks.WITHERING_BONEWOOD_FENCE)
            .fenceGate(DuskBlocks.WITHERING_BONEWOOD_FENCE_GATE)
            .door(DuskBlocks.WITHERING_BONEWOOD_DOOR)
            .trapdoor(DuskBlocks.WITHERING_BONEWOOD_TRAPDOOR)
//            .button(DnDWoodBlocks.WITHERING_BONEWOOD_BUTTON)
//            .pressurePlate(DnDWoodBlocks.WITHERING_BONEWOOD_PRESSURE_PLATE)
//            .sign(DnDWoodBlocks.WITHERING_BONEWOOD_SIGN, DnDWoodBlocks.WITHERING_BONEWOOD_WALL_SIGN)
            .recipeGroupPrefix("bonewood")
            .recipeUnlockedBy("has_bonewood")
            .family

    val blockFamilies = listOf(
        CUT_BRONZE_FAMILY,
        VOLCANIC_SANDSTONE_FAMILY,
        CUT_VOLCANIC_SANDSTONE_FAMILY,
        SMOOTH_VOLCANIC_SANDSTONE_FAMILY,
        CYPRESS_FAMILY,
        SEQUOIA_FAMILY,
        CHARRED_FAMILY
    )

    fun init() {}
}