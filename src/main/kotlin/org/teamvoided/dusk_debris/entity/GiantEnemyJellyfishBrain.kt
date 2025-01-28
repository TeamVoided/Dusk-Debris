package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.mob.AbstractPiglinEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.util.dynamic.GlobalPos
import java.util.*

object GiantEnemyJellyfishBrain {
    private const val ANGER_DURATION = 600
    private const val MELEE_ATTACK_COOLDOWN = 20
    private const val ACTIVITY_SOUND_PROBABILITY = 0.0125
    private const val MAX_LOOK_DISTANCE = 8f
    private const val INTERACTION_RANGE = 8
    private const val TARGETING_RANGE = 12.0
    private const val IDLING_SPEED = 0.6f
    private const val HOME_CLOSE_ENOUGH_DISTANCE = 2
    private const val HOME_TOO_FAR_DISTANCE = 100
    private const val HOME_STROLL_AROUND_DISTANCE = 5

    internal fun create(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>): Brain<*> {
        addCoreActivities(jellyfish, brain)
        addIdleActivities(jellyfish, brain)
        addFightActivities(jellyfish, brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    internal fun setCurrentPosAsHome(jellyfish: GiantEnemyJellyfishEntity) {
        val globalPos = GlobalPos.create(jellyfish.world.registryKey, jellyfish.blockPos)
        jellyfish.brain.remember(MemoryModuleType.HOME, globalPos)
    }

    private fun addCoreActivities(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.CORE,
            0,
            ImmutableList.of(
                LookAroundTask(45, 90),
                WanderAroundTask(),
                OpenDoorsTask.create(),
                ForgetAngryAtTargetTask.run()
            )
        )
    }

    private fun addIdleActivities(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.IDLE,
            10,
            ImmutableList.of<TaskControl<in GiantEnemyJellyfishEntity>>(
                UpdateAttackTargetTask.create { obj: GiantEnemyJellyfishEntity -> findNearestAttackableTarget(obj) },
                createIdleLookTask(),
                createIdleMovementTask(),
                FindInteractionTargetTask.create(EntityType.PLAYER, 4)
            )
        )
    }

    private fun addFightActivities(jellyfish: GiantEnemyJellyfishEntity, brain: Brain<GiantEnemyJellyfishEntity>) {
        brain.setTaskList(
            Activity.FIGHT,
            10,
            ImmutableList.of(
                ForgetAttackTargetTask.create { target: LivingEntity ->
                    !isNearestAttackableTarget(jellyfish, target)
                },
                RangedApproachTask.create(1.0f), MeleeAttackTask.create(MELEE_ATTACK_COOLDOWN)
            ),
            MemoryModuleType.ATTACK_TARGET
        )
    }

    private fun createIdleLookTask(): RandomTask<GiantEnemyJellyfishEntity> {
        return RandomTask(
            ImmutableList.of(
                Pair.of(FollowMobTask.createMatchingType(EntityType.PLAYER, MAX_LOOK_DISTANCE), 1),
                Pair.of(FollowMobTask.createMatchingType(EntityType.PIGLIN, MAX_LOOK_DISTANCE), 1),
                Pair.of(FollowMobTask.createMatchingType(EntityType.PIGLIN_BRUTE, MAX_LOOK_DISTANCE), 1),
                Pair.of(FollowMobTask.create(8.0f), 1),
                Pair.of(WaitTask(30, 60), 1)
            ) as List<Pair<out TaskControl<in GiantEnemyJellyfishEntity>, Int>>
        )
    }

    private fun createIdleMovementTask(): RandomTask<GiantEnemyJellyfishEntity?> {
        return RandomTask(
            ImmutableList.of(
                Pair.of(MeanderTask.create(IDLING_SPEED), 2),
                Pair.of(
                    FindEntityTask.run(
                        EntityType.PIGLIN,
                        INTERACTION_RANGE,
                        MemoryModuleType.INTERACTION_TARGET,
                        IDLING_SPEED,
                        2
                    ),
                    2
                ),
                Pair.of(
                    FindEntityTask.run(
                        EntityType.PIGLIN_BRUTE,
                        INTERACTION_RANGE,
                        MemoryModuleType.INTERACTION_TARGET,
                        IDLING_SPEED,
                        2
                    ),
                    2
                ),
                Pair.of(
                    GoToNearbyPositionTask.run(
                        MemoryModuleType.HOME,
                        IDLING_SPEED,
                        HOME_CLOSE_ENOUGH_DISTANCE,
                        HOME_TOO_FAR_DISTANCE
                    ),
                    2
                ),
                Pair.of(
                    GoToIfNearbyTask.create(
                        MemoryModuleType.HOME,
                        IDLING_SPEED,
                        HOME_STROLL_AROUND_DISTANCE
                    ),
                    2
                ),
                Pair.of(WaitTask(30, 60), 1)
            )
        )
    }

    internal fun updateActivity(jellyfish: GiantEnemyJellyfishEntity) {
        val brain = jellyfish.brain
        val activity = brain.firstPossibleNonCoreActivity.orElse(null as Activity?) as Activity
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
        val activity2 = brain.firstPossibleNonCoreActivity.orElse(null as Activity?) as Activity
        if (activity !== activity2) {
            playAngrySoundIfFighting(jellyfish)
        }

        jellyfish.isAttacking = brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET)
    }

    private fun isNearestAttackableTarget(jellyfish: GiantEnemyJellyfishEntity, entity: LivingEntity): Boolean {
        return findNearestAttackableTarget(jellyfish).filter { entity2: LivingEntity -> entity2 == entity }.isPresent
    }

    private fun findNearestAttackableTarget(jellyfish: GiantEnemyJellyfishEntity): Optional<out LivingEntity> {
        val optional = LookTargetUtil.getEntity(jellyfish, MemoryModuleType.ANGRY_AT)
        if (optional.isPresent && Sensor.testAttackableTargetPredicateIgnoreVisibility(jellyfish, optional.get())) {
            return optional
        } else {
            val optional2 = findRememberedEntityInRange(jellyfish, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)
            return if (optional2.isPresent) optional2 else jellyfish.brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)
        }
    }

    private fun findRememberedEntityInRange(
        jellyfish: GiantEnemyJellyfishEntity,
        memoryType: MemoryModuleType<out LivingEntity>
    ): Optional<out LivingEntity> {
        return jellyfish.brain.getOptionalMemory(memoryType)
            .filter { jellyfish: LivingEntity -> jellyfish.isInRange(jellyfish, TARGETING_RANGE) }
    }

    internal fun tryRevenge(jellyfish: GiantEnemyJellyfishEntity, target: LivingEntity) {
//        if (target !is GiantEnemyJellyfishEntity) {
//            PiglinBrain.tryRevenge(jellyfish, target)
//        }
    }

    internal fun setAngryAt(jellyfish: GiantEnemyJellyfishEntity, target: LivingEntity) {
        jellyfish.brain.forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)
        jellyfish.brain.remember(MemoryModuleType.ANGRY_AT, target.uuid, 600L)
    }

    internal fun randomlyPlayAngrySoundIfFighting(jellyfish: GiantEnemyJellyfishEntity) {
        if (jellyfish.world.random.nextFloat().toDouble() < ACTIVITY_SOUND_PROBABILITY) {
            playAngrySoundIfFighting(jellyfish)
        }
    }

    private fun playAngrySoundIfFighting(jellyfish: GiantEnemyJellyfishEntity) {
        jellyfish.brain.firstPossibleNonCoreActivity.ifPresent { activity: Activity ->
            if (activity == Activity.FIGHT) {
                jellyfish.playAngrySound()
            }
        }
    }
}
