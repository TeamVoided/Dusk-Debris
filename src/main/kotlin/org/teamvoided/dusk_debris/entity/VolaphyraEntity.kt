package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.ai.goal.UniversalAngerGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.ai.goal.VolaphyraMoveGoal
import org.teamvoided.dusk_debris.entity.ai.movement_control.VolaphraMoveControl
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.world.explosion.custom.DuskExplosion
import java.util.*
import kotlin.math.min

class VolaphyraEntity(entityType: EntityType<out VolaphyraEntity>, world: World) :
    HostileEntity(entityType, world), Angerable {
    private var angerTime = 0
    private var targetUuid: UUID? = null

    init {
        this.moveControl = VolaphraMoveControl(this)
        this.experiencePoints = 3
    }

    override fun initGoals() {
//        goalSelector.add(
//            8, LookAtEntityGoal(
//                this,
//                PlayerEntity::class.java, 8.0f
//            )
//        )
//        goalSelector.add(8, LookAroundGoal(this))
        goalSelector.add(1, VolaphyraMoveGoal(this, !this.isLaunched))
        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(2, UniversalAngerGoal(this, false))
    }

    override fun move(movementType: MovementType, movement: Vec3d) {
        super.move(movementType, movement)
        this.checkBlockCollision()
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        isLaunched = false
        hoverPos = this.blockPos
        return super.initialize(world, difficulty, spawnReason, entityData)
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

    override fun mobTick() {
        super.mobTick()
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(HOVER_POS, BlockPos.ORIGIN)
        builder.add(PROVOKED, false)
        builder.add(LAUNCHED, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        this.writeAngerToNbt(nbt)
        nbt.putInt("HoverPosX", hoverPos.z)
        nbt.putInt("HoverPosY", hoverPos.z)
        nbt.putInt("HoverPosZ", hoverPos.z)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.readAngerFromNbt(this.world, nbt)
        val hoverX = nbt.getInt("HoverPosX")
        val hoverY = nbt.getInt("HoverPosY")
        val hoverZ = nbt.getInt("HoverPosZ")
        hoverPos = BlockPos(hoverX, hoverY, hoverZ)
    }

    override fun setTarget(target: LivingEntity?) {
        super.setTarget(target)
        val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        if (target == null) {
            dataTracker.set(PROVOKED, false)
            entityAttributeInstance!!.removeModifier(ATTACKING_SPEED_MODIFIER_ID)
        } else {
            dataTracker.set(PROVOKED, true)
            if (!entityAttributeInstance!!.hasModifier(ATTACKING_SPEED_MODIFIER_ID)) {
                entityAttributeInstance.addTemporaryModifier(ATTACKING_SPEED_MODIFIER)
            }
        }
    }

    val isProvoked: Boolean
        get() = dataTracker.get(PROVOKED)
    var isLaunched: Boolean
        get() = dataTracker.get(LAUNCHED)
        set(boolean) {
            val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)
            if (boolean) {
                dataTracker.set(LAUNCHED, true)
                entityAttributeInstance!!.removeModifier(LAUNCH_GRAVITY_MODIFIER_ID)
            } else {
                dataTracker.set(LAUNCHED, false)
                if (!entityAttributeInstance!!.hasModifier(LAUNCH_GRAVITY_MODIFIER_ID)) {
                    entityAttributeInstance.addTemporaryModifier(LAUNCH_GRAVITY_MODIFIER)
                }
            }
        }
    var hoverPos: BlockPos
        get() = dataTracker.get(HOVER_POS)
        set(pos) = dataTracker.set(HOVER_POS, pos)

    fun explosion() {
        this.discard()
        val world = world
        if (world is ServerWorld) {
            val attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.value!!.toFloat()
            DuskExplosion(world, 5.0, attackDamage, this.damageSources.sonicBoom(this), DuskParticles.DRAINED_SOUL)
        }
    }


    override fun isPushable(): Boolean = false

    override fun pushAway(entity: Entity) {}

    override fun chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE[random])
    }

    override fun setAngerTime(ticks: Int) {
        this.angerTime = ticks
    }

    override fun getAngerTime(): Int {
        return this.angerTime
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

    override fun getAmbientSound(): SoundEvent? {
        return null
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return null
    }

    override fun getDeathSound(): SoundEvent? {
        return null
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        this.despawnCounter = 0
        if (!source.isTypeIn(DamageTypeTags.NO_KNOCKBACK)) {
            var xVal = 0.0
            var yVal = 0.0
            var zVal = 0.0
            val sourcePos = source.position
            if (sourcePos != null) {
                xVal = sourcePos.getX() - this.x
                yVal = sourcePos.getZ() - this.z
                zVal = sourcePos.getZ() - this.z
            }
            this.takeKnockback(5.0, xVal, yVal, zVal)
        } else if (world.isClient || this.isDead) {
            return false
        } else if (source.isTypeIn(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return true
        return false
    }

    fun takeKnockback(strength: Double, x: Double, y: Double, z: Double) {
        this.velocityDirty = true
        val vec3d2 = Vec3d(x, y, z).normalize().multiply(-strength)
        this.velocity = vec3d2
    }

    override fun onBlockCollision(state: BlockState) {
//        if (state != Blocks.AIR && velocity.length() >= 1) {
//            this.discard()
//        } else
            super.onBlockCollision(state)
    }

    override fun isClimbing(): Boolean = false
    override fun fall(fallDistance: Double, onGround: Boolean, landedState: BlockState, landedPosition: BlockPos) {}

    companion object {
        private val ATTACKING_SPEED_MODIFIER_ID: Identifier = id("attacking")
        private val ATTACKING_SPEED_MODIFIER = EntityAttributeModifier(
            ATTACKING_SPEED_MODIFIER_ID,
            0.1,
            EntityAttributeModifier.Operation.ADD_VALUE
        )
        private val LAUNCH_GRAVITY_MODIFIER_ID: Identifier = id("launch_gravity")
        private val LAUNCH_GRAVITY_MODIFIER = EntityAttributeModifier(
            LAUNCH_GRAVITY_MODIFIER_ID,
            0.001,
            EntityAttributeModifier.Operation.ADD_VALUE
        )
        private const val MIN_ANGER_LOSS_TIME = 600
        private val LAUNCHED: TrackedData<Boolean> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val PROVOKED: TrackedData<Boolean> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val HOVER_POS: TrackedData<BlockPos> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BLOCK_POS)

        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 40.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.GENERIC_GRAVITY, 0.0)
        }
    }
}