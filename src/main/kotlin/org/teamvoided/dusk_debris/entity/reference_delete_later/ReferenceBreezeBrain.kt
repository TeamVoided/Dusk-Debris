package org.teamvoided.dusk_debris.entity.reference_delete_later

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.mob.BreezeBrain
import net.minecraft.entity.mob.BreezeEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Unit
import java.util.Set

object ReferenceBreezeBrain {
    const val SLIDING_SPEED_MULTIPLIER: Float = 0.6f
    const val JUMP_CIRCLE_INNER_RADIUS: Float = 4.0f
    const val JUMP_CIRCLE_MIDDLE_RADIUS: Float = 8.0f
    const val JUMP_CIRCLE_OUTER_RADIUS: Float = 20.0f
    val SENSORS: List<SensorType<out Sensor<in BreezeEntity>>> =
        ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            SensorType.NEAREST_PLAYERS,
            SensorType.BREEZE_ATTACK_ENTITY_SENSOR
        )
    val MEMORY_MODULES: List<MemoryModuleType<*>> = listOf(
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.VISIBLE_MOBS,
        MemoryModuleType.NEAREST_ATTACKABLE,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.BREEZE_JUMP_COOLDOWN,
        MemoryModuleType.BREEZE_JUMP_INHALING,
        MemoryModuleType.BREEZE_SHOOT,
        MemoryModuleType.BREEZE_SHOOT_CHARGING,
        MemoryModuleType.BREEZE_SHOOT_RECOVER,
        MemoryModuleType.BREEZE_SHOOT_COOLDOWN,

        MemoryModuleType.BREEZE_JUMP_TARGET,
        MemoryModuleType.BREEZE_LEAVING_WATER,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.PATH
    )

    internal fun create(breeze: BreezeEntity, brain: Brain<BreezeEntity>): Brain<*> {
        addCoreTasks(brain)
        addIdleTasks(brain)
        addFightTasks(breeze, brain)
        brain.setCoreActivities(Set.of(Activity.CORE))
        brain.setDefaultActivity(Activity.FIGHT)
        brain.resetPossibleActivities()
        return brain
    }

    private fun addCoreTasks(brain: Brain<BreezeEntity>) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(StayAboveWaterTask(0.8f), LookAroundTask(45, 90)))
    }

    private fun addIdleTasks(brain: Brain<BreezeEntity>) {
        brain.setTaskList(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(
                    0, UpdateAttackTargetTask.create { breezeEntity: BreezeEntity ->
                        breezeEntity.brain.getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                    }),
                Pair.of(1, UpdateAttackTargetTask.create { obj: BreezeEntity -> obj.recentAttacker }),
                Pair.of(2, BreezeWanderTask(20, 40)),
                Pair.of(
                    3, RandomTask(ImmutableList.of(Pair.of(WaitTask(20, 100), 1), Pair.of(MeanderTask.create(0.6f), 2)))
                )
            )
        )
    }

    private fun addFightTasks(breeze: BreezeEntity, brain: Brain<BreezeEntity>) {
        brain.setTaskList(
            Activity.FIGHT,
            ImmutableList.of(
                Pair.of(
                    0, ForgetAttackTargetTask.create { livingEntity: LivingEntity ->
                        !Sensor.testAttackableTargetPredicate(
                            breeze,
                            livingEntity
                        )
                    }),
                Pair.of(1, BreezeShootTask()),
                Pair.of(2, BreezeLongJumpTask()),
                Pair.of(3, BreezeShootWhenStuckTask()),
                Pair.of(4, BreezeSlideTask())
            ),
            ImmutableSet.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT)
            )
        )
    }

    fun updateActivities(breeze: BreezeEntity) {
        breeze.brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
    }

    class BreezeWanderTask @VisibleForTesting constructor(i: Int, j: Int) : WanderAroundTask(i, j) {
        override fun run(world: ServerWorld, mobEntity: MobEntity, l: Long) {
            super.run(world, mobEntity, l)
            mobEntity.playSound(SoundEvents.ENTITY_BREEZE_SLIDE)
            mobEntity.pose = EntityPose.SLIDING
        }

        override fun finishRunning(world: ServerWorld, mobEntity: MobEntity, l: Long) {
            super.finishRunning(world, mobEntity, l)
            mobEntity.pose = EntityPose.STANDING
            if (mobEntity.brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET)) {
                mobEntity.brain.remember(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L)
            }
        }
    }
}
