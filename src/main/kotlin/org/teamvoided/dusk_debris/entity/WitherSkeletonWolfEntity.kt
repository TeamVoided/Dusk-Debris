package org.teamvoided.dusk_debris.entity

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities

open class WitherSkeletonWolfEntity(
    entityType: EntityType<out SkeletonWolfEntity>,
    world: Level
) : SkeletonWolfEntity(entityType, world) {
    override var fleeEntity = DuskEntityTypeTags.DUSK_SKELETON_RETREATS
    override var attackEntity = DuskEntityTypeTags.DUSK_SKELETON_ATTACKS

    override fun doHurtTarget(target: Entity?): Boolean {
        if (!super.doHurtTarget(target)) {
            return false
        } else {
            if (target is LivingEntity) {
                target.addEffect(MobEffectInstance(MobEffects.WITHER, 200), this)
            }
            return true
        }
    }

    override fun getDefaultDimensions(pose: Pose): EntityDimensions {
        return if (this.isBaby) WITHER_BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }

    companion object {
        private val WITHER_BABY_DIMENSIONS: EntityDimensions =
            DuskEntities.WITHER_SKELETON_WOLF.dimensions.scale(0.5f).withEyeHeight(0.41f)
    }
}