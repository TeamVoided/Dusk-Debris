//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.block.throwable_bomb

import net.minecraft.core.BlockPos
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.entity.throwable_bomb.FirebombEntity
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombBlock(settings: Properties) : AbstractThrwowableBombBlock(settings) {
    override val explosionBehavior: ExplosionDamageCalculator = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )
    override val explosionBehaviorOnExploded: ExplosionDamageCalculator = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )

    override fun explode(world: Level, pos: BlockPos, explosionBehavior: ExplosionDamageCalculator) {
        world.destroyBlock(pos, false)
        val firebombEntity = FirebombEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() / 2 + 0.25,
            pos.z.toDouble() + 0.5
        )
        world.addFreshEntity(firebombEntity)
    }
}