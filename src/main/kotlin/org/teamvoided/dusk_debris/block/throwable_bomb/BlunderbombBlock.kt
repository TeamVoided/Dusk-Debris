package org.teamvoided.dusk_debris.block.throwable_bomb

import net.minecraft.util.math.BlockPos
import net.minecraft.world.*
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity
import org.teamvoided.dusk_debris.world.explosion.BlunderbombExplosionBehavior

open class BlunderbombBlock(settings: Settings) : AbstractThrwowableBombBlock(settings) {

    override val explosionBehavior: ExplosionBehavior = BlunderbombExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
        1.1f,
        12f
    )

    override val explosionBehaviorOnExploded: ExplosionBehavior = BlunderbombExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
        Math.random().toFloat(),
        Math.random().toFloat() * 3 + 4f
    )
    override fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        world.breakBlock(pos, false)
        val blunderbombEntity = BlunderbombEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() * 0.8,
            pos.z.toDouble() + 0.5,
            explosionBehavior
        )
        world.spawnEntity(blunderbombEntity)
    }
}