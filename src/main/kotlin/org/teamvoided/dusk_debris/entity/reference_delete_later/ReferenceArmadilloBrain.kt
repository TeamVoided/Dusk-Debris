package org.teamvoided.dusk_debris.entity.reference_delete_later

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.kinds.Const
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import net.minecraft.entity.EntityTrigger
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ReportingTaskControl
import net.minecraft.entity.ai.brain.*
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.passive.ArmadilloEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.unmapped.C_lygsomtd
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.int_provider.UniformIntProvider
import java.util.Map
import java.util.Set
import java.util.function.Function
import java.util.function.Predicate

object ReferenceArmadilloBrain {
    private const val PANICKING_SPEED_MULTIPLIER = 2.0f
    private const val IDLING_SPEED_MULTIPLIER = 1.0f
    private const val TEMPTED_SPEED_MULTIPLIER = 1.25f
    private const val FOLLOWING_ADULT_SPEED_MULTIPLIER = 1.25f
    private const val BREEDING_SPEED_MULTIPLIER = 1.0f
    private const val DEFAULT_TEMPTED_STOP_DISTANCE = 2.0
    private const val BABY_TEMPTED_STOP_DISTANCE = 1.0
    private val WALK_TOWARD_ADULT_RANGE: UniformIntProvider = UniformIntProvider.create(5, 16)
    private val SENSORS: List<SensorType<out Sensor<in ArmadilloEntity>>> =
        listOf(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            SensorType.ARMADILLO_TEMPTATIONS,
            SensorType.NEAREST_ADULT,
            SensorType.ARMADILLO_SCARE_DETECTED
        )
    private val MEMORY_MODULES: List<MemoryModuleType<out Any>> =
        listOf(
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.GAZE_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,

            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.DANGER_DETECTED_RECENTLY
        )
    private val UNROLL_TASK: ReportingTaskControl<ArmadilloEntity> =
        TaskBuilder.task { instance: TaskBuilder.Instance<ArmadilloEntity> ->
            instance.group(
                instance.absentMemory(
                    MemoryModuleType.DANGER_DETECTED_RECENTLY
                )
            ).apply(instance) { memoryAccessor: MemoryAccessor<Const.Mu<Unit>, Boolean> ->
                EntityTrigger { world: ServerWorld?, armadilloEntity: ArmadilloEntity, l: Long ->
                    if (armadilloEntity.isNotIdle) {
                        armadilloEntity.unroll()
                        return@EntityTrigger true
                    } else {
                        return@EntityTrigger false
                    }
                }
            }
        }

    fun createProfile() = Brain.createProfile<ArmadilloEntity>(MEMORY_MODULES, SENSORS as Nothing?)


