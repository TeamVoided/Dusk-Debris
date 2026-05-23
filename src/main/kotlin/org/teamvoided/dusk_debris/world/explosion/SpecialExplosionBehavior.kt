package org.teamvoided.dusk_debris.world.explosion

import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import java.util.*
import kotlin.math.sqrt

open class SpecialExplosionBehavior(
    private val destroyCondition: TagKey<Block>,
    private val damageCondition: TagKey<EntityType<*>>,
    private val range: Float,
    private val knockbackMultiplier: Float,
    private val maxDamage: Float
) : ExplosionDamageCalculator() {

//    fun SpecialExplosionBehavior(destroyTag: TagKey<Block>, damageTag: TagKey<EntityType<*>>) {
//        SpecialExplosionBehavior(destroyTag, damageTag, null, null)
//    }

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
        return if (entity.type.`is`(damageCondition)) false
        else super.shouldDamageEntity(explosion, entity)
    }

    override fun getKnockbackMultiplier(target: Entity): Float {
        return knockbackMultiplier * super.getKnockbackMultiplier(target)
    }

    override fun getEntityDamageAmount(explosion: Explosion, entity: Entity): Float {
        val sourcePosition = explosion.center()
        val distance = sqrt(entity.distanceToSqr(sourcePosition)) / (range)
        val exposeDist = (1.0 - distance) * Explosion.getSeenPercent(sourcePosition, entity).toDouble()
        return (-(exposeDist * exposeDist - (2 * exposeDist)) * maxDamage).toFloat()
    }
//    power is range
}