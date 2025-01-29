package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.ai.goal.UniversalAngerGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskEntities

class VolaphyraEntity(entityType: EntityType<VolaphyraEntity>, world: World) :
    AbstractVolaphyraEntity(entityType, world) {

    init {
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
        targetSelector.add(1, RevengeGoal(this))
        targetSelector.add(2, UniversalAngerGoal(this, false))
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

    override fun move(movementType: MovementType, movement: Vec3d) {
//        if (!this.isLaunched) {
//            val gravity = this.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)?.value
//            moveEntities(velocity.add(0.0, gravity!!, 0.0))
//        }
        super.move(movementType, movement)
        if (isLaunched) {
            checkCollisionForPop()
        }
    }

    override fun tickMovement() {
        super.tickMovement()
//        if (!world.isClient && this.isAlive && this.isLaunched && this.angerTime <= 0) {
//            println(angerTime)
//            isLaunched = false
//        }
        println(propulsionTicks)
    }

    override fun travel(movementInput: Vec3d?) {
        if (this.isAlive)
            if (!this.isLaunched) {
                if (this.canAiMove()) {
                    this.velocity = velocity.multiply(0.9)
                    if (this.hoverPos != BlockPos.ORIGIN) {
                        if (this.propulsionTicks == 10) {
                            propulse()
                        } else if (world is ServerWorld && ((this.propulsionTicks >= 40 && checkCollisionForPathing()) || this.propulsionTicks >= 60)) {
                            world.sendEntityStatus(this, 19.toByte())
                        }
                    }
                    this.updateVelocity(0.01f, movementInput)
                    this.move(MovementType.SELF, this.velocity)
                    gravity()
                } else {
                    this.velocity = Vec3d.ZERO
                }
            } else {
                this.updateVelocity(0.01f, movementInput)
                this.move(MovementType.SELF, this.velocity)
            }
    }

    fun propulse() {
        val direction = this.hoverPos.ofCenter().subtract(this.pos)
        var moveDirection = direction.normalize()
        moveDirection = moveDirection.multiply(0.25)
        if (moveDirection.y < 0)
            moveDirection = moveDirection.multiply(1.0, 0.0, 1.0)
        if (direction.horizontalLength() < 1)
            moveDirection.multiply(0.5, 1.0, 0.5)
        this.velocity = this.velocity.add(moveDirection)
    }

    fun gravity() {
        val gravity = this.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)?.value
        this.velocity = velocity.add(0.0, -gravity!!, 0.0)
    }

    override fun shouldRender(distance: Double): Boolean {
        var averageSideLength = this.bounds.averageSideLength
        if (java.lang.Double.isNaN(averageSideLength)) {
            averageSideLength = 1.0
        }

        averageSideLength *= 64.0 * getRenderDistanceMultiplier()
        return distance < averageSideLength * averageSideLength
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(HOVER_POS, BlockPos.ORIGIN)
        builder.add(LAUNCHED, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("HoverPosX", hoverPos.x)
        nbt.putInt("HoverPosY", hoverPos.y)
        nbt.putInt("HoverPosZ", hoverPos.z)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        val hoverX = nbt.getInt("HoverPosX")
        val hoverY = nbt.getInt("HoverPosY")
        val hoverZ = nbt.getInt("HoverPosZ")
        hoverPos = BlockPos(hoverX, hoverY, hoverZ)
    }

    override fun setFlying(source: DamageSource, amount: Float) {
        if (amount >= 0) {
            velocity = Vec3d.ZERO
            val sourceEntity = source.source
            val sourcePos = source.position
            val velocity: Vec3d = if (source.source is ProjectileEntity) {
                sourceEntity!!.velocity.multiply(-1.0)
            } else if (sourcePos != null) {
                sourcePos.subtract(pos)
            } else {
                Vec3d(0.0, 1.0, 0.0)
            }
            take3DKnockback(5.0, velocity.x, velocity.y, velocity.z)
        }
    }

    override fun take3DKnockback(strengthInput: Double, x: Double, y: Double, z: Double) {
        isLaunched = true
        super.take3DKnockback(strengthInput, x, y, z)
    }

    override fun onDestroyed() {
        val bombEntity = DuskEntities.VOLAPHYRA_CORE.create(this.world) as VolaphyraCoreEntity
        if (this.isPersistent) {
            bombEntity.setPersistent()
        }
        bombEntity.customName = this.customName
        bombEntity.isAiDisabled = this.isAiDisabled
        bombEntity.isInvulnerable = this.isInvulnerable
        bombEntity.target = target
        bombEntity.targetUuid = targetUuid
        bombEntity.refreshPositionAndAngles(
            this.x, this.y + this.height / 2 - bombEntity.height / 2, this.z,
            random.nextFloat() * 360.0f, 0.0f
        )
        world.spawnEntity(bombEntity)
        super.onDestroyed()
    }

    var isLaunched: Boolean
        get() = dataTracker.get(LAUNCHED)
        set(boolean) = dataTracker.set(LAUNCHED, boolean)

    var hoverPos: BlockPos
        get() = dataTracker.get(HOVER_POS)
        set(pos) = dataTracker.set(HOVER_POS, pos)

    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE[random]
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

    override fun pushAway(entity: Entity) {}

    override fun collidesWith(other: Entity): Boolean {
        return canCollide(this, other)
    }

    fun canCollide(entity: Entity, other: Entity): Boolean {
        return other != this && (other.isCollidable || other.isPushable) && !entity.isConnectedThroughVehicle(other)
    }

    override fun isCollidable(): Boolean {
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
        private val LAUNCHED: TrackedData<Boolean> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val HOVER_POS: TrackedData<BlockPos> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BLOCK_POS)

        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(10, 30)
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractVolaphyraEntity.createAttributes()
        }
    }
}