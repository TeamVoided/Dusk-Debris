package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import java.util.*

open class PickupAndDropItemGoal(
    protected val mob: PathAwareEntity,
    val wantsToPickupItem: Boolean,
    val probability: Double = 1.0,
    val soundEvent: SoundEvent = SoundEvents.ENTITY_ITEM_PICKUP,
    val checkRange: Double = 8.0
) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    private val pickableDropFilter = java.util.function.Predicate<ItemEntity> { item: ItemEntity ->
        !item.cannotPickup() && item.isAlive && item.age > 30
    }

    override fun canStart(): Boolean {
        if (!wantsToPickupItem) {
            return false
        } else if (mob.target == null && mob.attacker == null) {
            if (mob.method_59922().nextFloat() <= probability) {
                return false
            } else {
                val list: List<ItemEntity> = mob.world.getEntitiesByClass(
                    ItemEntity::class.java,
                    mob.bounds.expand(checkRange, checkRange, checkRange),
                    pickableDropFilter
                )
                return list.isNotEmpty()
            }
        } else {
            return false
        }
    }

    override fun start() {
        if (mob.navigation.isIdle) {
            val list: List<ItemEntity> = mob.world.getEntitiesByClass(
                ItemEntity::class.java,
                mob.bounds.expand(checkRange, checkRange, checkRange),
                pickableDropFilter
            )
            if (list.isNotEmpty()) {
                mob.navigation.startMovingTo(list.random() as Entity, 1.2)
            }
        }
    }

    override fun tick() {
        if (mob.navigation.isIdle) {
            val list: List<ItemEntity> = mob.world.getEntitiesByClass(
                ItemEntity::class.java,
                mob.bounds.expand(checkRange, checkRange, checkRange),
                pickableDropFilter
            )
            val mainhand: ItemStack = mob.getEquippedStack(EquipmentSlot.MAINHAND)
            if (list.isNotEmpty()) {
                if (mainhand != ItemStack.EMPTY) {
                    spit(mainhand)
                }
                mob.navigation.startMovingTo(list.random() as Entity, 1.2)
            }
        }
    }

    private fun spit(stack: ItemStack) {
        if (!stack.isEmpty && !mob.world.isClient) {
            val itemEntity = ItemEntity(
                mob.world,
                mob.x + mob.rotationVector.x,
                mob.y + 1.0,
                mob.z + mob.rotationVector.z,
                stack.copy()
            )
            itemEntity.setPickupDelay(40)
            itemEntity.setThrower(mob)
            mob.playSound(soundEvent, 1.0f, 1.0f)
            mob.world.spawnEntity(itemEntity)
            val itemStack = mob.getEquippedStack(EquipmentSlot.MAINHAND)
            itemStack.decrement(itemStack.count)
        }
    }
}