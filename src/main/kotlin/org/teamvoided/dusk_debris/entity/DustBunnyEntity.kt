package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.entity.BunnyGraveBlockEntity
import org.teamvoided.dusk_debris.particle.color.DustBunnyParticleEffect
import java.util.*
import kotlin.jvm.optionals.getOrNull

class DustBunnyEntity(entityType: EntityType<out DustBunnyEntity>, world: Level) :
    Monster(entityType, world), TraceableEntity {
    var creator: Mob? = null
    private var alive = false

    init {
        this.moveControl = NoClipMoveControl(this)
        this.xpReward = 3
    }

    override fun registerGoals() {
        super.registerGoals()
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(4, ChargeTargetGoal())
        goalSelector.addGoal(8, LookAtTargetGoal())
        goalSelector.addGoal(
            9, LookAtPlayerGoal(
                this,
                Player::class.java, 3.0f, 1.0f
            )
        )
        goalSelector.addGoal(
            10, LookAtPlayerGoal(
                this,
                Mob::class.java, 8.0f
            )
        )
        targetSelector.addGoal(1, TrackOwnerTargetGoal(this))
        targetSelector.addGoal(
            2, NearestAttackableTargetGoal(
                this,
                Player::class.java, true
            )
        )
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(TRACKER_CHARGING, false)
        builder.define(TRACKER_SUMMON_POSITION, Optional.empty())
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        this.isCharging = nbt.getBoolean("Charging")
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putBoolean("Charging", this.isCharging)
    }

    override fun restoreFrom(original: Entity) {
        super.restoreFrom(original)
        if (original is DustBunnyEntity) {
            this.owner = original.owner
        }
    }

    override fun move(movementType: MoverType, movement: Vec3) {
        super.move(movementType, movement)
        this.checkInsideBlocks()
    }

    override fun die(source: DamageSource) {
        super.die(source)
        if (summonedPos != null) {
            val summonGrave = level().getBlockEntity(summonedPos)
            if (summonGrave is BunnyGraveBlockEntity) {
                summonGrave.removeDustBunny(this)
            }
        }
    }

    override fun tick() {
        this.noPhysics = true
        super.tick()
        this.noPhysics = false
        this.setNoGravity(true)
        particles(level(), this, 1)
        if (this.alive && !this.hasCustomName() && this.tickCount >= 72000 && tickCount % 20 == 0) {
            this.hurt(this.damageSources().starve(), 1.0f)
        }
    }

    override fun getOwner(): Mob? {
        return this.creator
    }

    override fun isPushable(): Boolean = false

    override fun doPush(entity: Entity) {
    }


    var isCharging: Boolean
        get() = entityData[TRACKER_CHARGING]
        set(charging) {
            entityData[TRACKER_CHARGING] = charging
        }


    var summonedPos: BlockPos?
        get() = entityData[TRACKER_SUMMON_POSITION].getOrNull()
        set(pos) {
            entityData[TRACKER_SUMMON_POSITION] = Optional.ofNullable(pos)
        }

    fun setOwner(owner: Mob?) {
        this.creator = owner
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.VEX_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.VEX_DEATH
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.VEX_HURT
    }

    override fun getLightLevelDependentMagicValue(): Float = 1.0f

    fun particles(world: Level, entity: Entity, count: Int, multiplier: Double = 0.1) {
        val rand = world.random
        val entityPos: Vec3 = entity.position()
        repeat(count) {
            val velocity = Vec3(
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
            )
            world.addParticle(
                DustBunnyParticleEffect(0xCCBA76, 0x896230),
                true,
                entityPos.x + ((rand.nextDouble() - rand.nextDouble()) * entity.bbWidth),
                entityPos.y + (rand.nextDouble() * entity.bbHeight),
                entityPos.z + ((rand.nextDouble() - rand.nextDouble()) * entity.bbWidth),
                velocity.x,
                velocity.y,
                velocity.z,
            )
        }
    }

    private inner class NoClipMoveControl(bunnyEntity: DustBunnyEntity) : MoveControl(bunnyEntity) {
        override fun tick() {
            if (this.operation == Operation.MOVE_TO) {
                val vec3d = Vec3(
                    this.wantedX - this@DustBunnyEntity.x,
                    this.wantedY - this@DustBunnyEntity.y,
                    this.wantedZ - this@DustBunnyEntity.z
                )
                val distance = vec3d.length()
                if (distance < boundingBox.size / 2) {
                    this.operation = Operation.WAIT
                    this@DustBunnyEntity.setDeltaMovement(deltaMovement.scale(0.5))
                } else {
                    this@DustBunnyEntity.setDeltaMovement(deltaMovement.add(vec3d.scale((this.speedModifier * 0.05 / distance))))
                    if (this@DustBunnyEntity.target == null) {
                        val velocity = this@DustBunnyEntity.deltaMovement
                        this@DustBunnyEntity.setYRot(-(Mth.atan2(velocity.x, velocity.z).toFloat()) * 57.295776f)
                        this@DustBunnyEntity.yBodyRot = this@DustBunnyEntity.yRot
                    } else {
                        val e = target!!.x - this@DustBunnyEntity.x
                        val f = target!!.z - this@DustBunnyEntity.z
                        this@DustBunnyEntity.setYRot(-(Mth.atan2(e, f).toFloat()) * 57.295776f)
                        this@DustBunnyEntity.yBodyRot = this@DustBunnyEntity.yRot
                    }
                }
            }
        }
    }

    private inner class ChargeTargetGoal : Goal() {
        init {
            this.setFlags(EnumSet.of(Flag.MOVE))
        }

        override fun canUse(): Boolean {
            val livingEntity = this@DustBunnyEntity.target
            return if (
                (livingEntity != null && livingEntity.isAlive && !getMoveControl().hasWanted()) &&
                random.nextInt(reducedTickDelay(7)) == 0
            ) {
                this@DustBunnyEntity.distanceToSqr(livingEntity) > 8.0
            } else {
                false
            }
        }

        override fun canContinueToUse(): Boolean {
            return getMoveControl().hasWanted() &&
                    this@DustBunnyEntity.isCharging &&
                    (this@DustBunnyEntity.target != null) &&
                    target!!.isAlive
        }

        override fun start() {
            val livingEntity = this@DustBunnyEntity.target
            if (livingEntity != null) {
                val vec3d = livingEntity.eyePosition
                moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0)
            }

            this@DustBunnyEntity.isCharging = true
            this@DustBunnyEntity.playSound(SoundEvents.VEX_CHARGE, 1.0f, 1.0f)
        }

        override fun stop() {
            this@DustBunnyEntity.isCharging = false
        }

        override fun requiresUpdateEveryTick(): Boolean {
            return true
        }

        override fun tick() {
            val livingEntity = this@DustBunnyEntity.target
            if (livingEntity != null) {
                if (boundingBox.intersects(livingEntity.boundingBox)) {
                    this@DustBunnyEntity.doHurtTarget(livingEntity)
                    this@DustBunnyEntity.isCharging = false
                } else {
                    val d = this@DustBunnyEntity.distanceToSqr(livingEntity)
                    if (d < 9.0) {
                        val vec3d = livingEntity.eyePosition
                        moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0)
                    }
                }
            }
        }
    }

    private inner class LookAtTargetGoal : Goal() {
        init {
            this.setFlags(EnumSet.of(Flag.MOVE))
        }

        override fun canUse(): Boolean {
            return !getMoveControl().hasWanted() && random.nextInt(reducedTickDelay(7)) == 0
        }

        override fun canContinueToUse(): Boolean {
            return false
        }

        override fun tick() {
            val blockPos: BlockPos? = this@DustBunnyEntity.blockPosition()
//            if (blockPos == null) {
//                blockPos = this@NightmareEntity.blockPos
//            }

            for (i in 0..2) {
                val blockPos2 = blockPos!!.offset(
                    random.nextInt(15) - 7,
                    random.nextInt(11) - 5, random.nextInt(15) - 7
                )
                if (level().isEmptyBlock(blockPos2)) {
                    moveControl.setWantedPosition(
                        blockPos2.x.toDouble() + 0.5,
                        blockPos2.y.toDouble() + 0.5,
                        blockPos2.z.toDouble() + 0.5,
                        0.25
                    )
                    if (this@DustBunnyEntity.target == null) {
                        getLookControl().setLookAt(
                            blockPos2.x.toDouble() + 0.5,
                            blockPos2.y.toDouble() + 0.5,
                            blockPos2.z.toDouble() + 0.5,
                            180.0f,
                            20.0f
                        )
                    }
                    break
                }
            }
        }
    }

    internal inner class TrackOwnerTargetGoal(mob: PathfinderMob?) : TargetGoal(mob, false) {
        private val trackOwnerPredicate: TargetingConditions =
            TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting()

        override fun canUse(): Boolean {
            return this@DustBunnyEntity.owner != null && (owner!!.target != null) && this.canAttack(
                owner!!.target, this.trackOwnerPredicate
            )
        }

        override fun start() {
            this@DustBunnyEntity.target = owner!!.target
            super.start()
        }
    }

    companion object {

        val TRACKER_CHARGING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(
            DustBunnyEntity::class.java, EntityDataSerializers.BOOLEAN
        )

        val TRACKER_SUMMON_POSITION: EntityDataAccessor<Optional<BlockPos>> = SynchedEntityData.defineId(
            DustBunnyEntity::class.java, EntityDataSerializers.OPTIONAL_BLOCK_POS
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.ATTACK_KNOCKBACK, 5.0)
        }
    }
}