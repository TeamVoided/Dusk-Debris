package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.core.GlobalPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.behavior.*
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.entity.ai.sensing.Sensor
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.schedule.Activity
import org.teamvoided.dusk_debris.init.brain.DuskSensorType
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
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
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
        brain.useDefaultActivity()
        return brain
    }

    fun createProfile(): Brain.Provider<GiantEnemyJellyfishEntity> =
        Brain.provider(MEMORY_MODULES, SENSORS as Nothing?)


    internal fun setCurrentPosAsHome(jellyfish: GiantEnemyJellyfishEntity) {
        val globalPos = GlobalPos.of(jellyfish.level().dimension(), jellyfish.blockPosition())
        jellyfish.brain.setMemory(MemoryModuleType.HOME, globalPos)
    }


    private fun addCoreTasks(brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(Swim(0.8f), LookAtTargetSink(45, 90)))
    }

    private fun addIdleTasks(brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(
                    0, StartAttacking.create { jellyfish: GiantEnemyJellyfishEntity ->
                        jellyfish.brain.getMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                    }),
                Pair.of(1, StartAttacking.create { it.getRecentAttacker() }),
                Pair.of(2, MoveToTargetSink(20, 40)),
                Pair.of(
                    3, RunOne(
                        ImmutableList.of(
                            Pair.of(DoNothing(20, 100), 1),
                            Pair.of(RandomStroll.stroll(0.6f), 2)
                        )
                    )
                )
            )
        )
    }

    private fun addFightTasks(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.addActivityWithConditions(
            Activity.FIGHT,
            ImmutableList.of<Pair<Int, BehaviorControl<GiantEnemyJellyfishEntity>>>(
                Pair.of(
                    0,
                    StopAttackingIfTargetInvalid.create { target: LivingEntity ->
                        !Sensor.isEntityAttackable(jellyfish, target)
                    }),
//                Pair.of(1, BreezeShootTask()),
//                Pair.of(2, BreezeLongJumpTask()),
//                Pair.of(3, BreezeShootWhenStuckTask()),
//                Pair.of(4, BreezeSlideTask())
            ),
            ImmutableSet.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
            )
        )
    }

    fun updateActivities(jellyfish: GiantEnemyJellyfishEntity) {
        jellyfish.brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
    }
}
