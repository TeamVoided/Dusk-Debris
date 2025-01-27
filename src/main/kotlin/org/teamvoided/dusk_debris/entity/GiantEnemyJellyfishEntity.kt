package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.GhastEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.util.Utils.degToRad

class GiantEnemyJellyfishEntity(entityType: EntityType<out AbstractVolaphyraEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world) {
    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE.get(this.random);
    }

//    override fun createBrainProfile(): Brain.Profile<*> {
//        return Brain.createProfile(MEMORY_MODULES, SENSORS);
//    }

    fun canSpawn(
        type: EntityType<GiantEnemyJellyfishEntity>,
        world: WorldAccess,
        spawnReason: SpawnReason,
        pos: BlockPos,
        random: RandomGenerator
    ): Boolean {
        if (world.difficulty != Difficulty.PEACEFUL) {
            if (SpawnReason.isSpawner(spawnReason)) {
                return true
            } else if (random.nextInt(20) == 0) {
                return !(!world.getFluidState(pos).isIn(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.up()).isIn(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.up(2)).isIn(DuskFluidTags.ACID))
            }
        }
        return false
    }

//    override fun damage(source: DamageSource?, amount: Float): Boolean {
//        return if (isInvulnerable) this.isRemoved() || this.invulnerable
//        else super.damage(source, amount)
//    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        if (source.attacker != null) {
            val entity: Entity = source.attacker!!
//        playBounce(mult - 0.1f, world, pos)
            launchFromFacing(entity, -(amount * 0.2f + 0.5))
//        onLandedUpon(world, state, pos, entity, entity.fallDistance)
//        if (mult > 3) {
//            explodeBlock(world, pos)
//        } else if (mult > 0.75) {
//            launchParticles(world, pos, world.random.nextInt((mult * 50).toInt()), mult.toDouble())
//        }
        }

        return super.applyEnchantmentsToDamage(source, amount)
    }

    private fun launchFromFacing(entity: Entity, mult: Double) {
        val pitchSin: Double = MathHelper.sin(entity.pitch * degToRad).toDouble()
        val pitchCos: Double = MathHelper.cos(entity.pitch * degToRad).toDouble()
        val yawSin: Double = MathHelper.sin(entity.yaw * degToRad).toDouble()
        val yawCos: Double = MathHelper.cos(entity.yaw * degToRad).toDouble()
        entity.addVelocity(
            -yawSin * pitchCos * mult,
            -pitchSin * mult,
            yawCos * pitchCos * mult
        )
    }

    companion object {
//        val SENSORS = ImmutableList.of(
//            SensorType.NEAREST_LIVING_ENTITIES,
//            SensorType.NEAREST_PLAYERS,
//            SensorType.HURT_BY,
//            SensorType.NEAREST_ITEMS
//        )
//        val MEMORY_MODULES: ImmutableList<MemoryModuleType<*>> = ImmutableList.of(
//            MemoryModuleType.PATH,
//            MemoryModuleType.LOOK_TARGET,
//            MemoryModuleType.VISIBLE_MOBS,
//            MemoryModuleType.WALK_TARGET,
//            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
//            MemoryModuleType.HURT_BY,
//            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
//            MemoryModuleType.LIKED_PLAYER,
//            MemoryModuleType.LIKED_NOTEBLOCK,
//            MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS,
//            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
//            MemoryModuleType.IS_PANICKING,
//            *arrayOfNulls<MemoryModuleType<*>>(0)
//        )

        val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(40, 79)
    }
}