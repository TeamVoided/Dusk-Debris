package org.teamvoided.dusk_debris.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class EquipableBlockItemItem(block: Block, settings: Properties, private val equipmentSlot: EquipmentSlot = EquipmentSlot.HEAD) :
    BlockItem(block, settings), Equipable {
    override fun getEquipmentSlot(): EquipmentSlot {
        return equipmentSlot
    }

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        return this.swapWithEquipmentSlot(this, world, user, hand)
    }
}