//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.block.throwable_bomb

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.entity.throwable_bomb.FirebombEntity
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombBlock(settings: Settings) : AbstractThrwowableBombBlock(settings) {
    override val explosionBehavior: ExplosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )
    override val explosionBehaviorOnExploded: ExplosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )

    override fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        world.breakBlock(pos, false)
        val firebombEntity = FirebombEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() / 2 + 0.25,
            pos.z.toDouble() + 0.5
        )
        world.spawnEntity(firebombEntity)
    }
}