package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.item.ItemStack
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import java.util.*

open class PickupAndDropItemGoal(
    protected val golem: TuffGolemEntity,
    val probability: Double = 1.0,
    val checkRange: Double = 8.0
) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    private val pickableDropFilter = java.util.function.Predicate<ItemEntity> { item: ItemEntity ->
        !item.cannotPickup() && item.isAlive && item.age > 30
    }

    override fun canStart(): Boolean {
        if (golem.wasGivenItem() || golem.state?.isStatueMode() == true) {
            return false
        } else if (golem.target == null && golem.attacker == null) {
            if (golem.method_59922().nextFloat() <= probability) {
                return false
            } else {
                val list: List<ItemEntity> = golem.world.getEntitiesByClass(
                    ItemEntity::class.java,
                    golem.bounds.expand(checkRange, checkRange, checkRange),
                    pickableDropFilter
                )
                return list.isNotEmpty()
            }
        } else {
            return false
        }
    }

    override fun start() {
        if (golem.navigation.isIdle) {
            val list: List<ItemEntity> = golem.world.getEntitiesByClass(
                ItemEntity::class.java,
                golem.bounds.expand(checkRange, checkRange, checkRange),
                pickableDropFilter
            )
            if (list.isNotEmpty()) {
                golem.navigation.startMovingTo(list.random() as Entity, 1.2, 0)
            }
        }
    }

    override fun tick() {
        if (golem.navigation.isIdle) {
            val list: List<ItemEntity> = golem.world.getEntitiesByClass(
                ItemEntity::class.java,
                golem.bounds.expand(checkRange, checkRange, checkRange),
                pickableDropFilter
            )
            val mainhand: ItemStack = golem.getEquippedStack(EquipmentSlot.MAINHAND)
            if (list.isNotEmpty()) {
                if (mainhand != ItemStack.EMPTY) {
                    golem.spit(mainhand)
                    golem.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
                }
                golem.navigation.startMovingTo(list.random() as Entity, 1.2, 0)
            }
        }
    }

    open fun EntityNavigation.startMovingTo(entity: Entity?, speed: Double, distance: Int): Boolean {
        val path: Path? = this.findPathTo(entity, distance)
        return path != null && this.startMovingAlong(path, speed)
    }
}