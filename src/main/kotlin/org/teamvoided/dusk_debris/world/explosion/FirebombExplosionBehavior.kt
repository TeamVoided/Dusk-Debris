package org.teamvoided.dusk_debris.world.explosion

import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import java.util.*

class FirebombExplosionBehavior(
    private val destroyCondition: TagKey<Block>
) : ExplosionDamageCalculator() {
    override fun getBlockExplosionResistance(
        explosion: Explosion,
        world: BlockGetter,
        pos: BlockPos,
        blockState: BlockState,
        fluidState: FluidState
    ): Optional<Float> {
        return if (!blockState.`is`(destroyCondition)) Optional.empty()
        else super.getBlockExplosionResistance(explosion, world, pos, blockState, fluidState)
    }

    override fun shouldBlockExplode(
        explosion: Explosion,
        world: BlockGetter,
        pos: BlockPos,
        state: BlockState,
        power: Float
    ): Boolean {
        return if (!state.`is`(destroyCondition)) false
        else super.shouldBlockExplode(explosion, world, pos, state, power)
    }

    override fun shouldDamageEntity(explosion: Explosion, entity: Entity): Boolean {
        return false
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return 0f
    }

    override fun getEntityDamageAmount(explosion: Explosion, entity: Entity): Float {
        return 0f
    }
}