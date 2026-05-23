package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import java.util.function.Predicate

open class ShowOffGoal(
    protected val mob: Mob,
    val requirement: Boolean,
    val inclusionSelector:Predicate<LivingEntity>,
    val probability: Double = 1.0,
    val speed: Double = 1.2,
    val distanceAwayFromEndTarget: Int = 1,
    val checkRange: Double = 16.0
) : Goal() {
    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }

    override fun canUse(): Boolean {
        if (!requirement) {
            return false
        } else if (mob.target == null && mob.lastHurtByMob == null) {
            if (mob.random.nextFloat() <= probability) {
                return false
            } else {
                val list: List<LivingEntity> = mob.level().getEntitiesOfClass(
                    LivingEntity::class.java,
                    mob.boundingBox.inflate(checkRange, checkRange, checkRange),
                    inclusionSelector
                )
                return list.isNotEmpty()
            }
        } else {
            return false
        }
    }

    override fun start() {
        val list: List<LivingEntity> = mob.level().getEntitiesOfClass(
            LivingEntity::class.java,
            mob.boundingBox.inflate(checkRange, checkRange, checkRange),
            inclusionSelector
        )
        if (list.isNotEmpty()) {
            mob.navigation.startMovingTo(list.random() as Entity, speed, distanceAwayFromEndTarget)
        }
    }

    open fun PathNavigation.startMovingTo(entity: Entity?, speed: Double, distance: Int): Boolean {
        val path: Path? = this.createPath(entity, distance)
        return path != null && this.moveTo(path, speed)
    }
}