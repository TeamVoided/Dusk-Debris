package org.teamvoided.dusk_debris.entity.reference_delete_later

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.*
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.passive.TadpoleEntity
import net.minecraft.unmapped.C_lygsomtd
import net.minecraft.util.math.int_provider.UniformIntProvider

object ReferenceTadpoleBrain {
    private const val PANICKING_SPEED = 2.0f
    private const val WATER_IDLE_SPEED = 0.5f
    private const val TEMPT_SPEED = 1.25f

    internal fun create(brain: Brain<TadpoleEntity>): Brain<*> {
        addCoreActivities(brain)
        addIdleActivities(brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    private fun addCoreActivities(brain: Brain<TadpoleEntity>) {
        brain.setTaskList(
            Activity.CORE, 0, ImmutableList.of(
                WalkTask(2.0f), LookAroundTask(45, 90), WanderAroundTask(), ReduceCooldownTask(
                    MemoryModuleType.TEMPTATION_COOLDOWN_TICKS
                )
            )
        )
    }

    private fun addIdleActivities(brain: Brain<TadpoleEntity>) {
        brain.setTaskList(
            Activity.IDLE, ImmutableList.of(
                Pair.of(0, (C_lygsomtd.method_47069(EntityType.PLAYER, 6.0f, UniformIntProvider.create(30, 60)))),
                Pair.of(1, (TemptTask { 1.25f })),
                Pair.of(
                    2,
                    CompositeTask(
                        ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET,
                            MemoryModuleState.VALUE_ABSENT
                        ),
                        ImmutableSet.of(),
                        CompositeTask.Order.ORDERED,
                        CompositeTask.RunMode.TRY_ALL,
                        ImmutableList.of(
                            Pair.of(MeanderTask.createSwim(0.5f), 2),
                            Pair.of(GoTowardsLookTarget.create(0.5f, 3), 3),
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

    fun reset(entity: TadpoleEntity) {
        entity.brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE))
    }
}
