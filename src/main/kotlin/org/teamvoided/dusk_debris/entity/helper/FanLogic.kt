package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

object FanLogic {
    fun Entity.inFanWind(velocity: Vec3d) {
        this.inFanWind(velocity.x, velocity.y, velocity.z)
    }

    fun Entity.inFanWind(x: Double, y: Double, z: Double) {
        val velocity = Vec3d.ZERO
//        if (x != 0.0) velocity.add(lowerOrCombine(x, this.velocity.x), 0.0, 0.0)
//        if (y != 0.0) velocity.add(0.0, lowerOrCombine(y, this.velocity.y), 0.0)
//        if (z != 0.0) velocity.add(0.0, 0.0, lowerOrCombine(z, this.velocity.z))
        velocity.add(
            lowerOrCombine(x, this.velocity.x),
            lowerOrCombine(y, this.velocity.y),
            lowerOrCombine(z, this.velocity.z)
        )
        this.velocity = velocity
    }

    private fun lowerOrCombine(input: Double, old: Double): Double {
        return if (old >= 0) {
            if (input < 0)
                old + input
            else
                Math.max(old, input)
        } else {
            if (input >= 0)
                old + input
            else
                Math.min(old, input)
        }
    }
}