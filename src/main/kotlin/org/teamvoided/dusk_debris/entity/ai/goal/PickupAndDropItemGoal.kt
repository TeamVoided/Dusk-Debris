package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import java.util.function.Predicate

open class PickupAndDropItemGoal(
    protected val mob: Mob,
    val requirement: Boolean,
    val probability: Double = 1.0,
    val checkRange: Double = 8.0
) : Goal() {
    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }

    private val pickableDropFilter = Predicate<ItemEntity> { item: ItemEntity ->
        !item.hasPickUpDelay() && item.isAlive && item.tickCount > 30
    }

    override fun canUse(): Boolean {
        if (!requirement) {
            return false
        } else if (mob.target == null && mob.lastHurtByMob == null) {
            if (mob.random.nextFloat() <= probability) {
                return false
            } else {
                val list: List<ItemEntity> = mob.level().getEntitiesOfClass(
                    ItemEntity::class.java,
                    mob.boundingBox.inflate(checkRange, checkRange, checkRange),
                    pickableDropFilter
                )
                return list.isNotEmpty()
            }
        } else {
            return false
        }
    }

    override fun start() {
        val list: List<ItemEntity> = mob.level().getEntitiesOfClass(
            ItemEntity::class.java,
            mob.boundingBox.inflate(checkRange, checkRange, checkRange),
            pickableDropFilter
        )
        if (list.isNotEmpty()) {
            mob.navigation.startMovingTo(list.random() as Entity, 1.2, 0)
        }
    }

    open fun PathNavigation.startMovingTo(entity: Entity?, speed: Double, distance: Int): Boolean {
        val path: Path? = this.createPath(entity, distance)
        return path != null && this.moveTo(path, speed)
    }
}