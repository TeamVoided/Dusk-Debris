package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.TimeUtil
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskEntities

class VolaphyraEntity(entityType: EntityType<VolaphyraEntity>, world: Level) :
    AbstractVolaphyraEntity(entityType, world) {

    init {
        this.xpReward = 3
    }

    override fun registerGoals() {
//        goalSelector.add(
//            8, LookAtEntityGoal(
//                this,
//                PlayerEntity::class.java, 8.0f
//            )
//        )
//        goalSelector.add(8, LookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        targetSelector.addGoal(2, ResetUniversalAngerTargetGoal(this, false))
    }

    override fun finalizeSpawn(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData?
    ): SpawnGroupData? {
        isLaunched = false
        hoverPos = this.blockPosition()
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData)
    }

    override fun move(movementType: MoverType, movement: Vec3) {
//        if (!this.isLaunched) {
//            val gravity = this.getAttributeInstance(EntityAttributes.GRAVITY)?.value
//            moveEntities(velocity.add(0.0, gravity!!, 0.0))
//        }
        super.move(movementType, movement)
        if (isLaunched) {
            checkCollisionForPop()
        }
    }

    override fun aiStep() {
        super.aiStep()
//        if (!world.isClient && this.isAlive && this.isLaunched && this.angerTime <= 0) {
//            println(angerTime)
//            isLaunched = false
//        }
        println(propulsionTicks)
    }

    override fun travel(movementInput: Vec3?) {
        if (this.isAlive)
            if (!this.isLaunched) {
                if (this.isEffectiveAi) {
                    this.setDeltaMovement(deltaMovement.scale(0.9))
                    if (this.hoverPos != BlockPos.ZERO) {
                        if (this.propulsionTicks == 10) {
                            propulse()
                        } else if (level() is ServerLevel && ((this.propulsionTicks >= 40 && checkCollisionForPathing()) || this.propulsionTicks >= 60)) {
                            level().broadcastEntityEvent(this, 19.toByte())
                        }
                    }
                    this.moveRelative(0.01f, movementInput)
                    this.move(MoverType.SELF, this.deltaMovement)
                    gravity()
                } else {
                    this.setDeltaMovement(Vec3.ZERO)
                }
            } else {
                this.moveRelative(0.01f, movementInput)
                this.move(MoverType.SELF, this.deltaMovement)
            }
    }

    fun propulse() {
        val direction = this.hoverPos.center.subtract(this.position())
        var moveDirection = direction.normalize()
        moveDirection = moveDirection.scale(0.25)
        if (moveDirection.y < 0)
            moveDirection = moveDirection.multiply(1.0, 0.0, 1.0)
        if (direction.horizontalDistance() < 1)
            moveDirection.multiply(0.5, 1.0, 0.5)
        this.setDeltaMovement(this.deltaMovement.add(moveDirection))
    }

    fun gravity() {
        val gravity = this.getAttribute(Attributes.GRAVITY)?.value
        this.setDeltaMovement(deltaMovement.add(0.0, -gravity!!, 0.0))
    }

    override fun shouldRenderAtSqrDistance(distance: Double): Boolean {
        var averageSideLength = this.boundingBox.size
        if (java.lang.Double.isNaN(averageSideLength)) {
            averageSideLength = 1.0
        }

        averageSideLength *= 64.0 * getViewScale()
        return distance < averageSideLength * averageSideLength
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(HOVER_POS, BlockPos.ZERO)
        builder.define(LAUNCHED, false)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putInt("HoverPosX", hoverPos.x)
        nbt.putInt("HoverPosY", hoverPos.y)
        nbt.putInt("HoverPosZ", hoverPos.z)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        val hoverX = nbt.getInt("HoverPosX")
        val hoverY = nbt.getInt("HoverPosY")
        val hoverZ = nbt.getInt("HoverPosZ")
        hoverPos = BlockPos(hoverX, hoverY, hoverZ)
    }

    override fun setFlying(source: DamageSource, amount: Float) {
        if (amount >= 0) {
            setDeltaMovement(Vec3.ZERO)
            val sourceEntity = source.directEntity
            val sourcePos = source.sourcePosition
            val velocity: Vec3 = if (source.directEntity is Projectile) {
                sourceEntity!!.deltaMovement.scale(-1.0)
            } else if (sourcePos != null) {
                sourcePos.subtract(position())
            } else {
                Vec3(0.0, 1.0, 0.0)
            }
            take3DKnockback(5.0, velocity.x, velocity.y, velocity.z)
        }
    }

    override fun take3DKnockback(strengthInput: Double, x: Double, y: Double, z: Double) {
        isLaunched = true
        super.take3DKnockback(strengthInput, x, y, z)
    }

    override fun onDestroyed() {
        val bombEntity = DuskEntities.VOLAPHYRA_CORE.create(this.level()) as VolaphyraCoreEntity
        if (this.isPersistenceRequired) {
            bombEntity.setPersistenceRequired()
        }
        bombEntity.customName = this.customName
        bombEntity.setNoAi(this.isNoAi)
        bombEntity.isInvulnerable = this.isInvulnerable
        bombEntity.target = target
        bombEntity.targetUuid = targetUuid
        bombEntity.moveTo(
            this.x, this.y + this.bbHeight / 2 - bombEntity.bbHeight / 2, this.z,
            random.nextFloat() * 360.0f, 0.0f
        )
        level().addFreshEntity(bombEntity)
        super.onDestroyed()
    }

    var isLaunched: Boolean
        get() = entityData.get(LAUNCHED)
        set(boolean) = entityData.set(LAUNCHED, boolean)

    var hoverPos: BlockPos
        get() = entityData.get(HOVER_POS)
        set(pos) = entityData.set(HOVER_POS, pos)

    override fun startPersistentAngerTimer() {
        this.remainingPersistentAngerTime = ANGER_TIME_RANGE.sample(random)
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

    override fun doPush(entity: Entity) {}

    override fun canCollideWith(other: Entity): Boolean {
        return canCollide(this, other)
    }

    fun canCollide(entity: Entity, other: Entity): Boolean {
        return other != this && (other.canBeCollidedWith() || other.isPushable) && !entity.isPassengerOfSameVehicle(other)
    }

    override fun canBeCollidedWith(): Boolean {
        return this.isAlive && !this.isLaunched
    }

    override fun isPushable(): Boolean {
        return false
    }

//    private fun moveEntities(velocity: Vec3d) {
//        if (velocity.y > 0) {
//            val box = this.bounds.expand(0.1)
//            val entitiesInRange = world.getOtherEntities(this, box) { entityx: Entity ->
//                canMoveEntity(
//                    box,
//                    entityx
//                )
//            }
//            if (entitiesInRange.isNotEmpty()) {
//                entitiesInRange.forEach {
//                    val gravity = it.gravity
//                    moveEntity(it, velocity.add(0.0, gravity, 0.0))
//                    it.velocityModified = true
//                }
//            }
//        }
//    }
//
//    private fun canMoveEntity(box: Box, entity: Entity): Boolean {
//        return true//entity.isOnGround &&
//        //entity.x >= box.minX && entity.x <= box.maxX && entity.z >= box.minZ && entity.z <= box.maxZ
//    }
//
//    private fun moveEntity(entity: Entity, velocity: Vec3d) {
//        entity.move(
//            MovementType.SELF,
//            velocity
//        )
//    }


    companion object {
        private val LAUNCHED: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(VolaphyraEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val HOVER_POS: EntityDataAccessor<BlockPos> =
            SynchedEntityData.defineId(VolaphyraEntity::class.java, EntityDataSerializers.BLOCK_POS)

        private val ANGER_TIME_RANGE: UniformInt = TimeUtil.rangeOfSeconds(10, 30)
        fun createAttributes(): AttributeSupplier.Builder {
            return AbstractVolaphyraEntity.createAttributes()
        }
    }
}