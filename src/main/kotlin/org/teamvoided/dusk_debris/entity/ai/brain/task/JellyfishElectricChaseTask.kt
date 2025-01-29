package org.teamvoided.dusk_debris.entity.ai.brain.task

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableMap
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.BreezeWindChargeEntity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Unit
import org.teamvoided.dusk_debris.entity.GiantEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.LightningCloudEntity

class JellyfishElectricChaseTask @VisibleForTesting constructor(runTime: Int) : Task<GiantEnemyJellyfishEntity>(
    ImmutableMap.of(
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleState.VALUE_PRESENT,
        MemoryModuleType.BREEZE_SHOOT_COOLDOWN,
        MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT_CHARGING,
        MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT_RECOVER,
        MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT,
        MemoryModuleState.VALUE_PRESENT,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.BREEZE_JUMP_TARGET,
        MemoryModuleState.VALUE_ABSENT
    ),
    runTime
) {
    //standing == idle
    //roaring == active

    override fun shouldRun(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity): Boolean {
        return if (jellyfish.pose != EntityPose.STANDING) false
        else (jellyfish.brain.getOptionalMemory(
            MemoryModuleType.ATTACK_TARGET
        ).map { livingEntity: LivingEntity -> isTargetWithinRange(jellyfish, livingEntity) }
            .map { boolean: Boolean ->
                if (!boolean) {
                    jellyfish.brain.forget(MemoryModuleType.BREEZE_SHOOT)
                }
                boolean
            }.orElse(false))
    }

    override fun shouldKeepRunning(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity, time: Long): Boolean {
        return jellyfish.brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET) &&
                jellyfish.brain.hasMemoryModule(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun run(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        jellyfish.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET)
            .ifPresent { jellyfish.pose = EntityPose.ROARING }
        jellyfish.brain.remember(MemoryModuleType.BREEZE_SHOOT_CHARGING, Unit.INSTANCE, INHALING_TICKS.toLong())
        jellyfish.playSound(SoundEvents.ENTITY_BREEZE_INHALE, 1.0f, 0.0f)
    }

    override fun finishRunning(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        if (jellyfish.pose == EntityPose.ROARING) {
            jellyfish.pose = EntityPose.STANDING
        }

        jellyfish.brain.remember(MemoryModuleType.BREEZE_SHOOT_COOLDOWN, Unit.INSTANCE, COOLDOWN_TICKS.toLong())
        jellyfish.brain.forget(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun keepRunning(world: ServerWorld, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        val brain = jellyfish.brain
        val target = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).orElse(null) as LivingEntity
        if (target != null) {
            jellyfish.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target.pos)
            if (time.toInt() % 20 == 0) {
                val cloudEntity = LightningCloudEntity(world, target.x, target.y, target.z)
                jellyfish.playSound(SoundEvents.ENTITY_BREEZE_SHOOT, 1.5f, 0.0f)
                world.spawnEntity(cloudEntity)
            }
        }
    }

    companion object {
        private const val MAX_ATTACK_RANGE = 32 * 32
        private val INHALING_TICKS = Math.round(15.0f)
        private val RECOVERY_TICKS = Math.round(4.0f)
        private val COOLDOWN_TICKS = Math.round(10.0f)

        private fun isTargetWithinRange(jellyfish: GiantEnemyJellyfishEntity, target: LivingEntity): Boolean {
            val d = jellyfish.pos.squaredDistanceTo(target.pos)
            return d < MAX_ATTACK_RANGE
        }
    }
}

