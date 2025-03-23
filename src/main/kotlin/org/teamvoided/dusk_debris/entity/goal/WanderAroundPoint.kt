package org.teamvoided.dusks_and_dungeons.entity.goal

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

class WanderAroundPoint(val entity: PathAwareEntity, val target: BlockPos?, val speed: Double) :
    Goal() {
    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun stop() {
        entity.navigation.stop()
    }

    override fun canStart(): Boolean {
        return  target != null
    }

    override fun tick() {
        if (target != null && entity.navigation.isIdle) {
            if (this.isTooFarFrom(target, 10.0)) {
                val vec3d =
                    Vec3d(
                        target.x.toDouble() - entity.x,
                        target.y.toDouble() - entity.y,
                        target.z.toDouble() - entity.z
                    ).normalize()
                val vec3d2 = vec3d.multiply(10.0).add(
                    entity.x,
                    entity.y,
                    entity.z
                )
                entity.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed)
            } else {
                entity.navigation.startMovingTo(
                    target.x.toDouble(), target.y.toDouble(), target.z.toDouble(),
                    this.speed
                )
            }
        }
    }

    private fun isTooFarFrom(pos: BlockPos, proximityDistance: Double): Boolean {
        return !pos.isCenterWithinDistance(entity.pos, proximityDistance)
    }
}