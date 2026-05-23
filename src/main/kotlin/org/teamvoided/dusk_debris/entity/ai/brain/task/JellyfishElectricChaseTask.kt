package org.teamvoided.dusk_debris.entity.ai.brain.task

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableMap
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Unit
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.ai.behavior.Behavior
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import org.teamvoided.dusk_debris.entity.GiantEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.LightningCloudEntity
import org.teamvoided.dusk_debris.init.DuskParticles

class JellyfishElectricChaseTask @VisibleForTesting constructor(runTime: Int) : Behavior<GiantEnemyJellyfishEntity>(
    ImmutableMap.of(
        MemoryModuleType.ATTACK_TARGET,
        MemoryStatus.VALUE_PRESENT,
        MemoryModuleType.BREEZE_SHOOT_COOLDOWN,
        MemoryStatus.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT_CHARGING,
        MemoryStatus.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT_RECOVERING,
        MemoryStatus.VALUE_ABSENT,
        MemoryModuleType.BREEZE_SHOOT,
        MemoryStatus.VALUE_PRESENT,
        MemoryModuleType.WALK_TARGET,
        MemoryStatus.VALUE_ABSENT,
        MemoryModuleType.BREEZE_JUMP_TARGET,
        MemoryStatus.VALUE_ABSENT
    ),
    runTime
) {
    //standing == idle
    //roaring == active

    override fun checkExtraStartConditions(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity): Boolean {
        return if (jellyfish.pose != Pose.STANDING) false
        else (jellyfish.brain.getMemory(
            MemoryModuleType.ATTACK_TARGET
        ).map { livingEntity: LivingEntity -> isTargetWithinRange(jellyfish, livingEntity) }
            .map { boolean: Boolean ->
                if (!boolean) {
                    jellyfish.brain.eraseMemory(MemoryModuleType.BREEZE_SHOOT)
                }
                boolean
            }.orElse(false))
    }

    override fun canStillUse(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity, time: Long): Boolean {
        return jellyfish.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) &&
                jellyfish.brain.hasMemoryValue(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun start(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        jellyfish.brain.getMemory(MemoryModuleType.ATTACK_TARGET)
            .ifPresent { jellyfish.pose = Pose.ROARING }
        jellyfish.brain.setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT_CHARGING, Unit.INSTANCE, INHALING_TICKS.toLong())
        jellyfish.playSound(SoundEvents.BREEZE_INHALE, 1.0f, 0.0f)
    }

    override fun stop(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        if (jellyfish.pose == Pose.ROARING) {
            jellyfish.pose = Pose.STANDING
        }

        jellyfish.brain.setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT_COOLDOWN, Unit.INSTANCE, COOLDOWN_TICKS.toLong())
        jellyfish.brain.eraseMemory(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun tick(world: ServerLevel, jellyfish: GiantEnemyJellyfishEntity, time: Long) {
        val brain = jellyfish.brain
        val target = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null) as LivingEntity
        if (target != null) {
            jellyfish.lookAt(EntityAnchorArgument.Anchor.EYES, target.position())
            if (time.toInt() % 20 == 0) {
                val cloudEntity = LightningCloudEntity(world, target.x, target.y, target.z)
                cloudEntity.particle = DuskParticles.SPARK
                jellyfish.playSound(SoundEvents.BREEZE_SHOOT, 1.5f, 0.0f)
                world.addFreshEntity(cloudEntity)
            }
        }
    }

    companion object {
        private const val MAX_ATTACK_RANGE = 32 * 32
        private val INHALING_TICKS = Math.round(15.0f)
        private val RECOVERY_TICKS = Math.round(4.0f)
        private val COOLDOWN_TICKS = Math.round(10.0f)

        private fun isTargetWithinRange(jellyfish: GiantEnemyJellyfishEntity, target: LivingEntity): Boolean {
            val d = jellyfish.position().distanceToSqr(target.position())
            return d < MAX_ATTACK_RANGE
        }
    }
}

