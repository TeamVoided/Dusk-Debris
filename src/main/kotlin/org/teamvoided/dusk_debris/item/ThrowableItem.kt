package org.teamvoided.dusk_debris.item

import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.entity.TwistingSoulChargeEntity


open class ThrowableItem(settings: Properties) : Item(settings) {

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        world.playSound(
            null as Player?,
            user.x,
            user.y,
            user.z,
            SoundEvents.WITCH_THROW,
            SoundSource.PLAYERS,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        val throwableEntity = TwistingSoulChargeEntity(world, user)
        throwableEntity.setPos(user.eyePosition)
        world.addFreshEntity(throwableEntity)

        user.awardStat(Stats.ITEM_USED.get(this))
        itemStack.consume(1, user)
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
    }
}