package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.MobEntity
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import java.util.*

open class PickupAndDropItemGoal(
    protected val mob: MobEntity,
    val requirement: Boolean,
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
        if (!requirement) {
            return false
        } else if (mob.target == null && mob.attacker == null) {
            if (mob.random.nextFloat() <= probability) {
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
        val list: List<ItemEntity> = mob.world.getEntitiesByClass(
            ItemEntity::class.java,
            mob.bounds.expand(checkRange, checkRange, checkRange),
            pickableDropFilter
        )
        if (list.isNotEmpty()) {
            mob.navigation.startMovingTo(list.random() as Entity, 1.2, 0)
        }
    }

    open fun EntityNavigation.startMovingTo(entity: Entity?, speed: Double, distance: Int): Boolean {
        val path: Path? = this.findPathTo(entity, distance)
        return path != null && this.startMovingAlong(path, speed)
    }
}