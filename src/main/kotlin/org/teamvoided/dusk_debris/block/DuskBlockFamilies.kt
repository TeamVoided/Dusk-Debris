package org.teamvoided.dusk_debris.block

import net.minecraft.data.family.BlockFamilies
import net.minecraft.data.family.BlockFamily
import org.teamvoided.dusk_debris.init.DuskBlocks

object DuskBlockFamilies {
    val CUT_BRONZE_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.CUT_BRONZE)
            .stairs(DuskBlocks.CUT_BRONZE_STAIRS)
            .slab(DuskBlocks.CUT_BRONZE_SLAB)
            .build()

    val VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.VOLCANIC_SANDSTONE_SLAB)
            .wall(DuskBlocks.VOLCANIC_SANDSTONE_WALL)
            .chiseled(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE)
            .cut(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .noGenerateRecipes()
            .build()
    val CUT_VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .slab(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB)
            .build()
    val SMOOTH_VOLCANIC_SANDSTONE_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB)
            .build()

    val CYPRESS_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.CYPRESS_PLANKS)
            .stairs(DuskBlocks.CYPRESS_STAIRS)
            .slab(DuskBlocks.CYPRESS_SLAB)
            .fence(DuskBlocks.CYPRESS_FENCE)
            .door(DuskBlocks.CYPRESS_DOOR)
            .trapdoor(DuskBlocks.CYPRESS_TRAPDOOR)
            .fenceGate(DuskBlocks.CYPRESS_FENCE_GATE)
            .button(DuskBlocks.CYPRESS_BUTTON)
            .pressurePlate(DuskBlocks.CYPRESS_PRESSURE_PLATE)
            .sign(DuskBlocks.CYPRESS_SIGN, DuskBlocks.CYPRESS_WALL_SIGN)
            .group("wooden")
            .unlockCriterionName("has_planks")
            .build()
    val CHARRED_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.CHARRED_PLANKS)
            .stairs(DuskBlocks.CHARRED_STAIRS)
            .slab(DuskBlocks.CHARRED_SLAB)

            .fence(DuskBlocks.CHARRED_FENCE)
            .door(DuskBlocks.CHARRED_DOOR)
            .trapdoor(DuskBlocks.CHARRED_TRAPDOOR)
            .fenceGate(DuskBlocks.CHARRED_FENCE_GATE)
            .button(DuskBlocks.CHARRED_BUTTON)
            .pressurePlate(DuskBlocks.CHARRED_PRESSURE_PLATE)
            .sign(DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN)

            .group("wooden")
            .unlockCriterionName("has_planks")
            .build()

    val blockFamilies = listOf(
        CUT_BRONZE_FAMILY,
        VOLCANIC_SANDSTONE_FAMILY,
        CUT_VOLCANIC_SANDSTONE_FAMILY,
        SMOOTH_VOLCANIC_SANDSTONE_FAMILY,
        CYPRESS_FAMILY,
        CHARRED_FAMILY
    )

    fun init() {}
}