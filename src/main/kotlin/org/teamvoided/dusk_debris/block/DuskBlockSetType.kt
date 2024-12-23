package org.teamvoided.dusk_debris.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeBuilder
import net.minecraft.block.BlockSetType
import net.minecraft.block.WoodType
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBlockSetType {
    val BRONZE: BlockSetType =
        BlockSetTypeBuilder()
            .openableByHand(true)
            .openableByWindCharge(true)
            .buttonActivatedByArrows(true)
            .soundGroup(BlockSoundGroup.COPPER)
            .doorOpenSound(SoundEvents.BLOCK_COPPER_DOOR_OPEN)
            .doorCloseSound(SoundEvents.BLOCK_COPPER_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
            .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
            .register(id("copper"))


    val CYPRESS_BLOCK_SET_TYPE: BlockSetType =
        BlockSetTypeBuilder()
            .openableByHand(true)
            .openableByWindCharge(true)
            .buttonActivatedByArrows(true)
            .soundGroup(BlockSoundGroup.WOOD)
            .doorOpenSound(SoundEvents.BLOCK_CHERRY_WOOD_DOOR_OPEN)
            .doorCloseSound(SoundEvents.BLOCK_CHERRY_WOOD_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.BLOCK_CHERRY_WOOD_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF)
            .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
            .register(id("cypress"))
    val CYPRESS_WOOD_TYPE: WoodType = WoodType.register(WoodType(id("cypress"), CYPRESS_BLOCK_SET_TYPE))

    val SEQUOIA_BLOCK_SET_TYPE: BlockSetType = BlockSetTypeBuilder().register(id("sequoia"))
    val SEQUOIA_WOOD_TYPE = WoodType("sequoia", WoodType.SPRUCE, SEQUOIA_BLOCK_SET_TYPE)

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
    val CHARRED_WOOD_TYPE: WoodType = WoodType.register(WoodType(id("charred"), CHARRED_BLOCK_SET_TYPE))


    private fun WoodType(id: Identifier, blockSetType: BlockSetType): WoodType =
        WoodType.register(WoodType(id.toString(), blockSetType))

    private fun WoodType(id: String, woodType: WoodType, blockSet: BlockSetType): WoodType =
        WoodTypeBuilder.copyOf(woodType).register(id(id), blockSet)

    fun init() {}

}