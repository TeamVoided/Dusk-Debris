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
import kotlin.math.sqrt

open class SpecialExplosionBehavior(
    private val destroyCondition: TagKey<Block>,
    private val damageCondition: TagKey<EntityType<*>>,
    private val knockbackMultiplier: Float,
    private val maxDamage: Float
) : ExplosionBehavior() {

//    fun SpecialExplosionBehavior(destroyTag: TagKey<Block>, damageTag: TagKey<EntityType<*>>) {
//        SpecialExplosionBehavior(destroyTag, damageTag, null, null)
//    }

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
        return if (entity.type.isIn(damageCondition)) false
        else super.shouldDamage(explosion, entity)
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return knockbackMultiplier * super.getKnockbackMultiplier(target)
    }

    override fun calculateDamage(explosion: Explosion, entity: Entity): Float {
        val range = explosion.power * 2.0f
        val sourcePosition = explosion.position
        val distance = sqrt(entity.squaredDistanceTo(sourcePosition)) / range.toDouble()
        val exposeDist = (1.0 - distance) * Explosion.getExposure(sourcePosition, entity).toDouble()
        return ((exposeDist * exposeDist + exposeDist) / 2.0 * maxDamage).toFloat()
    }
//    power is range


//    this is a gradual before steep drop off, instead of steep then gradual
//    return (-(exposedDistance * exposedDistance - (2 * exposedDistance)) * maxDamage).toFloat()
}