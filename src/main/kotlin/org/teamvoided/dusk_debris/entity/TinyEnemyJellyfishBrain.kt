package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.behavior.*
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.Sensor
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.schedule.Activity

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
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.IS_PANICKING
        )


    internal fun create(brain: Brain<TinyEnemyJellyfishEntity>): Brain<*> {
        addCoreActivities(brain)
        addIdleActivities(brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.useDefaultActivity()
        return brain
    }

    fun createProfile(): Brain.Provider<TinyEnemyJellyfishEntity> =
        Brain.provider(MEMORY_MODULES, SENSORS)

    private fun addCoreActivities(brain: Brain<TinyEnemyJellyfishEntity>) {
        brain.addActivity(
            Activity.CORE, 0, ImmutableList.of(
                AnimalPanic(PANICKING_SPEED),
                LookAtTargetSink(45, 90),
                MoveToTargetSink()
            )
        )
    }

    private fun addIdleActivities(brain: Brain<TinyEnemyJellyfishEntity>) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0, MoveToTargetSink(20, 100)),
                Pair.of(
                    1, RunOne(
                        ImmutableList.of(
                            Pair.of(DoNothing(20, 100), 1),
                            Pair.of(RandomStroll.stroll(1f), 2),
                            Pair.of(SetWalkTargetFromLookTarget.create(1f, 3), 2)
                        )
                    )
                ),
            )
        )
    }

    fun updateActivities(entity: TinyEnemyJellyfishEntity) {
        entity.brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE))
    }
}
