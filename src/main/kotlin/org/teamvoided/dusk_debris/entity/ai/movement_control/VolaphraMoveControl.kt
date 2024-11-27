package org.teamvoided.dusk_debris.entity.ai.movement_control

import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.VolaphyraEntity
import org.teamvoided.dusk_debris.util.Utils.radToDeg

class VolaphraMoveControl(entity: VolaphyraEntity) : MoveControl(entity) {
    override fun tick() {
        if (this.state == State.MOVE_TO) {
            val vec3d = Vec3d(
                this.targetX - entity.x,
                this.targetY - entity.y,
                this.targetZ - entity.z
            )
            val distance = vec3d.length()
            if (distance < entity.bounds.averageSideLength / 2) {
                this.state = State.WAIT
                entity.velocity = entity.velocity.multiply(0.5)
            } else {
                entity.velocity = entity.velocity.add(vec3d.multiply(this.speed * 0.05 / distance))
//                val target = entity.target
//                if (target == null) {
//                    val velocity: Vec3d = entity.velocity
//                    entity.yaw = -(MathHelper.atan2(velocity.x, velocity.z).toFloat()) * radToDeg
//                    entity.bodyYaw = entity.yaw
//                } else {
//                    val targetX: Double = target.x - entity.x
//                    val targetY: Double = target.z - entity.z
//                    entity.yaw = -(MathHelper.atan2(targetX, targetY).toFloat()) * radToDeg
//                    entity.bodyYaw = entity.yaw
//                }
                entity.bodyYaw = entity.yaw
            }
        }
    }
}