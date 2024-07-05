package org.teamvoided.dusk_debris.block.throwable_bomb.nethershroom_throwable_block

import net.minecraft.util.math.BlockPos
import net.minecraft.world.*
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.block.throwable_bomb.AbstractThrwowableBombBlock
import org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable.PocketpoisonEntity

open class PocketpoisonBlock(
    settings: Settings
) : AbstractThrwowableBombBlock(settings) {
    override fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        world.breakBlock(pos, false)
        val nethershroomThrowableEntity = PocketpoisonEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() * 0.8,
            pos.z.toDouble() + 0.5
        )
        world.spawnEntity(nethershroomThrowableEntity)
    }
}