package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ReportingTaskControl
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.brain.task.*

object TinyEnemyJellyfishBrain {
    private const val PANICKING_SPEED = 2f


    val SENSORS: List<SensorType<Sensor<TinyEnemyJellyfishEntity>>> =
        listOf(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
        ) as List<SensorType<Sensor<TinyEnemyJellyfishEntity>>>
    val MEMORY_MODULES: List<MemoryModuleType<out Any>> =
        listOf(
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.IS_PANICKING
        )


    internal fun create(brain: Brain<TinyEnemyJellyfishEntity>): Brain<*> {
        addCoreActivities(brain)
        addIdleActivities(brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    fun createProfile(): Brain.Profile<TinyEnemyJellyfishEntity> =
        Brain.createProfile(MEMORY_MODULES, SENSORS)

    private fun addCoreActivities(brain: Brain<TinyEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.CORE, 0, ImmutableList.of(
                WalkTask(PANICKING_SPEED),
                LookAroundTask(45, 90),
                WanderAroundTask()
            )
        )
    }

    private fun addIdleActivities(brain: Brain<TinyEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0, WanderAroundTask(20, 100)),
                Pair.of(
                    1, RandomTask(
                        ImmutableList.of(
                            Pair.of(WaitTask(20, 100), 1),
                            Pair.of(MeanderTask.create(1f), 2),
                            Pair.of(GoTowardsLookTarget.create(1f, 3), 2)
                        )
                    )
                ),
            )
        )
    }

    fun updateActivities(entity: TinyEnemyJellyfishEntity) {
        entity.brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE))
    }
}
