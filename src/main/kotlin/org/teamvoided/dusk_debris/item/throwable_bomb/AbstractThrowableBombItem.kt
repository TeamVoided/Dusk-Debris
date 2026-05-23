package org.teamvoided.dusk_debris.item.throwable_bomb

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity


open class AbstractThrowableBombItem(block: Block, settings: Properties) : BlockItem(block, settings), ProjectileItem {

    override fun useOn(context: UseOnContext): InteractionResult {
        val user = context.player!!
        return if(user.isShiftKeyDown)  super.useOn(context)
        else this.use(context.level, user, context.hand).result
    }
    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        if(user.isShiftKeyDown) return super.use(world, user, hand)
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
        user.cooldowns.addCooldown(this, 40)
        throwBomb(world, user, itemStack)

        user.awardStat(Stats.ITEM_USED.get(this))
        itemStack.consume(1, user)
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
    }

    open fun throwBomb(world: Level, user: Player, itemStack: ItemStack) {
        println("Ya forgot to override throwBomb function")
    }

    override fun asProjectile(world: Level, pos: Position, stack: ItemStack, direction: Direction): Projectile {
        println("Ya forgot to override createEntity function")
        val blunderbombEntity = BlunderbombEntity(world, pos.x(), pos.y(), pos.z())
        blunderbombEntity.setItem(stack)
        return blunderbombEntity
    }
}