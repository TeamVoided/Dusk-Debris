package org.teamvoided.dusk_debris.entity

import net.minecraft.core.Direction
import net.minecraft.core.Rotations
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.util.FastColor
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems

class DiceEntity : AbstractArrow {

    constructor(entityType: EntityType<out DiceEntity>, world: Level) : super(entityType, world)

    constructor(world: Level, owner: LivingEntity, stack: ItemStack) :
            super(DuskEntities.DIE, owner, world, stack, null as ItemStack?) {
        this.color = getDiceColor(stack)
    }

    constructor(world: Level, x: Double, y: Double, z: Double, stack: ItemStack) :
            super(DuskEntities.DIE, x, y, z, world, stack, stack) {
        this.color = getDiceColor(stack)
    }

    var sideUp: Int
        get() = entityData[TRACKER_SIDE_UP]
        set(sideUp) {
            entityData[TRACKER_SIDE_UP] = sideUp
        }

    var rotationVec: Rotations
        get() = entityData[TRACKER_ROTATION]
        set(rotationVec) {
            entityData[TRACKER_ROTATION] = rotationVec
        }

    var timeSinceLastFall: Int
        get() = entityData[TRACKER_TIME_SINCE_LAST_FALL]
        set(timeSinceLastFall) {
            entityData[TRACKER_TIME_SINCE_LAST_FALL] = timeSinceLastFall
        }

    var color: Int
        get() = entityData[TRACKER_COLOR]
        set(color) {
            entityData[TRACKER_COLOR] = color
        }

    init {
        sideUp = 1
//        rotationVec = EulerAngle(random.nextFloat(), random.nextFloat(), random.nextFloat())
        rotationVec = Rotations(0f, 0f, 0f)
        timeSinceLastFall = 20
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(TRACKER_SIDE_UP, 1)
        builder.define(TRACKER_ROTATION, DEFAULT_ROTATION)
        builder.define(TRACKER_TIME_SINCE_LAST_FALL, 20)
        builder.define(TRACKER_COLOR, 0xFFFFFF)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)

        nbt.putInt("SideUp", this.sideUp)
        nbt.put("RotationVector", rotationVec.save())
        nbt.putInt("TimeSinceLastFall", this.timeSinceLastFall)
        nbt.putInt("Color", this.color)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)

        val rotation = nbt.getList("RotationVector", 5)

        this.sideUp = nbt.getInt("SideUp")
        this.rotationVec = (if (rotation.isEmpty()) DEFAULT_ROTATION else Rotations(rotation))
        this.timeSinceLastFall = nbt.getInt("TimeSinceLastFall")
        this.color = nbt.getInt("Color")
    }

    override fun getDefaultPickupItem(): ItemStack = DuskItems.DIE_ITEM.defaultInstance

    override fun shouldRender(cameraX: Double, cameraY: Double, cameraZ: Double): Boolean {
        return true
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        if (level().isClientSide)
            sideUp = random.nextInt(5) + 1
        if (timeSinceLastFall < 4 && blockHitResult.direction == Direction.UP) {
            rotationVec = rotationValues(sideUp)
            super.onHitBlock(blockHitResult)
        } else {
            //            rotationVec = EulerAngle(random.nextFloat(), random.nextFloat(), random.nextFloat())
            setDeltaMovement(
                when (blockHitResult.direction) {
                    Direction.UP -> deltaMovement.multiply(theEvil, theEvilY, theEvil)
                    Direction.DOWN -> deltaMovement.multiply(theEvil, -theEvil, theEvil)
                    Direction.NORTH -> deltaMovement.multiply(theEvil, theEvil, -theEvil)
                    Direction.SOUTH -> deltaMovement.multiply(theEvil, theEvil, -theEvil)
                    Direction.EAST -> deltaMovement.multiply(-theEvil, theEvil, theEvil)
                    Direction.WEST -> deltaMovement.multiply(-theEvil, theEvil, theEvil)
                    else -> deltaMovement
                }
            )
        }
        timeSinceLastFall = 0
    }

    override fun tick() {
        super.tick()
        if (!onGround()) {
//            rotationVec = EulerAngle(
//                rotationVec.pitch * tickRotateMult,
//                rotationVec.yaw * tickRotateMult,
//                rotationVec.roll * tickRotateMult
//            )
        }
        timeSinceLastFall++
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val superResult = super.interact(player, hand)
        if (superResult == InteractionResult.PASS)
            return superResult
        if (player.getItemInHand(hand).isEmpty) {
            player.addItem(pickupItemStackOrigin)
            this.kill()
            return InteractionResult.sidedSuccess(level().isClientSide)
        }
        return InteractionResult.PASS
    }

    override fun tryPickup(player: Player?): Boolean = false
    override fun checkDespawn() {
        super.checkDespawn()
    }

    fun rotationValues(side: Int): Rotations {
        if (!(side >= 1 && side <= 6)) {
            println("oopsie :) --------------------------------------------------------------")
            println(side)
        }
        return when (side) {
            1 -> Rotations(rotate180, 0f, 0f)
            2 -> Rotations(0f, 0f, rotate270)
            3 -> Rotations(rotate90, 0f, 0f)
            4 -> Rotations(rotate270, 0f, 0f)
            5 -> Rotations(0f, 0f, rotate90)
            6 -> Rotations(0f, 0f, 0f)
            else -> Rotations(0f, 0f, 0f)
        }
    }

    override fun getDefaultGravity(): Double {
        return 0.07
    }

    override fun isPushable(): Boolean = false

    fun getDiceColor(itemStack: ItemStack): Int {
        return FastColor.ARGB32.opaque(
            DyedItemColor.getOrDefault(
                itemStack,
                0xFFFFFF
            )
        )
    }

