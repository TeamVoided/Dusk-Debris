package org.teamvoided.dusk_debris.entity

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
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.init.DuskEntities

class VolaphyraEntity(entityType: EntityType<out VolaphyraEntity>, world: World) :
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
    }

    override fun travel(movementInput: Vec3d?) {
        if (this.isAlive)
            if (!this.isLaunched) {
                if (this.canAiMove()) {
                    val gravity = this.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)?.value
                    this.updateVelocity(0.01f, movementInput)
                    this.move(MovementType.SELF, this.velocity)
                    this.velocity = velocity.multiply(0.9)
                    if (this.hoverPos != BlockPos.ORIGIN && (this.age % 60 == 0 || checkCollisionForPathing())) {
                        val distance = this.hoverPos.ofCenter().subtract(this.pos)
                        var moveDirection = distance.normalize().multiply(0.25)
                        if (moveDirection.y < 0)
                            moveDirection = moveDirection.multiply(1.0, -0.5, 1.0)
                        this.velocity = this.velocity.add(moveDirection)
                    }
                    this.velocity = velocity.add(0.0, -gravity!!, 0.0)
                } else {
                    this.velocity = Vec3d.ZERO
                }
            } else {
                this.updateVelocity(1f, movementInput)
                this.move(MovementType.SELF, this.velocity)
            }
    }

    override fun shouldRender(distance: Double): Boolean {
        var averageSideLength = this.bounds.averageSideLength * 3
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
        this.writeAngerToNbt(nbt)
        nbt.putInt("HoverPosX", hoverPos.x)
        nbt.putInt("HoverPosY", hoverPos.y)
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

    override fun setFlying(source: DamageSource, amount: Float) {
        velocity = Vec3d.ZERO
        val sourcePos = source.position
        val velocity = if (sourcePos != null) {
            pos.subtract(sourcePos).toVector3f()
        } else {
            Vector3f(0.0f, 1.0f, 0.0f)
        }
        isLaunched = true
        this.velocityDirty = true
        val vec3d2 = Vec3d(velocity).normalize().multiply(5.0)
        this.velocity = vec3d2
    }

    override fun onDestroyed() {
        super.onDestroyed()
        val text = this.customName
        val disabledAI = this.isAiDisabled
        val bombEntity = DuskEntities.VOLAPHYRA_CORE.create(this.world) as VolaphyraCoreEntity
        if (this.isPersistent) {
            bombEntity.setPersistent()
        }
        bombEntity.customName = text
        bombEntity.isAiDisabled = disabledAI
        bombEntity.isInvulnerable = this.isInvulnerable
        bombEntity.target = target
        bombEntity.targetUuid = targetUuid
        bombEntity.refreshPositionAndAngles(
            this.x, this.y + this.height / 2 - bombEntity.height / 2, this.z,
            random.nextFloat() * 360.0f, 0.0f
        )
        world.spawnEntity(bombEntity)
    }

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
        private val LAUNCH_GRAVITY_MODIFIER_ID: Identifier = id("launch_gravity")
        private val LAUNCH_GRAVITY_MODIFIER = EntityAttributeModifier(
            LAUNCH_GRAVITY_MODIFIER_ID,
            GRAVITY_VALUE,
            EntityAttributeModifier.Operation.ADD_VALUE
        )
        private const val MIN_ANGER_LOSS_TIME = 600
        private val LAUNCHED: TrackedData<Boolean> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val HOVER_POS: TrackedData<BlockPos> =
            DataTracker.registerData(VolaphyraEntity::class.java, TrackedDataHandlerRegistry.BLOCK_POS)

        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(10, 30)
        fun createAttributes(): DefaultAttributeContainer.Builder{
            return AbstractVolaphyraEntity.createAttributes()
        }
    }
}