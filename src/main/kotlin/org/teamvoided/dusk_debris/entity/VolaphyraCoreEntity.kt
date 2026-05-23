package org.teamvoided.dusk_debris.entity

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.util.Utils.RAD_TO_DEG
import org.teamvoided.dusk_debris.world.explosion.custom.DuskExplosion

class VolaphyraCoreEntity(entityType: EntityType<VolaphyraCoreEntity>, world: Level) :
    AbstractVolaphyraEntity(entityType, world) {
    override fun registerGoals() {
        targetSelector.addGoal(
            3, NearestAttackableTargetGoal(
                this,
                LivingEntity::class.java, 5, false, false
            ) { it is Enemy && it !is Creeper && it !is AbstractVolaphyraEntity }
        )
    }

    override fun move(movementType: MoverType, movement: Vec3) {
        super.move(movementType, movement)
        checkCollisionForPop()
    }

    override fun aiStep() {
        super.aiStep()
        if (target != null) {
            val lookX: Double = this.target!!.x - this.x
            val lookY: Double = this.target!!.z - this.z
            this.setYRot(Mth.atan2(lookX, lookY).toFloat() * -RAD_TO_DEG)
            this.yBodyRot = this.yRot

//            this.getLookControl().lookAt(target!!.x, target!!.eyeY, target!!.z)
        }
    }

    override fun travel(movementInput: Vec3?) {
        if (this.isEffectiveAi && this.tickCount > 20) {
            val target = target
            if (target != null) {
                this.move(MoverType.SELF, this.deltaMovement)
                if (this.tickCount % 3 == 0) {
                    val targetPos = target.eyePosition
                    val moveDirection = targetPos.subtract(this.position()).normalize().scale(0.5)
                    this.setDeltaMovement(this.deltaMovement.add(moveDirection))
                }
            } else {
                this.move(MoverType.SELF, this.deltaMovement)
                val gravity = this.getAttribute(Attributes.GRAVITY)?.value
                this.setDeltaMovement(this.deltaMovement.scale(0.9).add(0.0, -gravity!!, 0.0))
            }
        } else {
            this.setDeltaMovement(this.deltaMovement.scale(0.1))
        }
        this.moveRelative(deltaMovement.length().toFloat(), movementInput)
    }

    override fun push(entity: Entity?) {
        if (entity != null) {
            if (entity !is AbstractVolaphyraEntity && entity.isAlive && entity.isAttackable) {
                if (!(entity is Player && (entity.isCreative || entity.isSpectator)))
                    this.onDestroyed()
            }
        }
        super.push(entity)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        val world = level()
        if (world is ServerLevel) {
            val attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE)?.value!!.toFloat()
            DuskExplosion(
                world,
                9.0,
                attackDamage,
                this.damageSources().sonicBoom(this),
                this.position(),
                ParticleTypes.GUST_EMITTER_SMALL
            )
        }
    }

    override fun startPersistentAngerTimer() {}

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return AbstractVolaphyraEntity.createAttributes()
                .add(Attributes.GRAVITY, GRAVITY_VALUE)
        }
    }
}