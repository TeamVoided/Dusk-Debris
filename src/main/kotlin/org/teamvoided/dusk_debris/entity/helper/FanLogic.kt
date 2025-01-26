package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.util.velocityWind

object FanLogic {
    fun Entity.inFanWind(velocity: Vec3d) {
        this.inFanWind(velocity.x, velocity.y, velocity.z)
    }

    fun Entity.inFanWind(x: Double, y: Double, z: Double) {
        if (this !is PlayerEntity) return
        this.velocityWind = Vec3d(
            lowerOrCombine(x, this.velocityWind.x),
            lowerOrCombine(y, this.velocityWind.y),
            lowerOrCombine(z, this.velocityWind.z)
        )
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