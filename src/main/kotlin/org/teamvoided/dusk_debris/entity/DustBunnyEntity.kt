package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.Ownable
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.control.MoveControl
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.BunnyGraveBlockEntity
import org.teamvoided.dusk_debris.particle.DustBunnyParticleEffect
import java.util.*
import kotlin.jvm.optionals.getOrNull

class DustBunnyEntity(entityType: EntityType<out DustBunnyEntity>, world: World) :
    HostileEntity(entityType, world), Ownable {
    var creator: MobEntity? = null
    private var alive = false

    init {
        this.moveControl = NoClipMoveControl(this)
        this.experiencePoints = 3
    }

    override fun initGoals() {
        super.initGoals()
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(4, ChargeTargetGoal())
        goalSelector.add(8, LookAtTargetGoal())
        goalSelector.add(
            9, LookAtEntityGoal(
                this,
                PlayerEntity::class.java, 3.0f, 1.0f
            )
        )
        goalSelector.add(
            10, LookAtEntityGoal(
                this,
                MobEntity::class.java, 8.0f
            )
        )
        targetSelector.add(1, TrackOwnerTargetGoal(this))
        targetSelector.add(
            2, TargetGoal(
                this,
                PlayerEntity::class.java, true
            )
        )
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(TRACKER_CHARGING, false)
        builder.add(TRACKER_SUMMON_POSITION, Optional.empty())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.isCharging = nbt.getBoolean("Charging")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean("Charging", this.isCharging)
    }

    override fun copyFrom(original: Entity) {
        super.copyFrom(original)
        if (original is DustBunnyEntity) {
            this.owner = original.owner
        }
    }

    override fun move(movementType: MovementType, movement: Vec3d) {
        super.move(movementType, movement)
        this.checkBlockCollision()
    }

    override fun onDeath(source: DamageSource) {
        super.onDeath(source)
        if (summonedPos != null) {
            val summonGrave = world.getBlockEntity(summonedPos)
            if (summonGrave is BunnyGraveBlockEntity) {
                summonGrave.removeDustBunny(this)
            }
        }
    }

    override fun tick() {
        this.noClip = true
        super.tick()
        this.noClip = false
        this.setNoGravity(true)
        particles(world, this, 1)
        if (this.alive && !this.hasCustomName() && this.age >= 72000 && age % 20 == 0) {
            this.damage(this.damageSources.starve(), 1.0f)
        }
    }

    override fun getOwner(): MobEntity? {
        return this.creator
    }

    override fun isPushable(): Boolean = false

    override fun pushAway(entity: Entity) {
    }


    var isCharging: Boolean
        get() = dataTracker[TRACKER_CHARGING]
        set(charging) {
            dataTracker[TRACKER_CHARGING] = charging
        }


    var summonedPos: BlockPos?
        get() = dataTracker[TRACKER_SUMMON_POSITION].getOrNull()
        set(pos) {
            dataTracker[TRACKER_SUMMON_POSITION] = Optional.ofNullable(pos)
        }

    fun setOwner(owner: MobEntity?) {
        this.creator = owner
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.ENTITY_VEX_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.ENTITY_VEX_DEATH
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.ENTITY_VEX_HURT
    }

    override fun getLightLevelDependentValue(): Float = 1.0f

    fun particles(world: World, entity: Entity, count: Int, multiplier: Double = 0.1) {
        val rand = world.random
        val entityPos: Vec3d = entity.pos
        repeat(count) {
            val velocity = Vec3d(
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
            )
            world.addParticle(
                DustBunnyParticleEffect(0xCCBA76, 0x896230),
                true,
                entityPos.x + ((rand.nextDouble() - rand.nextDouble()) * entity.width),
                entityPos.y + (rand.nextDouble() * entity.height),
                entityPos.z + ((rand.nextDouble() - rand.nextDouble()) * entity.width),
                velocity.x,
                velocity.y,
                velocity.z,
            )
        }
    }

    private inner class NoClipMoveControl(bunnyEntity: DustBunnyEntity) : MoveControl(bunnyEntity) {
        override fun tick() {
            if (this.state == State.MOVE_TO) {
                val vec3d = Vec3d(
                    this.targetX - this@DustBunnyEntity.x,
                    this.targetY - this@DustBunnyEntity.y,
                    this.targetZ - this@DustBunnyEntity.z
                )
                val distance = vec3d.length()
                if (distance < bounds.averageSideLength / 2) {
                    this.state = State.WAIT
                    this@DustBunnyEntity.velocity = velocity.multiply(0.5)
                } else {
                    this@DustBunnyEntity.velocity =
                        velocity.add(vec3d.multiply((this.speed * 0.05 / distance)))
                    if (this@DustBunnyEntity.target == null) {
                        val velocity = this@DustBunnyEntity.velocity
                        this@DustBunnyEntity.yaw = -(MathHelper.atan2(velocity.x, velocity.z).toFloat()) * 57.295776f
                        this@DustBunnyEntity.bodyYaw = this@DustBunnyEntity.yaw
                    } else {
                        val e = target!!.x - this@DustBunnyEntity.x
                        val f = target!!.z - this@DustBunnyEntity.z
                        this@DustBunnyEntity.yaw = -(MathHelper.atan2(e, f).toFloat()) * 57.295776f
                        this@DustBunnyEntity.bodyYaw = this@DustBunnyEntity.yaw
                    }
                }
            }
        }
    }

    private inner class ChargeTargetGoal : Goal() {
        init {
            this.controls = EnumSet.of(Control.MOVE)
        }

        override fun canStart(): Boolean {
            val livingEntity = this@DustBunnyEntity.target
            return if (
                (livingEntity != null && livingEntity.isAlive && !getMoveControl().isMoving) &&
                random.nextInt(toGoalTicks(7)) == 0
            ) {
                this@DustBunnyEntity.squaredDistanceTo(livingEntity) > 8.0
            } else {
                false
            }
        }

        override fun shouldContinue(): Boolean {
            return getMoveControl().isMoving &&
                    this@DustBunnyEntity.isCharging &&
                    (this@DustBunnyEntity.target != null) &&
                    target!!.isAlive
        }

        override fun start() {
            val livingEntity = this@DustBunnyEntity.target
            if (livingEntity != null) {
                val vec3d = livingEntity.eyePos
                moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0)
            }

            this@DustBunnyEntity.isCharging = true
            this@DustBunnyEntity.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0f, 1.0f)
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
                if (getBounds().intersects(livingEntity.bounds)) {
                    this@DustBunnyEntity.tryAttack(livingEntity)
                    this@DustBunnyEntity.isCharging = false
                } else {
                    val d = this@DustBunnyEntity.squaredDistanceTo(livingEntity)
                    if (d < 9.0) {
                        val vec3d = livingEntity.eyePos
                        moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0)
                    }
                }
            }
        }
    }

    private inner class LookAtTargetGoal : Goal() {
        init {
            this.controls = EnumSet.of(Control.MOVE)
        }

        override fun canStart(): Boolean {
            return !getMoveControl().isMoving && random.nextInt(toGoalTicks(7)) == 0
        }

        override fun shouldContinue(): Boolean {
            return false
        }

        override fun tick() {
            val blockPos: BlockPos? = this@DustBunnyEntity.blockPos
//            if (blockPos == null) {
//                blockPos = this@NightmareEntity.blockPos
//            }

            for (i in 0..2) {
                val blockPos2 = blockPos!!.add(
                    random.nextInt(15) - 7,
                    random.nextInt(11) - 5, random.nextInt(15) - 7
                )
                if (world.isAir(blockPos2)) {
                    moveControl.moveTo(
                        blockPos2.x.toDouble() + 0.5,
                        blockPos2.y.toDouble() + 0.5,
                        blockPos2.z.toDouble() + 0.5,
                        0.25
                    )
                    if (this@DustBunnyEntity.target == null) {
                        getLookControl().lookAt(
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

    internal inner class TrackOwnerTargetGoal(mob: PathAwareEntity?) : TrackTargetGoal(mob, false) {
        private val trackOwnerPredicate: TargetPredicate =
            TargetPredicate.createNonAttackable().ignoreVisibility().ignoreDistanceScalingFactor()

        override fun canStart(): Boolean {
            return this@DustBunnyEntity.owner != null && (owner!!.target != null) && this.canTrack(
                owner!!.target, this.trackOwnerPredicate
            )
        }

        override fun start() {
            this@DustBunnyEntity.target = owner!!.target
            super.start()
        }
    }

    companion object {

        val TRACKER_CHARGING: TrackedData<Boolean> = DataTracker.registerData(
            DustBunnyEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN
        )

        val TRACKER_SUMMON_POSITION: TrackedData<Optional<BlockPos>> = DataTracker.registerData(
            DustBunnyEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS
        )

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 5.0)
        }
    }
}