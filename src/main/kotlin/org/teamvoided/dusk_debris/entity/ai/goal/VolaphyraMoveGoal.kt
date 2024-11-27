package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.VolaphyraEntity
import java.util.*

class VolaphyraMoveGoal(val entity: VolaphyraEntity, val condition: Boolean) : Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun canStart(): Boolean {
        return !entity.moveControl.isMoving && condition && entity.age % 60 == 0 //&& entity.hoverPos.y > entity.pos.y
    }

    override fun shouldContinue(): Boolean {
        return false
    }

    override fun tick() {
        val blockPosTarget = entity.hoverPos
        if (entity.world.isAir(blockPosTarget)) {
            val movementTarget = Vec3d(
                blockPosTarget.x + 0.5,
                blockPosTarget.y + entity.random.nextDouble(),
                blockPosTarget.z + 0.5
            )
            entity.moveControl.moveTo(
                movementTarget.x,
                movementTarget.y,
                movementTarget.z,
                entity.hoverPos.getSquaredDistanceToCenter(entity.pos) * 0.3
            )
            if (entity.target == null) {
                entity.lookControl.lookAt(
                    movementTarget.x,
                    movementTarget.y,
                    movementTarget.z,
                    45f,
                    5f
                )
            }
        }
    }
}