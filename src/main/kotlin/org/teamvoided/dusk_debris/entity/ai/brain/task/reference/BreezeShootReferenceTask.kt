package org.teamvoided.dusk_debris.entity.ai.brain.task.reference

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableMap
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.BreezeWindChargeEntity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.mob.BreezeEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Unit

class BreezeShootReferenceTask @VisibleForTesting constructor() : Task<BreezeEntity>(
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
    INHALING_TICKS + 1 + RECOVERY_TICKS
) {
    override fun shouldRun(world: ServerWorld, breezeEntity: BreezeEntity): Boolean {
        return if (breezeEntity.pose != EntityPose.STANDING) false
        else (breezeEntity.brain.getOptionalMemory(
            MemoryModuleType.ATTACK_TARGET
        ).map { livingEntity: LivingEntity -> isTargetWithinRange(breezeEntity, livingEntity) }
            .map { boolean: Boolean? ->
                if (!boolean!!) {
                    breezeEntity.brain.forget(MemoryModuleType.BREEZE_SHOOT)
                }
                boolean
            }.orElse(false))
    }

    override fun shouldKeepRunning(world: ServerWorld, breezeEntity: BreezeEntity, l: Long): Boolean {
        return breezeEntity.brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET) &&
                breezeEntity.brain.hasMemoryModule(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun run(world: ServerWorld, breezeEntity: BreezeEntity, l: Long) {
        breezeEntity.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET)
            .ifPresent { breezeEntity.pose = EntityPose.SHOOTING }
        breezeEntity.brain.remember(MemoryModuleType.BREEZE_SHOOT_CHARGING, Unit.INSTANCE, INHALING_TICKS.toLong())
        breezeEntity.playSound(SoundEvents.ENTITY_BREEZE_INHALE, 1.0f, 1.0f)
    }

    override fun finishRunning(world: ServerWorld, breezeEntity: BreezeEntity, l: Long) {
        if (breezeEntity.pose == EntityPose.SHOOTING) {
            breezeEntity.pose = EntityPose.STANDING
        }

        breezeEntity.brain.remember(MemoryModuleType.BREEZE_SHOOT_COOLDOWN, Unit.INSTANCE, COOLDOWN_TICKS.toLong())
        breezeEntity.brain.forget(MemoryModuleType.BREEZE_SHOOT)
    }

    override fun keepRunning(world: ServerWorld, breezeEntity: BreezeEntity, l: Long) {
        val brain = breezeEntity.brain
        val livingEntity = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).orElse(null) as LivingEntity
        if (livingEntity != null) {
            breezeEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, livingEntity.pos)
            if (!brain.getOptionalMemory(MemoryModuleType.BREEZE_SHOOT_CHARGING).isPresent && !brain.getOptionalMemory(
                    MemoryModuleType.BREEZE_SHOOT_RECOVER
                ).isPresent
            ) {
                brain.remember(MemoryModuleType.BREEZE_SHOOT_RECOVER, Unit.INSTANCE, RECOVERY_TICKS.toLong())
                if (isFacingTarget(breezeEntity, livingEntity)) {
                    val d = livingEntity.x - breezeEntity.x
                    val e =
                        livingEntity.getBodyY(if (livingEntity.hasVehicle()) 0.8 else 0.3) - breezeEntity.getBodyY(0.5)
                    val f = livingEntity.z - breezeEntity.z
                    val breezeWindChargeEntity = BreezeWindChargeEntity(breezeEntity, world)
                    breezeEntity.playSound(SoundEvents.ENTITY_BREEZE_SHOOT, 1.5f, 1.0f)
                    breezeWindChargeEntity.setVelocity(
                        d,
                        e,
                        f,
                        PROJECTILE_SPEED,
                        (BASE_PROJECTILE_DEVIANCE - world.difficulty.id * PROJECTILE_DEVIANCE_MULTIPLIER).toFloat()
                    )
                    world.spawnEntity(breezeWindChargeEntity)
                }
            }
        }
    }

    companion object {
        private const val MIN_SQUARED_ATTACK_RANGE = 4
        private const val MAX_SQUARED_ATTACK_RANGE = 256
        private const val BASE_PROJECTILE_DEVIANCE = 5
        private const val PROJECTILE_DEVIANCE_MULTIPLIER = 4
        private const val PROJECTILE_SPEED = 0.7f
        private val INHALING_TICKS = Math.round(15.0f)
        private val RECOVERY_TICKS = Math.round(4.0f)
        private val COOLDOWN_TICKS = Math.round(10.0f)

        @VisibleForTesting
        fun isFacingTarget(breeze: BreezeEntity, target: LivingEntity): Boolean {
            val vec3d = breeze.getRotationVec(1.0f)
            val vec3d2 = target.pos.subtract(breeze.pos).normalize()
            return vec3d.dotProduct(vec3d2) > 0.5
        }

        private fun isTargetWithinRange(breeze: BreezeEntity, target: LivingEntity): Boolean {
            val d = breeze.pos.squaredDistanceTo(target.pos)
            return d > MIN_SQUARED_ATTACK_RANGE && d < MAX_SQUARED_ATTACK_RANGE
        }
    }
}

