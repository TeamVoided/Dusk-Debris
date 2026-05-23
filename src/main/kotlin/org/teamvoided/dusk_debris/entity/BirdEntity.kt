package org.teamvoided.dusk_debris.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.PathType

class BirdEntity(entityType: EntityType<out BirdEntity>, world: Level) : Animal(entityType, world) {

    init {
        this.moveControl = FlyingMoveControl(this, 10, false)
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0f)
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0f)
    }


    var flapProgress: Float = 0f
    var maxWingDeviation: Float = 0f
    var prevMaxWingDeviation: Float = 0f
    var prevFlapProgress: Float = 0f
    private var flapSpeed = 1.0f
    private var nextFlap = 1.0f

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0))
    }


    private fun flapWings() {
        this.prevFlapProgress = this.flapProgress
        this.prevMaxWingDeviation = this.maxWingDeviation
        this.maxWingDeviation += (if (!this.onGround() && !this.isPassenger) 4 else -1).toFloat() * 0.3f
        this.maxWingDeviation = Mth.clamp(this.maxWingDeviation, 0.0f, 1.0f)
        if (!this.onGround() && this.flapSpeed < 1.0f) {
            this.flapSpeed = 1.0f
        }

        this.flapSpeed *= 0.9f
        val vec3d = this.deltaMovement
        if (!this.onGround() && vec3d.y < 0.0) {
            this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0))
        }

        this.flapProgress += this.flapSpeed * 2.0f
    }

    override fun isFlapping(): Boolean {
        return this.flyDist > this.nextFlap
    }

    override fun onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15f, 1.0f)
        this.nextFlap = this.flyDist + this.maxWingDeviation / 2.0f
    }

    override fun getBreedOffspring(world: ServerLevel, entity: AgeableMob): AgeableMob? {
        return null
    }

    override fun isFood(stack: ItemStack): Boolean {
        return false
    }

    fun isInAir(): Boolean = !this.onGround()

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
        }
    }
}