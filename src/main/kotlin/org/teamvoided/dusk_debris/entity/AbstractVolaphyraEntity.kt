package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.World
import java.util.*

abstract class AbstractVolaphyraEntity(entityType: EntityType<out AbstractVolaphyraEntity>, world: World) :
    HostileEntity(entityType, world), Angerable {
    var angerTicks = 0
    var targetUuid: UUID? = null

    init {
        this.setNoGravity(true)
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

    fun checkCollisionForPathing(): Boolean { //returns false if there is a collision box below
        val box = this.bounds.expand(0.0, this.height * 2.0, 0.0).offset(0.0, this.height * -4.0, 0.0)
        val blockPos = BlockPos.create(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7)
        val blockPos2 = BlockPos.create(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7)
        if (!this.isInvulnerable && !world.isClient && world.isRegionLoaded(blockPos, blockPos2)) {
            if (this.noClip || !this.isAlive) {
                return true
            } else {
                val belowNotAir = checkBlockBoxes(box)
                if (belowNotAir) {
                    return false
                }
            }
        }
        return true
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

    override fun tickMovement() {
//        if (world.isClient) {
//                world.addParticle(
//                    ParticleTypes.PORTAL,
//                    this.getParticleX(0.5),
//                    this.randomBodyY - 0.25,
//                    this.getParticleZ(0.5),
//                    (random.nextDouble() - 0.5) * 2.0,
//                    -random.nextDouble(),
//                    (random.nextDouble() - 0.5) * 2.0
//                )
//        }

        this.jumping = false
        if (!world.isClient) {
            this.tickAngerLogic(world as ServerWorld, true)
        }
        super.tickMovement()
    }

    override fun travel(movementInput: Vec3d?) {}

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
        this.onRemoval(RemovalReason.KILLED)
        this.kill()
    }

    override fun isClimbing(): Boolean = false
    override fun fall(fallDistance: Double, onGround: Boolean, landedState: BlockState, landedPosition: BlockPos) {}

    override fun setAngerTime(ticks: Int) {
        this.angerTicks = ticks
    }

    override fun getAngerTime(): Int {
        return this.angerTicks
    }

    override fun setAngryAt(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun getAngryAt(): UUID? {
        return this.targetUuid
    }

    override fun canTarget(type: EntityType<*>): Boolean {
        return true
    }

    companion object {
        const val GRAVITY_VALUE = 0.003
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR, 30.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 40.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.GENERIC_GRAVITY, 0.0)
        }
    }
}