package org.teamvoided.dusk_debris.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.TwistingSoulChargeEntity


open class ThrowableItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        world.playSound(
            null as PlayerEntity?,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_WITCH_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        val throwableEntity = TwistingSoulChargeEntity(world, user)
        throwableEntity.setPosition(user.eyePos)
        world.spawnEntity(throwableEntity)

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        itemStack.consume(1, user)
        return TypedActionResult.success(itemStack, world.isClient())
    }
}