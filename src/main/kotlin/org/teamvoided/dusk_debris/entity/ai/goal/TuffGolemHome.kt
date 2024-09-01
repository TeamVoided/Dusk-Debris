package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.ai.goal.WanderAroundGoal
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.TuffGolemEntity

open class TuffGolemHome(
    private val golem: TuffGolemEntity,
    speed: Double,
    private val probability: Float = 0.001f
) : WanderAroundGoal(golem, speed) {

    override fun getWanderTarget(): Vec3d? {
        return golem.getSummonedPos()?.method_61082()
    }

    override fun canStart(): Boolean {
        return if (golem.getSummonedPos() != null && mob.method_59922().nextFloat() >= probability) {
            super.canStart()
        } else false
    }

    override fun start() {
        if (golem.getSummonedPos() != null) {
            val target = golem.getSummonedPos()!!.method_61082()
            mob.navigation.startMovingTo(target.x, target.y, target.z, 0, this.speed)
        }
    }
}