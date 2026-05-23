package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.Wolf
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.Shapes

abstract class AbstractVolaphyraEntity(entityType: EntityType<out AbstractVolaphyraEntity>, world: Level) :
    AbstractJellyfishEntity(entityType, world) {
    var propulsionTicks: Int = 0
    override fun tick() {
        super.tick()
        this.propulsionTicks++
    }

    override fun move(movementType: MoverType, movement: Vec3) {
        super.move(movementType, movement)
        checkInsideBlocks()
    }

    fun checkCollisionForPop() {
        val box = this.boundingBox.inflate(0.1)
        val blockPos = BlockPos.containing(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7)
        val blockPos2 = BlockPos.containing(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7)
        if (!this.isInvulnerable && !level().isClientSide && level().hasChunksAt(blockPos, blockPos2)) {
            if (this.noPhysics || !this.isAlive) {
                return
            } else {
                val the = checkBlockBoxes(box)
                if (the) {
                    this.onDestroyed()
                }
            }
        }
    }

    fun checkCollisionForPathing(): Boolean { //returns true if there is a collision box below
        val box = this.boundingBox.inflate(0.0, this.bbHeight * 2.0, 0.0).move(0.0, this.bbHeight * -4.0, 0.0)
        val blockPos = BlockPos.containing(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7)
        val blockPos2 = BlockPos.containing(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7)
        if (!this.isInvulnerable && !level().isClientSide && level().hasChunksAt(blockPos, blockPos2)) {
            if (this.noPhysics || !this.isAlive) {
                return false
            } else {
                val belowNotAir = checkBlockBoxes(box)
                if (belowNotAir) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkBlockBoxes(box: AABB): Boolean {
        return BlockPos.betweenClosedStream(box).anyMatch { pos: BlockPos ->
            val blockState = level().getBlockState(pos)
            !blockState.isAir &&
                    Shapes.joinIsNotEmpty(
                        blockState.getCollisionShape(this.level(), pos)
                            .move(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()),
                        Shapes.create(box),
                        BooleanOp.AND
                    )
        }
    }

    override fun travel(movementInput: Vec3?) {}

    override fun updateAnimations() {
        if (propulsionTicks <= 0) {
            idleAnimationState.stop()
        } else {
            idleAnimationState.startIfStopped(this.tickCount)
        }
    }

    override fun handleEntityEvent(status: Byte) {
        if (status.toInt() == 19) {
            this.propulsionTicks = 0
        } else {
            super.handleEntityEvent(status)
        }
    }

    open fun take3DKnockback(strengthInput: Double, x: Double, y: Double, z: Double) {
        var strength = strengthInput
        strength *= 1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)
        if (!(strength <= 0.0)) {
            this.hasImpulse = true
            val vec3d = deltaMovement

            val vec3d2 = Vec3(x, y, z).normalize().scale(strength)
            this.setDeltaMovement(
                vec3d.x / 2.0 - vec3d2.x,
                vec3d.y / 2.0 - vec3d2.y,
                vec3d.z / 2.0 - vec3d2.z
            )
        }
    }

    open fun setFlying(source: DamageSource, amount: Float) {}


    override fun hurt(source: DamageSource, amount: Float): Boolean {
        this.noActionTime = 0
        val entity2 = source.entity
        if (entity2 != null) {
            if (entity2 is LivingEntity) {
                if (!source.`is`(DamageTypeTags.NO_ANGER) &&
                    (!source.`is`(DamageTypes.WIND_CHARGE) || !type.`is`(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE))
                ) {
                    this.setLastHurtByMob(entity2)
                }
            }

            if (entity2 is Player) {
                this.lastHurtByPlayerTime = 100
                this.lastHurtByPlayer = entity2
            } else if (entity2 is Wolf) {
                if (entity2.isTame) {
                    this.lastHurtByPlayerTime = 100
                    val var11 = entity2.owner
                    if (var11 is Player) {
                        this.lastHurtByPlayer = var11
                    } else {
                        this.lastHurtByPlayer = null
                    }
                }
            }
        }
        if (level().isClientSide || this.isDeadOrDying) {
            return false
        } else if (source.`is`(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.type() == this.damageSources().genericKill().type()) {
            this.setFlying(source, amount)
            return super.hurt(source, amount)
        } else if (!source.`is`(DamageTypeTags.NO_KNOCKBACK)) {
            this.setFlying(source, amount)
        }
        return false
    }

    open fun onDestroyed() {
        this.dead = true
        this.discard()
    }

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return AbstractJellyfishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 40.0)
        }
    }
}