package org.teamvoided.dusk_debris.entity.reference_delete_later

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.*
import net.minecraft.entity.ai.brain.task.*
import net.minecraft.entity.passive.AllayEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.unmapped.C_lygsomtd
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.int_provider.UniformIntProvider
import java.util.*
import java.util.function.Predicate

object ReferenceAllayBrain {
    private const val IDLING_SPEED = 1.0f
    private const val FOLLOWING_DEPOSIT_TARGET_SPEED = 2.25f
    private const val RETRIEVING_ITEM_SPEED = 1.75f
    private const val PANICKING_SPEED = 2.5f
    private const val TARGET_COMPLETION_RANGE = 4
    private const val TARGET_SEARCH_RANGE = 16
    private const val MAX_LOOK_DISTANCE = 6
    private const val MIN_WAIT_DURATION = 30
    private const val MAX_WAIT_DURATION = 60
    private const val FORGET_NOTEBLOCK_TIME = 600
    private const val DISTANCE_TO_WANTED_ITEM = 32
    private const val GIVE_ITEM_TIMEOUT = 20

    internal fun create(brain: Brain<AllayEntity>): Brain<*> {
        addCoreActivities(brain)
        addIdleActivities(brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()
        return brain
    }

    private fun addCoreActivities(brain: Brain<AllayEntity>) {
        brain.setTaskList(
            Activity.CORE, 0, ImmutableList.of(
                StayAboveWaterTask(0.8f),
                WalkTask(2.5f),
                LookAroundTask(45, 90),
                WanderAroundTask(),
                ReduceCooldownTask(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS),
                ReduceCooldownTask(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)
            )
        )
    }

    private fun addIdleActivities(brain: Brain<AllayEntity>) {
        brain.setTaskList(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0, WalkToNearestVisibleWantedItemTask.create({ true }, 1.75f, true, 32)),
                Pair.of(1, GiveItemsToTargetTask({ allay: LivingEntity -> getItemDepositPos(allay) }, 2.25f, 20)),
                Pair.of(
                    2, StayCloseToTargetTask.create(
                        { allay: LivingEntity -> getItemDepositPos(allay) },
                        Predicate.not { entity: LivingEntity -> hasNearestVisibleWantedItemModule(entity) },
                        4,
                        16,
                        2.25f
                    )
                ),
                Pair.of(3, C_lygsomtd.method_47067(6.0f, UniformIntProvider.create(30, 60))),
                Pair.of(
                    4, RandomTask(
                        ImmutableList.of(
                            Pair.of(MeanderTask.createFly(1.0f), 2),
                            Pair.of(GoTowardsLookTarget.create(1.0f, 3), 2),
                            Pair.of(
                                WaitTask(30, 60), 1
                            )
                        )
                    )
                )
            ), ImmutableSet.of<Pair<MemoryModuleType<*>, MemoryModuleState>>()
        )
    }

    fun updateActivities(allay: AllayEntity) {
        allay.brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE))
    }

    fun hearNoteblock(allay: LivingEntity, pos: BlockPos?) {
        val brain = allay.brain
        val globalPos = GlobalPos.create(allay.world.registryKey, pos)
        val optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK)
        if (optional.isEmpty) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK, globalPos)
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600)
        } else if (optional.get() == globalPos) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600)
        }
    }

    private fun getItemDepositPos(allay: LivingEntity): Optional<LookTarget> {
        val brain = allay.brain
        val optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK)
        if (optional.isPresent) {
            val globalPos = optional.get()
            if (shouldDepositItemsAtNoteblock(allay, brain, globalPos)) {
                return Optional.of(BlockPosLookTarget(globalPos.pos.up()))
            }

            brain.forget(MemoryModuleType.LIKED_NOTEBLOCK)
        }

        return getLikedPlayerPos(allay)
    }

    private fun hasNearestVisibleWantedItemModule(entity: LivingEntity): Boolean {
        val brain = entity.brain
        return brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
    }

    private fun shouldDepositItemsAtNoteblock(allay: LivingEntity, brain: Brain<*>, globalPos: GlobalPos): Boolean {
        val optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS)
        val world = allay.world
        return world.registryKey === globalPos.dimension && world.getBlockState(globalPos.pos)
            .isOf(Blocks.NOTE_BLOCK) && optional.isPresent
    }

    private fun getLikedPlayerPos(allay: LivingEntity): Optional<LookTarget> {
        return getLikedPlayer(allay).map { player: ServerPlayerEntity? -> EntityLookTarget(player, true) }
    }

    fun getLikedPlayer(entity: LivingEntity): Optional<ServerPlayerEntity> {
        val world = entity.world
        if (!world.isClient() && world is ServerWorld) {
            val optional = entity.brain.getOptionalMemory(MemoryModuleType.LIKED_PLAYER)
            if (optional.isPresent) {
                val entity2 = world.getEntity(optional.get())
                if (entity2 is ServerPlayerEntity) {
                    if ((entity2.interactionManager.isSurvivalLike || entity2.interactionManager.isCreative) &&
                        entity2.isInRange(entity, 64.0)
                    ) {
                        return Optional.of(entity2)
                    }
                }

                return Optional.empty()
            }
        }

        return Optional.empty()
    }
}
