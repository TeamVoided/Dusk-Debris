package org.teamvoided.dusk_debris.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.init.DuskComponents
import org.teamvoided.dusk_debris.util.spellController

class DebugSpellItem(settings: Properties) : Item(settings) {
    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        itemStack.prototype.forEach {
            println(it)
        }
        if (itemStack.prototype.has(DuskComponents.SPELL)) {
            if (!world.isClientSide) {
                val spell = itemStack.prototype.get(DuskComponents.SPELL)!!.spell
                user.spellController.setSpell(user, spell)
            }
            return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide)
        }
        return super.use(world, user, hand)
    }
}