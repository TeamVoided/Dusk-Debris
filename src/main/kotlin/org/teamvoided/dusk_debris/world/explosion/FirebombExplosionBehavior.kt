package org.teamvoided.dusk_debris.world.explosion

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import java.util.*

class FirebombExplosionBehavior(
    private val destroyCondition: TagKey<Block>
) : ExplosionBehavior() {
    override fun getBlastResistance(
        explosion: Explosion,
        world: BlockView,
        pos: BlockPos,
        blockState: BlockState,
        fluidState: FluidState
    ): Optional<Float> {
        return if (!blockState.isIn(destroyCondition)) Optional.empty()
        else super.getBlastResistance(explosion, world, pos, blockState, fluidState)
    }

    override fun canDestroyBlock(
        explosion: Explosion,
        world: BlockView,
        pos: BlockPos,
        state: BlockState,
        power: Float
    ): Boolean {
        return if (!state.isIn(destroyCondition)) false
        else super.canDestroyBlock(explosion, world, pos, state, power)
    }

    override fun shouldDamage(explosion: Explosion, entity: Entity): Boolean {
        return false
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return 0f
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        return 0f
    }
}