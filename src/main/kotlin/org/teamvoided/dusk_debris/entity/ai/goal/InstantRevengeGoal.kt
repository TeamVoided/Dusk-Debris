//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.level.GameRules

open class InstantRevengeGoal(mob: PathfinderMob) : HurtByTargetGoal(mob, *arrayOfNulls(0)) {

    override fun canUse(): Boolean {
        val livingEntity = mob.lastHurtByMob
        if (livingEntity != null) {
            return if (livingEntity.type === EntityType.PLAYER && mob.level().gameRules.getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                false
            } else {
                this.canAttack(livingEntity, VALID_AVOIDABLES_PREDICATE)
            }
        } else {
            return false
        }
    }
    companion object{
        private val VALID_AVOIDABLES_PREDICATE: TargetingConditions =
            TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting()

    }
}