package org.teamvoided.dusk_debris.item

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.FirebombEntity


class FirebombItem(block: Block, settings: Settings) : BlunderbombItem(block, settings), ProjectileItem {

    override fun throwBomb(world: World, user: PlayerEntity, itemStack: ItemStack) {
        if (!world.isClient) {
            val bombItem = FirebombEntity(world, user)
            bombItem.setItem(itemStack)
            bombItem.setProperties(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(bombItem)
        }
    }

    override fun createEntity(world: World, pos: Position, stack: ItemStack, direction: Direction): ProjectileEntity {
        val bombEntity = FirebombEntity(world, pos.x, pos.y, pos.z)
        bombEntity.setItem(stack)
        return bombEntity
    }
}