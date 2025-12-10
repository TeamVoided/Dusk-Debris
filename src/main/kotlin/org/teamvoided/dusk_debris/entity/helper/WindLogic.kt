package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.util.velocityWind

object WindLogic {
    fun Entity.inFanWind(velocity: Vec3d) {
        this.inFanWind(velocity.x, velocity.y, velocity.z)
    }

    fun Entity.inFanWind(x: Double, y: Double, z: Double) {
        var mult = 1.0
        if (this is PlayerEntity) {
            if (isCreative && abilities.flying) {
                return
            }
//            mult = 1.25x
            this.isOnGround = false
        }
        this.resetFallDistance()
        this.velocityModified = true
        this.velocityWind = Vec3d(
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


    fun windLength(world: World, pos: BlockPos, direction: Direction, maxLength: Int): Int {
        var retorn = maxLength
        for (it in 0 until maxLength) {
            val posCheck = pos.offset(direction, it + 1)
            val worldBlock = world.getBlockState(pos.offset(direction, it + 1))
            if (
                !worldBlock.materialReplaceable() &&
                !worldBlock.isIn(DuskBlockTags.WIND_IGNORE) &&
                (worldBlock.isSideSolidFullSquare(world, posCheck, direction) ||
                        worldBlock.isSideSolidFullSquare(world, posCheck, direction.opposite))
            ) {
                retorn = it
                break
            }
        }
        return retorn
    }
}