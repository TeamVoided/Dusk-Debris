package org.teamvoided.dusk_debris.item.throwable_bomb.bonecaller

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonechillerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonewitherEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.ShadecallerEntity
import org.teamvoided.dusk_debris.item.throwable_bomb.AbstractThrowableBombItem


open class ShadecallerItem(block: Block, settings: Settings) : AbstractThrowableBombItem(block, settings) {

    override fun throwBomb(world: World, user: PlayerEntity, itemStack: ItemStack) {
        if (!world.isClient) {
            val bombItem = ShadecallerEntity(user, world)
            bombItem.setItem(itemStack)
            bombItem.setProperties(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(bombItem)
        }
    }

    override fun createEntity(world: World, pos: Position, stack: ItemStack, direction: Direction): ProjectileEntity {
        val bombEntity = ShadecallerEntity(pos.x, pos.y, pos.z, world)
        bombEntity.setItem(stack)
        return bombEntity
    }
}