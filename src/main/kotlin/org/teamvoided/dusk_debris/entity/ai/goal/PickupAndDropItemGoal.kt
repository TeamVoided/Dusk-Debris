package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import java.util.*

open class PickupAndDropItemGoal(
    protected val mob: PathAwareEntity,
    val wantsToPickupItem: Boolean
) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    private val pickableDropFilter = java.util.function.Predicate<ItemEntity> { item: ItemEntity ->
        !item.cannotPickup() && item.isAlive
    }

    override fun canStart(): Boolean {
        if (!mob.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty) {
            return false
        } else if (mob.target == null && mob.attacker == null) {
            if (wantsToPickupItem) {
                return false
            } else if (mob.method_59922().nextInt(toGoalTicks(10)) != 0) {
                return false
            } else {
                val list: List<ItemEntity> = mob.world.getEntitiesByClass(
                    ItemEntity::class.java,
                    mob.bounds.expand(8.0, 8.0, 8.0),
                    pickableDropFilter
                )
                return list.isNotEmpty() && mob.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty
            }
        } else {
            return false
        }
    }

    override fun tick() {
        val list: List<ItemEntity> = mob.world.getEntitiesByClass(
            ItemEntity::class.java,
            mob.bounds.expand(8.0, 8.0, 8.0),
            pickableDropFilter
        )
        val itemStack: ItemStack = mob.getEquippedStack(EquipmentSlot.MAINHAND)
        mob.dropItem(itemStack.item)
        if (itemStack.isEmpty && list.isNotEmpty()) {
            mob.navigation.startMovingTo(list[0] as Entity, 1.2)
        }
    }

    override fun start() {
        val list: List<ItemEntity> = mob.world.getEntitiesByClass(
            ItemEntity::class.java,
            mob.bounds.expand(8.0, 8.0, 8.0),
            pickableDropFilter
        )
        if (list.isNotEmpty()) {
            mob.navigation.startMovingTo(list[0] as Entity, 1.2)
        }
    }
}