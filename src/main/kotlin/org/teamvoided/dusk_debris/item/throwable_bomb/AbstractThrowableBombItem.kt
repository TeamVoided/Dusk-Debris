package org.teamvoided.dusk_debris.item.throwable_bomb

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.ProjectileItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity


open class AbstractThrowableBombItem(block: Block, settings: Settings) : BlockItem(block, settings), ProjectileItem {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val user = context.player!!
        return if(user.isSneaking)  super.useOnBlock(context)
        else this.use(context.world, user, context.hand).result
    }
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        if(user.isSneaking) return super.use(world, user, hand)
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
        user.itemCooldownManager.set(this, 40)
        throwBomb(world, user, itemStack)

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        itemStack.consume(1, user)
        return TypedActionResult.success(itemStack, world.isClient())
    }

    open fun throwBomb(world: World, user: PlayerEntity, itemStack: ItemStack) {
        println("Ya forgot to override throwBomb function")
    }

    override fun createEntity(world: World, pos: Position, stack: ItemStack, direction: Direction): ProjectileEntity {
        println("Ya forgot to override createEntity function")
        val blunderbombEntity = BlunderbombEntity(world, pos.x, pos.y, pos.z)
        blunderbombEntity.setItem(stack)
        return blunderbombEntity
    }
}