package org.teamvoided.dusk_debris.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Equippable
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class BonecallerBandanaItem(settings: Settings) : Item(settings), Equippable {
    override fun getPreferredSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return this.use(this, world, user, hand)
    }
}