    internal fun create(brain: Brain<ArmadilloEntity>): Brain<*> {
        addCoreActivities(brain)
        addIdleActivities(brain)
        addPanicActivities(brain)
        brain.setCoreActivities(Set.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    private fun addCoreActivities(brain: Brain<ArmadilloEntity>) {
        brain.setTaskList(
            Activity.CORE,
            0,
            ImmutableList.of(
                StayAboveWaterTask(0.8f),
                PanicTask(2.0f),
                LookAroundTask(45, 90),
                object : WanderAroundTask() {
                    override fun shouldRun(world: ServerWorld, mobEntity: MobEntity): Boolean {
                        if (mobEntity is ArmadilloEntity) {
                            if (mobEntity.isNotIdle) {
                                return false
                            }
                        }

                        return super.shouldRun(world, mobEntity)
                    }
                },
                ReduceCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                ReduceCooldownTask(MemoryModuleType.GAZE_COOLDOWN_TICKS),
                UNROLL_TASK
            )
        )
    }

    private fun addIdleActivities(brain: Brain<ArmadilloEntity>) {
        brain.setTaskList(
            Activity.IDLE, ImmutableList.of(
                Pair.of(0, C_lygsomtd.method_47069(EntityType.PLAYER, 6.0f, UniformIntProvider.create(30, 60))),
                Pair.of(1, BreedTask(EntityType.ARMADILLO, 1.0f, 1)),
                Pair.of(
                    2, RandomTask(
                        ImmutableList.of(
                            Pair.of(
                                TemptTask(
                                    { armadillo: LivingEntity? -> 1.25f },
                                    { armadillo: LivingEntity -> if (armadillo.isBaby) 1.0 else 2.0 }), 1
                            ),
                            Pair.of(WalkTowardClosestAdultTask.create(WALK_TOWARD_ADULT_RANGE, 1.25f), 1)
                        )
                    )
                ),
                Pair.of(3, RandomLookAroundTask(UniformIntProvider.create(150, 250), 30.0f, 0.0f, 0.0f)),
                Pair.of(
                    4,
                    RandomTask(
                        ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET,
                            MemoryModuleState.VALUE_ABSENT
                        ),
                        ImmutableList.of(
                            Pair.of(MeanderTask.create(1.0f), 1),
                            Pair.of(GoTowardsLookTarget.create(1.0f, 3), 1),
                            Pair.of(
                                WaitTask(30, 60), 1
                            )
                        )
                    )
                )
            )
        )
    }

    private fun addPanicActivities(brain: Brain<ArmadilloEntity>) {
        brain.setTaskList(
            Activity.PANIC,
            ImmutableList.of(Pair.of(0, RollUpTask())),
            Set.of(
                Pair.of(MemoryModuleType.DANGER_DETECTED_RECENTLY, MemoryModuleState.VALUE_PRESENT),
                Pair.of(MemoryModuleType.IS_PANICKING, MemoryModuleState.VALUE_ABSENT)
            )
        )
    }

    fun updateActivities(armadillo: ArmadilloEntity) {
        armadillo.brain.resetPossibleActivities(ImmutableList.of(Activity.PANIC, Activity.IDLE))
    }

    val breedingIngredient: Predicate<ItemStack>
        get() = Predicate { stack: ItemStack -> stack.isIn(ItemTags.ARMADILLO_FOOD) }

    class PanicTask(f: Float) : WalkTask<ArmadilloEntity>(
        f,
        Function { pathAwareEntity: PathAwareEntity? -> DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES }) {
        override fun run(world: ServerWorld, armadilloEntity: ArmadilloEntity, l: Long) {
            armadilloEntity.unroll()
            super.run(world, armadilloEntity, l)
        }
    }

    class RollUpTask : Task<ArmadilloEntity>(Map.of(), RUN_TIME_TICKS) {
        var ticksUntilPeek: Int = 0
        var foundDanger: Boolean = false

        override fun keepRunning(world: ServerWorld, armadilloEntity: ArmadilloEntity, l: Long) {
            super.keepRunning(world, armadilloEntity, l)
            if (this.ticksUntilPeek > 0) {
                --this.ticksUntilPeek
            }

            if (armadilloEntity.shouldSwitchToScaredState()) {
                armadilloEntity.state = ArmadilloEntity.State.SCARED
                if (armadilloEntity.isOnGround) {
                    armadilloEntity.playSound(SoundEvents.ENTITY_ARMADILLO_LAND)
                }
            } else {
                val state = armadilloEntity.state
                val m = armadilloEntity.brain.getMemoryExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY)
                val bl = m > 75L
                if (bl != this.foundDanger) {
                    this.ticksUntilPeek = this.calculateTicksUntilPeek(armadilloEntity)
                }

                this.foundDanger = bl
                if (state === ArmadilloEntity.State.SCARED) {
                    if (this.ticksUntilPeek == 0 && armadilloEntity.isOnGround && bl) {
                        world.sendEntityStatus(armadilloEntity, 64.toByte())
                        this.ticksUntilPeek = this.calculateTicksUntilPeek(armadilloEntity)
                    }

                    if (m < ArmadilloEntity.State.UNROLLING.lengthTicks.toLong()) {
                        armadilloEntity.playSound(SoundEvents.ENTITY_ARMADILLO_UNROLL_START)
                        armadilloEntity.state = ArmadilloEntity.State.UNROLLING
                    }
                } else if (state == ArmadilloEntity.State.UNROLLING && m > ArmadilloEntity.State.UNROLLING.lengthTicks.toLong()) {
                    armadilloEntity.state = ArmadilloEntity.State.SCARED
                }
            }
        }

        private fun calculateTicksUntilPeek(armadillo: ArmadilloEntity): Int {
            return ArmadilloEntity.State.SCARED.lengthTicks + armadillo.getRandom().rangeInclusive(100, 400)
        }

        override fun shouldRun(world: ServerWorld, armadilloEntity: ArmadilloEntity): Boolean {
            return armadilloEntity.isOnGround
        }

        override fun shouldKeepRunning(world: ServerWorld, armadilloEntity: ArmadilloEntity, l: Long): Boolean {
            return armadilloEntity.state.isThreatened
        }

        override fun run(world: ServerWorld, armadilloEntity: ArmadilloEntity, l: Long) {
            armadilloEntity.startRolling()
        }

        override fun finishRunning(world: ServerWorld, armadilloEntity: ArmadilloEntity, l: Long) {
            if (!armadilloEntity.canStayRolledUp()) {
                armadilloEntity.unroll()
            }
        }

        companion object {
            val RUN_TIME_TICKS: Int = 5 * TimeHelper.SECONDS_PER_MINUTE * 20
            const val CHECK_FOR_DANGER_INTERVAL: Int = 5
            const val DANGER_DETECTED_RECENTLY_THRESHOLD: Int = 75
        }
    }
}