//    private val particleParameters: ParticleEffect
//        get() {
//            val itemStack = this.stack
//            return (if (!itemStack.isEmpty && !itemStack.isOf(this.defaultItem)) ItemStackParticleEffect(
//                ParticleTypes.ITEM,
//                itemStack
//            ) else ParticleTypes.ITEM_SNOWBALL) as ParticleEffect
//        }

//    override fun handleStatus(status: Byte) {
//        if (status.toInt() == 3) {
//            val particleEffect = this.particleParameters
//
//            for (i in 0..7) {
//                world.addParticle(particleEffect, this.x, this.y, this.z, 0.0, 0.0, 0.0)
//            }
//        }
//    }

//    override fun onEntityHit(entityHitResult: EntityHitResult) {
//        super.onEntityHit(entityHitResult)
//        val entity = entityHitResult.entity
//        val i = if (entity is BlazeEntity) 3 else 0
//        entity.damage(this.damageSources.thrown(this, this.owner), i.toFloat())
//    }


//    override fun onCollision(hitResult: HitResult) {
//        super.onCollision(hitResult)
//        if (!world.isClient) {
//            world.sendEntityStatus(this, 3.toByte())
//            this.discard()
//        }
//    }

    companion object {
        val theEvil = 0.75
        val theEvilY = -0.2
        val tickRotateMult = 0.9f
        private val DEFAULT_ROTATION = Rotations(0f, 0f, 0f)
        val TRACKER_SIDE_UP: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            DiceEntity::class.java, EntityDataSerializers.INT
        )
        val TRACKER_ROTATION: EntityDataAccessor<Rotations> = SynchedEntityData.defineId(
            DiceEntity::class.java, EntityDataSerializers.ROTATIONS
        )
        val TRACKER_TIME_SINCE_LAST_FALL: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            DiceEntity::class.java, EntityDataSerializers.INT
        )
        val TRACKER_COLOR: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            DiceEntity::class.java, EntityDataSerializers.INT
        )


        // TODO replace with voidlib
        const val rotate45 = 0.785f
        const val rotate90 = 1.571f
        const val rotate135 = 2.356f
        const val rotate180 = 3.142f
        const val rotate225 = 3.927f
        const val rotate270 = 4.712f
        const val rotate315 = 5.498f
        const val rotate360 = 6.28319f
    }
}