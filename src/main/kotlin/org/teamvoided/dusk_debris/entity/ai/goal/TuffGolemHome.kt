package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import java.util.*

open class TuffGolemHome(
    private val golem: TuffGolemEntity,
    private val speed: Double
) : Goal() {

    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }

    override fun canUse(): Boolean =
        !golem.hasControllingPassenger() &&
                golem.statueTicks > 0 &&
                golem.summonedPos != null

    override fun start() {
        if (golem.summonedPos != null && golem.statueTicks > 0) {
            golem.navigation.stop()
            val target = golem.summonedPos!!.bottomCenter
            golem.navigation.moveTo(target.x, target.y, target.z, 0, this.speed)
        }
    }

    override fun tick() {
        if (golem.summonedPos != null && golem.statueTicks > 0) {
            val summonPos = golem.distanceToSqr(golem.summonedPos!!.bottomCenter)
            if (summonPos < 0.5) {
                if (golem.state != golem.statueState && summonPos < 0.1) {
                    golem.setStateStatue()
                    golem.setYRot((golem.yRot.toInt() / 90) * 90f)
                }
                golem.move(MoverType.SELF, moveTo(golem.summonedPos!!))
            }
            super.tick()
        }
    }

    override fun stop() {
        golem.navigation.stop()
        super.stop()
    }

    private fun moveTo(endPos: BlockPos): Vec3 {
        val x: Double = ((endPos.x + 0.5) - golem.x) / 8
        val z: Double = ((endPos.z + 0.5) - golem.z) / 8
        return Vec3(x, 0.0, z)
    }
}