package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*


open class EnterDarknessGoal(protected val mob: PathfinderMob, private val speed: Double, private val lightThreshold: Int) : Goal() {
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private val world: Level = mob.level()

    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }

    override fun canUse(): Boolean {
        return if (mob.target != null) {
            false
        } else if (world.getMaxLocalRawBrightness(mob.blockPosition()) < lightThreshold) {
            false
        } else {
            this.targetDarkPos()
        }
    }

    protected fun targetDarkPos(): Boolean {
        val vec3d = this.locateDarkPos()
        if (vec3d == null) {
            return false
        } else {
            this.targetX = vec3d.x
            this.targetY = vec3d.y
            this.targetZ = vec3d.z
            return true
        }
    }

    override fun canContinueToUse(): Boolean {
        return !mob.navigation.isDone
    }

    override fun start() {
        mob.navigation.moveTo(this.targetX, this.targetY, this.targetZ, this.speed)
    }

    protected fun locateDarkPos(): Vec3? {
        val randomGenerator = mob.getRandom()
        val blockPos = mob.blockPosition()

        for (i in 0..9) {
            val blockPos2 = blockPos.offset(
                randomGenerator.nextInt(20) - 10,
                randomGenerator.nextInt(6) - 3,
                randomGenerator.nextInt(20) - 10
            )
            if (world.getMaxLocalRawBrightness(blockPos2) < lightThreshold && mob.getWalkTargetValue(blockPos2) < 0.0f) {
                return Vec3.atBottomCenterOf(blockPos2)
            }
        }

        return null
    }
}
