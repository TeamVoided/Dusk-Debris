package org.teamvoided.dusk_debris.item.throwable_bomb

import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Holder
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.init.DuskEntities


open class NethershroomThrowableItem(
    block: Block,
    val entity: EntityType<out NethershroomThrowableEntity>,
    val particle: ParticleEffect,
    val statusEffect: Holder<StatusEffect>?,
    val hasDoubleEffect: Boolean,
    settings: Settings
) : AbstractThrowableBombItem(block, settings) {

    override fun throwBomb(world: World, user: PlayerEntity, itemStack: ItemStack) {
        if (!world.isClient) {
            val bombItem = NethershroomThrowableEntity(world, user, this, entity, particle, statusEffect, hasDoubleEffect)
            bombItem.setItem(itemStack)
            bombItem.setProperties(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(bombItem)
        }
    }

    override fun createEntity(world: World, pos: Position, stack: ItemStack, direction: Direction): ProjectileEntity {
        val bombEntity =
            NethershroomThrowableEntity(world, pos.x, pos.y, pos.z, this.asItem(), entity, particle, statusEffect, hasDoubleEffect)
        bombEntity.setItem(stack)
        return bombEntity
    }
}