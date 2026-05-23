package org.teamvoided.dusk_debris.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBlockSetType {
    val BRONZE: BlockSetType =
        BlockSetTypeBuilder()
            .openableByHand(true)
            .openableByWindCharge(true)
            .buttonActivatedByArrows(true)
            .soundGroup(SoundType.COPPER)
            .doorOpenSound(SoundEvents.COPPER_DOOR_OPEN)
            .doorCloseSound(SoundEvents.COPPER_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.COPPER_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.COPPER_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.STONE_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.STONE_BUTTON_CLICK_OFF)
            .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
            .register(id("copper"))


    val CYPRESS_BLOCK_SET_TYPE: BlockSetType =
        BlockSetTypeBuilder()
            .openableByHand(true)
            .openableByWindCharge(true)
            .buttonActivatedByArrows(true)
            .soundGroup(SoundType.WOOD)
            .doorOpenSound(SoundEvents.CHERRY_WOOD_DOOR_OPEN)
            .doorCloseSound(SoundEvents.CHERRY_WOOD_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF)
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
            .soundGroup(SoundType.WOOD)
            .doorOpenSound(SoundEvents.WOODEN_DOOR_OPEN)
            .doorCloseSound(SoundEvents.WOODEN_DOOR_CLOSE)
            .trapdoorOpenSound(SoundEvents.WOODEN_TRAPDOOR_OPEN)
            .trapdoorCloseSound(SoundEvents.WOODEN_TRAPDOOR_CLOSE)
            .pressurePlateClickOnSound(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON)
            .pressurePlateClickOffSound(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF)
            .buttonClickOnSound(SoundEvents.WOODEN_BUTTON_CLICK_ON)
            .buttonClickOffSound(SoundEvents.WOODEN_BUTTON_CLICK_OFF)
            .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
            .register(id("charred"))
    val CHARRED_WOOD_TYPE: WoodType = WoodType.register(WoodType(id("charred"), CHARRED_BLOCK_SET_TYPE))

    val GALLERY_MAPLE_BLOCK_SET_TYPE: BlockSetType = BlockSetTypeBuilder().register(id("gallery_maple"))
    val GALLERY_MAPLE_WOOD_TYPE = registerWoodType("gallery_maple", WoodType.MANGROVE, GALLERY_MAPLE_BLOCK_SET_TYPE)

    val BONEWOOD_BLOCK_SET_TYPE: BlockSetType = BlockSetTypeBuilder().register(id("bonewood"))
    val BONEWOOD_WOOD_TYPE = registerWoodType("bonewood", WoodType.SPRUCE, BONEWOOD_BLOCK_SET_TYPE)
    val WITHERING_BONEWOOD_BLOCK_SET_TYPE: BlockSetType = BlockSetTypeBuilder().register(id("withering_bonewood"))
    val WITHERING_BONEWOOD_WOOD_TYPE =
        registerWoodType("withering_bonewood", BONEWOOD_WOOD_TYPE, WITHERING_BONEWOOD_BLOCK_SET_TYPE)


    private fun registerWoodType(id: String, woodType: WoodType, blockSet: BlockSetType): WoodType =
        WoodTypeBuilder.copyOf(woodType).register(id(id), blockSet)

    private fun WoodType(id: ResourceLocation, blockSetType: BlockSetType): WoodType =
        WoodType.register(WoodType(id.toString(), blockSetType))

    private fun WoodType(id: String, woodType: WoodType, blockSet: BlockSetType): WoodType =
        WoodTypeBuilder.copyOf(woodType).register(id(id), blockSet)

    fun init() {}

}