//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.world.GameRules

open class InstantRevengeGoal(mob: PathAwareEntity) : RevengeGoal(mob, *arrayOfNulls(0)) {

    override fun canStart(): Boolean {
        val livingEntity = mob.attacker
        if (livingEntity != null) {
            return if (livingEntity.type === EntityType.PLAYER && mob.world.gameRules.getBooleanValue(GameRules.UNIVERSAL_ANGER)) {
                false
            } else {
                this.canTrack(livingEntity, VALID_AVOIDABLES_PREDICATE)
            }
        } else {
            return false
        }
    }
    companion object{
        private val VALID_AVOIDABLES_PREDICATE: TargetPredicate =
            TargetPredicate.createAttackable().ignoreVisibility().ignoreDistanceScalingFactor()

    }
}