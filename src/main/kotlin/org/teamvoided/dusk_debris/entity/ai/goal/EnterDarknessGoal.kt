package org.teamvoided.dusk_debris.entity.ai.goal

import jdk.jfr.Threshold
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*


open class EnterDarknessGoal(protected val mob: PathAwareEntity, private val speed: Double, private val lightThreshold: Int) : Goal() {
    private var targetX = 0.0
    private var targetY = 0.0
    private var targetZ = 0.0
    private val world: World = mob.world

    init {
        this.controls = EnumSet.of(Control.MOVE)
    }

    override fun canStart(): Boolean {
        return if (mob.target != null) {
            false
        } else if (world.getLightLevel(mob.blockPos) < lightThreshold) {
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

    override fun shouldContinue(): Boolean {
        return !mob.navigation.isIdle
    }

    override fun start() {
        mob.navigation.startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed)
    }

    protected fun locateDarkPos(): Vec3d? {
        val randomGenerator = mob.method_59922()
        val blockPos = mob.blockPos

        for (i in 0..9) {
            val blockPos2 = blockPos.add(
                randomGenerator.nextInt(20) - 10,
                randomGenerator.nextInt(6) - 3,
                randomGenerator.nextInt(20) - 10
            )
            if (world.getLightLevel(blockPos2) < lightThreshold && mob.getPathfindingFavor(blockPos2) < 0.0f) {
                return Vec3d.ofBottomCenter(blockPos2)
            }
        }

        return null
    }
}
