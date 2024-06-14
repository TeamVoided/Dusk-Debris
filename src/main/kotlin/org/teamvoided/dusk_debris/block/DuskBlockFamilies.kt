package org.teamvoided.dusk_debris.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.minecraft.block.BlockSetType
import net.minecraft.block.Blocks
import net.minecraft.block.sign.SignType
import net.minecraft.data.family.BlockFamilies
import net.minecraft.data.family.BlockFamily
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.init.DuskBlocks

object DuskBlockFamilies {
    fun init() {}
    val VOLCANIC_SANDSTONE: BlockFamily =
        BlockFamilies.register(DuskBlocks.VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.VOLCANIC_SANDSTONE_SLAB)
            .wall(DuskBlocks.VOLCANIC_SANDSTONE_WALL)
            .chiseled(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE)
            .cut(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .noGenerateRecipes().build()
    val CUT_VOLCANIC_SANDSTONE: BlockFamily =
        BlockFamilies.register(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .slab(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB)
            .build()
    val SMOOTH_VOLCANIC_SANDSTONE: BlockFamily =
        BlockFamilies.register(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE)
            .stairs(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS)
            .slab(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB)
            .build()
    val CHARRED: BlockFamily =
        BlockFamilies.register(DuskBlocks.CHARRED_PLANKS)
            .stairs(DuskBlocks.CHARRED_STAIRS)
            .slab(DuskBlocks.CHARRED_SLAB)
//            .door(DuskBlocks.CHARRED_DOOR)
//            .trapdoor(DuskBlocks.CHARRED_TRAPDOOR)
//            .fence(DuskBlocks.CHARRED_FENCE)
//            .fenceGate(DuskBlocks.CHARRED_FENCE_GATE)
//            .button(DuskBlocks.CHARRED_BUTTON)
//            .pressurePlate(DuskBlocks.CHARRED_PRESSURE_PLATE)
//            .sign(DuskBlocks.CHARRED_SIGN, DuskBlocks.CHARRED_WALL_SIGN)
//            .group("wooden")
//            .unlockCriterionName("has_planks")
            .build()


    val CHARRED_BLOCK_SET_TYPE: BlockSetType =
        BlockSetTypeBuilder()
            .openableByHand(true)
            .openableByWindCharge(true)
            .buttonActivatedByArrows(true)
            .soundGroup(BlockSoundGroup.WOOD)
            .doorOpenSound(SoundEvents.BLOCK_WOODEN_DOOR_OPEN)
            .doorCloseSound(SoundEvents.BLOCK_WOODEN_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF)
            .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
            .register(id("charred"))
    val CHARRED_SIGN_TYPE = SignType.DARK_OAK
}