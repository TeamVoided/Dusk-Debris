package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities

open class WitherSkeletonWolfEntity(
    entityType: EntityType<out SkeletonWolfEntity>,
    world: World
) : SkeletonWolfEntity(entityType, world) {
    override var fleeEntity = DuskEntityTypeTags.DUSK_SKELETON_RETREATS
    override var attackEntity = DuskEntityTypeTags.DUSK_SKELETON_ATTACKS

    override fun tryAttack(target: Entity?): Boolean {
        if (!super.tryAttack(target)) {
            return false
        } else {
            if (target is LivingEntity) {
                target.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 200), this)
            }
            return true
        }
    }

    override fun getDefaultDimensions(pose: EntityPose): EntityDimensions {
        return if (this.isBaby) WITHER_BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }

    companion object {
        private val WITHER_BABY_DIMENSIONS: EntityDimensions =
            DuskEntities.WITHER_SKELETON_WOLF.dimensions.scaled(0.5f).withEyeHeight(0.41f)
    }
}