package org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonechillerEntity
import org.teamvoided.dusk_debris.item.throwable_bomb.AbstractThrowableBombItem


open class BonechillerItem(block: Block, settings: Properties) : AbstractThrowableBombItem(block, settings) {

    override fun throwBomb(world: Level, user: Player, itemStack: ItemStack) {
        if (!world.isClientSide) {
            val bombItem = BonechillerEntity(user, world)
            bombItem.setItem(itemStack)
            bombItem.shootFromRotation(user, user.xRot, user.yRot, 0.0f, 1.5f, 1.0f)
            world.addFreshEntity(bombItem)
        }
    }

    override fun asProjectile(world: Level, pos: Position, stack: ItemStack, direction: Direction): Projectile {
        val bombEntity = BonechillerEntity(pos.x(), pos.y(), pos.z(), world)
        bombEntity.setItem(stack)
        return bombEntity
    }
}