package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.ai.goal.TargetGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.world.explosion.custom.DuskExplosion
import java.util.function.Predicate

class VolaphyraCoreEntity(entityType: EntityType<out VolaphyraCoreEntity>, world: World) :
    AbstractVolaphyraEntity(entityType, world) {
    override fun initGoals() {
        targetSelector.add(
            3, TargetGoal(
                this,
                MobEntity::class.java, 5, false, false
            ) { it is Monster && it !is CreeperEntity && it !is AbstractVolaphyraEntity }
        )
    }

    override fun move(movementType: MovementType, movement: Vec3d) {
        super.move(movementType, movement)
        checkCollisionForPop()
    }

    override fun travel(movementInput: Vec3d?) {
        if (this.canAiMove() && this.age > 20) {
            val target = target
            if (target != null) {
                val targetPos = target.pos.add(0.0, target.height / 2.0, 0.0)
                this.move(MovementType.SELF, this.velocity)
                this.velocity = velocity.multiply(0.9)
                if (true) {
                    val moveDirection = targetPos.subtract(this.pos).normalize().multiply(0.2)
                    this.velocity = this.velocity.add(moveDirection)
                }
            } else {
                this.move(MovementType.SELF, this.velocity)
                val gravity = this.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)?.value
                this.velocity = this.velocity.multiply(0.1).add(0.0, -5 * gravity!!, 0.0)
            }
        } else {
            this.velocity = this.velocity.multiply(0.1)
        }
        this.updateVelocity(velocity.length().toFloat(), movementInput)
    }

    override fun pushAwayFrom(entity: Entity?) {
        if (entity != null) {
            if (entity !is AbstractVolaphyraEntity && entity.isAlive && entity.isAttackable) {
                if (!(entity is PlayerEntity && (entity.isCreative || entity.isSpectator)))
                    this.onDestroyed()
            }
        }
        super.pushAwayFrom(entity)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        val world = world
        if (world is ServerWorld) {
            val attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.value!!.toFloat()
            DuskExplosion(
                world,
                9.0,
                attackDamage,
                this.damageSources.sonicBoom(this),
                this.pos,
                ParticleTypes.GUST_EMITTER_SMALL
            )
        }
    }

    override fun chooseRandomAngerTime() {}

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractVolaphyraEntity.createAttributes()
                .add(EntityAttributes.GENERIC_GRAVITY, GRAVITY_VALUE)
        }
    }
}