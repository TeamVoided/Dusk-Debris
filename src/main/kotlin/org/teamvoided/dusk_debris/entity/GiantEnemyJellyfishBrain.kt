package org.teamvoided.dusk_debris.entity

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
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Unit
import net.minecraft.util.dynamic.GlobalPos
import org.teamvoided.dusk_debris.init.brain.DuskSensorType
import java.util.*
import java.util.Set

object GiantEnemyJellyfishBrain {
    private const val ANGER_DURATION = 600
    private const val MELEE_ATTACK_COOLDOWN = 20
    private const val ACTIVITY_SOUND_PROBABILITY = 0.0125
    private const val MAX_FOLLOW_DISTANCE = 8f
    private const val INTERACTION_RANGE = 8
    private const val TARGETING_RANGE = 12.0
    private const val IDLING_SPEED = 0.6f
    private const val HOME_CLOSE_ENOUGH_DISTANCE = 2
    private const val HOME_TOO_FAR_DISTANCE = 100
    private const val HOME_STROLL_AROUND_DISTANCE = 5

    val SENSORS: List<SensorType<out Sensor<in GiantEnemyJellyfishEntity>>> =
        listOf(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY,
            DuskSensorType.GEJ_ATTACK_ENTITY_SENSOR
        )
    val MEMORY_MODULES: List<MemoryModuleType<out Any>> =
        listOf(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.MOBS,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
//            MemoryModuleType.IS_EMERGING,

            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.HOME
        )


    internal fun create(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>): Brain<*> {
        addCoreTasks(brain)
        addIdleTasks(brain)
        addFightTasks(jellyfish, brain)
        brain.setCoreActivities(Set.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    fun createProfile(): Brain.Profile<GiantEnemyJellyfishEntity> =
        Brain.createProfile(MEMORY_MODULES, SENSORS as Nothing?)


    internal fun setCurrentPosAsHome(jellyfish: GiantEnemyJellyfishEntity) {
        val globalPos = GlobalPos.create(jellyfish.world.registryKey, jellyfish.blockPos)
        jellyfish.brain.remember(MemoryModuleType.HOME, globalPos)
    }


    private fun addCoreTasks(brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(StayAboveWaterTask(0.8f), LookAroundTask(45, 90)))
    }

    private fun addIdleTasks(brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(
                    0, UpdateAttackTargetTask.create { jellyfish: GiantEnemyJellyfishEntity ->
                        jellyfish.brain.getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                    }),
                Pair.of(1, UpdateAttackTargetTask.create { it.getRecentAttacker() }),
                Pair.of(2, WanderAroundTask(20, 40)),
                Pair.of(
                    3, RandomTask(
                        ImmutableList.of(
                            Pair.of(WaitTask(20, 100), 1),
                            Pair.of(MeanderTask.create(0.6f), 2)
                        )
                    )
                )
            )
        )
    }

    private fun addFightTasks(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.FIGHT,
            ImmutableList.of<Pair<Int, TaskControl<GiantEnemyJellyfishEntity>>>(
                Pair.of(
                    0,
                    ForgetAttackTargetTask.create { target: LivingEntity ->
                        !Sensor.testAttackableTargetPredicate(jellyfish, target)
                    }),
//                Pair.of(1, BreezeShootTask()),
//                Pair.of(2, BreezeLongJumpTask()),
//                Pair.of(3, BreezeShootWhenStuckTask()),
//                Pair.of(4, BreezeSlideTask())
            ),
            ImmutableSet.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT)
            )
        )
    }

    fun updateActivities(jellyfish: GiantEnemyJellyfishEntity) {
        jellyfish.brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
    }
}
