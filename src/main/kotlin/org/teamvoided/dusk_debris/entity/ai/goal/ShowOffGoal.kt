package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.MobEntity
import net.minecraft.registry.tag.TagKey
import java.util.*
import java.util.function.Predicate

open class ShowOffGoal(
    protected val mob: MobEntity,
    val requirement: Boolean,
    val inclusionSelector:Predicate<LivingEntity>,
    val probability: Double = 1.0,
    val speed: Double = 1.2,
    val distanceAwayFromEndTarget: Int = 1,
    val checkRange: Double = 16.0
) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun canStart(): Boolean {
        if (!requirement) {
            return false
        } else if (mob.target == null && mob.attacker == null) {
            if (mob.random.nextFloat() <= probability) {
                return false
            } else {
                val list: List<LivingEntity> = mob.world.getEntitiesByClass(
                    LivingEntity::class.java,
                    mob.bounds.expand(checkRange, checkRange, checkRange),
                    inclusionSelector
                )
                return list.isNotEmpty()
            }
        } else {
            return false
        }
    }

    override fun start() {
        val list: List<LivingEntity> = mob.world.getEntitiesByClass(
            LivingEntity::class.java,
            mob.bounds.expand(checkRange, checkRange, checkRange),
            inclusionSelector
        )
        if (list.isNotEmpty()) {
            mob.navigation.startMovingTo(list.random() as Entity, speed, distanceAwayFromEndTarget)
        }
    }

    open fun EntityNavigation.startMovingTo(entity: Entity?, speed: Double, distance: Int): Boolean {
        val path: Path? = this.findPathTo(entity, distance)
        return path != null && this.startMovingAlong(path, speed)
    }
}