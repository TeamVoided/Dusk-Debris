package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.control.FlightMoveControl
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.ai.pathing.PathNodeType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

class BirdEntity(entityType: EntityType<out BirdEntity>, world: World) : AnimalEntity(entityType, world) {

    init {
        this.moveControl = FlightMoveControl(this, 10, false)
        this.addPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0f)
        this.addPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f)
    }


    var flapProgress: Float = 0f
    var maxWingDeviation: Float = 0f
    var prevMaxWingDeviation: Float = 0f
    var prevFlapProgress: Float = 0f
    private var flapSpeed = 1.0f
    private var nextFlap = 1.0f

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0))
    }


    private fun flapWings() {
        this.prevFlapProgress = this.flapProgress
        this.prevMaxWingDeviation = this.maxWingDeviation
        this.maxWingDeviation += (if (!this.isOnGround && !this.hasVehicle()) 4 else -1).toFloat() * 0.3f
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f)
        if (!this.isOnGround && this.flapSpeed < 1.0f) {
            this.flapSpeed = 1.0f
        }

        this.flapSpeed *= 0.9f
        val vec3d = this.velocity
        if (!this.isOnGround && vec3d.y < 0.0) {
            this.velocity = vec3d.multiply(1.0, 0.6, 1.0)
        }

        this.flapProgress += this.flapSpeed * 2.0f
    }

    override fun hasWings(): Boolean {
        return this.flyDistance > this.nextFlap
    }

    override fun addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f)
        this.nextFlap = this.flyDistance + this.maxWingDeviation / 2.0f
    }

    override fun createChild(world: ServerWorld, entity: PassiveEntity): PassiveEntity? {
        return null
    }

    override fun isBreedingItem(stack: ItemStack): Boolean {
        return false
    }

    fun isInAir(): Boolean = !this.isOnGround

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
        }
    }
}