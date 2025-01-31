package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.*
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.passive.TadpoleEntity
import net.minecraft.unmapped.C_lygsomtd
import net.minecraft.util.math.int_provider.UniformIntProvider

object TinyEnemyJellyfishBrain {
    private const val PANICKING_SPEED = 2f
    private const val WATER_IDLE_SPEED = 0.5f


    val SENSORS: List<SensorType<out Sensor<in TinyEnemyJellyfishEntity>>> =
        listOf(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
        )
    val MEMORY_MODULES: List<MemoryModuleType<out Any>> =
        listOf(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,
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
        Brain.createProfile(MEMORY_MODULES, SENSORS as Nothing?)

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
                Pair.of(0, (C_lygsomtd.method_47069(EntityType.PLAYER, 6.0f, UniformIntProvider.create(30, 60)))),
                Pair.of(
                    1,
                    CompositeTask(
                        ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET,
                            MemoryModuleState.VALUE_ABSENT
                        ),
                        ImmutableSet.of(),
                        CompositeTask.Order.ORDERED,
                        CompositeTask.RunMode.TRY_ALL,
                        ImmutableList.of(
                            Pair.of(MeanderTask.createSwim(WATER_IDLE_SPEED), 2),
                            Pair.of(GoTowardsLookTarget.create(WATER_IDLE_SPEED, 3), 3),
                            Pair.of(
                                TaskBuilder.triggerIf { obj: LivingEntity -> obj.isInsideWaterOrBubbleColumn },
                                5
                            )
                        )
                    )
                )
            )
        )
    }

    fun updateActivities(entity: TinyEnemyJellyfishEntity) {
        entity.brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE))
    }
}
