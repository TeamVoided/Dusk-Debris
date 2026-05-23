package org.teamvoided.dusk_debris.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BonecallerBandanaItem(settings: Properties) : Item(settings), Equipable {
    override fun getEquipmentSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        return this.swapWithEquipmentSlot(this, world, user, hand)
    }
}