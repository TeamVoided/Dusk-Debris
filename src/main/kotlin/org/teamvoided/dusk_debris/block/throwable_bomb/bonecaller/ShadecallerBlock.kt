package org.teamvoided.dusk_debris.block.throwable_bomb.bonecaller

import net.minecraft.core.BlockPos
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.block.throwable_bomb.AbstractThrwowableBombBlock
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.ShadecallerEntity

open class ShadecallerBlock(settings: Properties) : AbstractThrwowableBombBlock(settings) {
    override fun explode(world: Level, pos: BlockPos, explosionBehavior: ExplosionDamageCalculator) {
        world.destroyBlock(pos, false)
        val bombEntity = ShadecallerEntity(
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() * 0.8,
            pos.z.toDouble() + 0.5,
            world
        )
        world.addFreshEntity(bombEntity)
    }
}