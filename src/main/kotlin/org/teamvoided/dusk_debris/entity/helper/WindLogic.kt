package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.util.velocityWind

object WindLogic {
    fun Entity.inFanWind(velocity: Vec3) {
        this.inFanWind(velocity.x, velocity.y, velocity.z)
    }

    fun Entity.inFanWind(x: Double, y: Double, z: Double) {
        var mult = 1.0
        if (this is Player) {
            if (isCreative && abilities.flying) {
                return
            }
//            mult = 1.25x
            this.setOnGround(false)
        }
        this.resetFallDistance()
        this.hurtMarked = true
        this.velocityWind = Vec3(
            lowerOrCombine(x * mult, this.velocityWind.x),
            lowerOrCombine(y * mult, this.velocityWind.y),
            lowerOrCombine(z * mult, this.velocityWind.z)
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


    fun windLength(world: Level, pos: BlockPos, direction: Direction, maxLength: Int): Int {
        var retorn = maxLength
        for (it in 0 until maxLength) {
            val posCheck = pos.relative(direction, it + 1)
            val worldBlock = world.getBlockState(pos.relative(direction, it + 1))
            if (
                !worldBlock.canBeReplaced() &&
                !worldBlock.`is`(DuskBlockTags.WIND_IGNORE) &&
                (worldBlock.isFaceSturdy(world, posCheck, direction) ||
                        worldBlock.isFaceSturdy(world, posCheck, direction.opposite))
            ) {
                retorn = it
                break
            }
        }
        return retorn
    }
}