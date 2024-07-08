package org.teamvoided.dusk_debris.item

import net.minecraft.block.Block
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Equippable
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class EquipableBlockItemItem(block: Block, settings: Settings, private val equipmentSlot: EquipmentSlot = EquipmentSlot.HEAD) :
    BlockItem(block, settings), Equippable {
    override fun getPreferredSlot(): EquipmentSlot {
        return equipmentSlot
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return this.use(this, world, user, hand)
    }
}