package org.teamvoided.dusk_debris.block

import net.minecraft.data.family.BlockFamilies
import net.minecraft.data.family.BlockFamily
import org.teamvoided.dusk_debris.init.DuskBlocks

object DuskBlockFamilies {
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

    val CYPRUS_FAMILY: BlockFamily =
        BlockFamilies.register(DuskBlocks.CYPRUS_PLANKS)
            .stairs(DuskBlocks.CYPRUS_STAIRS)
            .slab(DuskBlocks.CYPRUS_SLAB)
            .fence(DuskBlocks.CYPRUS_FENCE)
            .door(DuskBlocks.CYPRUS_DOOR)
            .trapdoor(DuskBlocks.CYPRUS_TRAPDOOR)
            .fenceGate(DuskBlocks.CYPRUS_FENCE_GATE)
            .button(DuskBlocks.CYPRUS_BUTTON)
            .pressurePlate(DuskBlocks.CYPRUS_PRESSURE_PLATE)
            .sign(DuskBlocks.CYPRUS_SIGN, DuskBlocks.CYPRUS_WALL_SIGN)
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

    fun init() {}
}