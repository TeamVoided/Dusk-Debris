package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.World

abstract class AbstractVolaphyraEntity(entityType: EntityType<out AbstractVolaphyraEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world) {
    var propulsionTicks: Int = 0
    override fun tick() {
        super.tick()
        this.propulsionTicks++
    }

    override fun move(movementType: MovementType, movement: Vec3d) {
        super.move(movementType, movement)
        checkBlockCollision()
    }

    fun checkCollisionForPop() {
        val box = this.bounds.expand(0.1)
        val blockPos = BlockPos.create(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7)
        val blockPos2 = BlockPos.create(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7)
        if (!this.isInvulnerable && !world.isClient && world.isRegionLoaded(blockPos, blockPos2)) {
            if (this.noClip || !this.isAlive) {
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
        val box = this.bounds.expand(0.0, this.height * 2.0, 0.0).offset(0.0, this.height * -4.0, 0.0)
        val blockPos = BlockPos.create(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7)
        val blockPos2 = BlockPos.create(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7)
        if (!this.isInvulnerable && !world.isClient && world.isRegionLoaded(blockPos, blockPos2)) {
            if (this.noClip || !this.isAlive) {
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

    private fun checkBlockBoxes(box: Box): Boolean {
        return BlockPos.stream(box).anyMatch { pos: BlockPos ->
            val blockState = world.getBlockState(pos)
            !blockState.isAir &&
                    VoxelShapes.matchesAnywhere(
                        blockState.getCollisionShape(this.world, pos)
                            .offset(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()),
                        VoxelShapes.cuboid(box),
                        BooleanBiFunction.AND
                    )
        }
    }

    override fun travel(movementInput: Vec3d?) {}

    override fun updateAnimations() {
        if (propulsionTicks <= 0) {
            idleAnimationState.stop()
        } else {
            idleAnimationState.start(this.age)
        }
    }

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 19) {
            this.propulsionTicks = 0
        } else {
            super.handleStatus(status)
        }
    }

    open fun take3DKnockback(strengthInput: Double, x: Double, y: Double, z: Double) {
        var strength = strengthInput
        strength *= 1.0 - this.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
        if (!(strength <= 0.0)) {
            this.velocityDirty = true
            val vec3d = velocity

            val vec3d2 = Vec3d(x, y, z).normalize().multiply(strength)
            this.setVelocity(
                vec3d.x / 2.0 - vec3d2.x,
                vec3d.y / 2.0 - vec3d2.y,
                vec3d.z / 2.0 - vec3d2.z
            )
        }
    }

    open fun setFlying(source: DamageSource, amount: Float) {}


    override fun damage(source: DamageSource, amount: Float): Boolean {
        this.despawnCounter = 0
        val entity2 = source.attacker
        if (entity2 != null) {
            if (entity2 is LivingEntity) {
                if (!source.isTypeIn(DamageTypeTags.NO_ANGER) &&
                    (!source.isType(DamageTypes.WIND_CHARGE) || !type.isIn(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE))
                ) {
                    this.attacker = entity2
                }
            }

            if (entity2 is PlayerEntity) {
                this.playerHitTimer = 100
                this.attackingPlayer = entity2
            } else if (entity2 is WolfEntity) {
                if (entity2.isTamed) {
                    this.playerHitTimer = 100
                    val var11 = entity2.owner
                    if (var11 is PlayerEntity) {
                        this.attackingPlayer = var11
                    } else {
                        this.attackingPlayer = null
                    }
                }
            }
        }
        if (world.isClient || this.isDead) {
            return false
        } else if (source.isTypeIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.type == this.damageSources.genericKill().type) {
            this.setFlying(source, amount)
            return super.damage(source, amount)
        } else if (!source.isTypeIn(DamageTypeTags.NO_KNOCKBACK)) {
            this.setFlying(source, amount)
        }
        return false
    }

    open fun onDestroyed() {
        this.dead = true
        this.discard()
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractJellyfishEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 40.0)
        }
    }
}