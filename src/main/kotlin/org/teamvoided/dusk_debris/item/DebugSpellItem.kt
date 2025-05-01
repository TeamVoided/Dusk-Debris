package org.teamvoided.dusk_debris.item

import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.item.ProjectileItem.DispenserConfig
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.init.DuskComponents
import org.teamvoided.dusk_debris.util.spellController

class DebugSpellItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        itemStack.itemComponents.forEach {
            println(it)
        }
        if (itemStack.itemComponents.contains(DuskComponents.SPELL)) {
            if (!world.isClient()) {
                val spell = itemStack.itemComponents.get(DuskComponents.SPELL)!!.spell
                user.spellController.setSpell(user, spell)
            }
            return TypedActionResult.success(user.getStackInHand(hand), world.isClient())
        }
        return super.use(world, user, hand)
    }
}