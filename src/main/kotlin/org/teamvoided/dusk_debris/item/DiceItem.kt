package org.teamvoided.dusk_debris.item

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.entity.DiceEntity

class DiceItem(settings: Properties) : Item(settings), ProjectileItem {
    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        world.playSound(
            null as Player?,
            user.x,
            user.y,
            user.z,
            SoundEvents.SNOWBALL_THROW,
            SoundSource.NEUTRAL,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        if (!world.isClientSide) {
            val diceEntity = DiceEntity(world, user, itemStack.copyWithCount(1))
            diceEntity.shootFromRotation(user, user.xRot, user.yRot, 0.0f, 0.5f, 1.0f)
            world.addFreshEntity(diceEntity)
        }

        user.awardStat(Stats.ITEM_USED.get(this))
        itemStack.consume(1, user)
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
    }

    override fun asProjectile(world: Level, pos: Position, stack: ItemStack, direction: Direction): Projectile {
        val diceEntity = DiceEntity(world, pos.x(), pos.y(), pos.z(), stack)
        return diceEntity
    }

    companion object {
    }

}