package org.teamvoided.dusks_and_dungeons.entity.goal

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.phys.Vec3
import java.util.*

class WanderAroundPoint(val entity: PathfinderMob, val target: BlockPos?, val speed: Double) :
    Goal() {
    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }

    override fun stop() {
        entity.navigation.stop()
    }

    override fun canUse(): Boolean {
        return  target != null
    }

    override fun tick() {
        if (target != null && entity.navigation.isDone) {
            if (this.isTooFarFrom(target, 10.0)) {
                val vec3d =
                    Vec3(
                        target.x.toDouble() - entity.x,
                        target.y.toDouble() - entity.y,
                        target.z.toDouble() - entity.z
                    ).normalize()
                val vec3d2 = vec3d.scale(10.0).add(
                    entity.x,
                    entity.y,
                    entity.z
                )
                entity.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed)
            } else {
                entity.navigation.moveTo(
                    target.x.toDouble(), target.y.toDouble(), target.z.toDouble(),
                    this.speed
                )
            }
        }
    }

    private fun isTooFarFrom(pos: BlockPos, proximityDistance: Double): Boolean {
        return !pos.closerToCenterThan(entity.position(), proximityDistance)
    }
}